package com.example.seniorshome

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.core.app.NotificationCompat
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.net.Uri
import android.os.Handler
import android.os.Looper


class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("ObsoleteSdkInt")
    override fun onReceive(context: Context, intent: Intent) {
        val taskName = intent.getStringExtra("taskName")
        val taskTime = intent.getStringExtra("taskTime")
        val alarmSoundUri = intent.getStringExtra("alarmSoundUri") // URI of the picked sound
        val days = intent.getStringArrayExtra("days")?.toList() // Retrieve the days as a List if needed
        val notificationId = intent.getIntExtra("notificationId", 1) // Notification ID to dismiss

        // Create the notification logic here
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val dismissIntent = Intent(context, DismissReceiver::class.java)
        dismissIntent.putExtra("notificationId", notificationId)

        val dismissPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Add FLAG_IMMUTABLE
        )


        val notificationBuilder = NotificationCompat.Builder(context, "task_channel")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(taskName)  // Show task name in title
            .setContentText("$taskTime")  // Notification text
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(Uri.parse(alarmSoundUri))  // Play alarm sound in notification
            .addAction(R.drawable.ic_dismiss, "Dismiss", dismissPendingIntent) // Add dismiss button

        // Create a notification channel for devices running Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "task_channel",  // Notification channel ID
                "Task Notifications",  // Channel name
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Trigger the notification
        notificationManager.notify(notificationId, notificationBuilder.build())

        // Play the alarm sound continuously
        try {
            val mediaPlayer = MediaPlayer().apply {
                setDataSource(context, Uri.parse(alarmSoundUri)) // Set the URI of the picked sound
                isLooping = true  // Set looping to true so the sound keeps playing
                prepare()
                start()
            }

            // Stop the sound after 30 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                mediaPlayer.stop()
                mediaPlayer.release() // Release the resources
            }, 30000) // Stop after 30 seconds

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
