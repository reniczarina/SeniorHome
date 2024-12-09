package com.example.seniorshome

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.telephony.SmsManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.app.Activity
import androidx.core.app.ActivityCompat

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val REQUEST_CODE_SMS_PERMISSION = 1001
    }

    @SuppressLint("ObsoleteSdkInt", "InlinedApi")
    override fun onReceive(context: Context, intent: Intent) {
        val taskName = intent.getStringExtra("taskName") ?: "Unknown Task"
        val taskTime = intent.getStringExtra("taskTime")
        val alarmSoundUri = intent.getStringExtra("alarmSoundUri")
        val notificationId = intent.getIntExtra("notificationId", 1)
        val phoneNumber = intent.getStringExtra("phoneNumber")  // Family member's phone number

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val dismissIntent = Intent(context, DismissReceiver::class.java)
        dismissIntent.putExtra("notificationId", notificationId)

        val dismissPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            dismissIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(context, "task_channel")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(taskName)
            .setContentText("$taskTime")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(Uri.parse(alarmSoundUri))
            .addAction(R.drawable.ic_dismiss, "Dismiss", dismissPendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "task_channel",
                "Task Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())

        var mediaPlayer: MediaPlayer? = null

        // Play alarm sound continuously
        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(context, Uri.parse(alarmSoundUri))
                isLooping = true
                prepare()
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Handler to send SMS after 60 seconds if the alarm is not dismissed
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            sendSmsIfNotDismissed(context, phoneNumber, taskName)
            mediaPlayer?.stop() // Stop the music after 60 seconds
            mediaPlayer?.release()
        }, 60 * 1000L)

        // Register dismiss receiver to stop alarm and SMS sending if dismissed
        val dismissReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                handler.removeCallbacksAndMessages(null)  // Remove pending SMS
                mediaPlayer?.stop()  // Stop the alarm music
                mediaPlayer?.release()  // Release the media player resources
                context.unregisterReceiver(this)  // Unregister receiver
            }
        }

        context.registerReceiver(dismissReceiver,
            IntentFilter("com.example.seniorshome.DISMISS_ACTION"),
            Context.RECEIVER_NOT_EXPORTED
        )
    }

    private fun sendSmsIfNotDismissed(context: Context, phoneNumber: String?, taskName: String) {
        if (phoneNumber.isNullOrEmpty()) return

        // Check if SMS permissions are granted
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            try {
                val smsManager: SmsManager = SmsManager.getDefault()
                val message = "The Alarm '$taskName' was missed. Please check."
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                Log.d("AlarmReceiver", "SMS sent to $phoneNumber: $message")

                // Add missed notification to shared preferences or a shared data store
                addMissedNotification(context, "Missed Alarm: $taskName.")
            } catch (e: Exception) {
                Log.e("AlarmReceiver", "Error sending SMS: ${e.message}")
            }
        }
    }

    private fun addMissedNotification(context: Context, message: String) {
        val sharedPreferences = context.getSharedPreferences("Notifications", Context.MODE_PRIVATE)
        val notifications = sharedPreferences.getStringSet("notifications", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        notifications.add(message)

        sharedPreferences.edit().putStringSet("notifications", notifications).apply()
        Log.d("AlarmReceiver", "Notification stored: $message")
    }

}
