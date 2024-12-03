package com.example.seniorshome

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        // Get the task name and time from the intent
        val taskName = intent.getStringExtra("taskName") ?: "Task"
        val taskTime = intent.getStringExtra("taskTime") ?: "Time"

        // Create the notification
        val notification = NotificationCompat.Builder(context, "taskChannel")
            .setSmallIcon(R.drawable.notification_icon) // Ensure this icon exists in your drawable
            .setContentTitle(taskName)
            .setContentText("Scheduled time: $taskTime")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setDefaults(Notification.DEFAULT_ALL) // Optional: Add sound or vibration
            .build()

        // Get the NotificationManager to display the notification
        val notificationManager = NotificationManagerCompat.from(context)

        // Use a unique ID based on task name or timestamp
        val notificationId = taskName.hashCode()
        notificationManager.notify(notificationId, notification)
    }
}
