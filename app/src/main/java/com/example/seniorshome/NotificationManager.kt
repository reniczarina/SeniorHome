package com.example.seniorshome

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.seniorshome.R

@SuppressLint("MissingPermission")
fun sendNotification(context: Context, taskName: String, taskTime: String) {
    // Ensure the notification channel is created
    createNotificationChannel(context)

    val notification = NotificationCompat.Builder(context, "taskChannel")
        .setSmallIcon(R.drawable.notification_icon) // Ensure this icon exists in your drawable
        .setContentTitle(taskName)
        .setContentText(taskTime)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        // Optional: Add sound or vibration if needed
        .setDefaults(Notification.DEFAULT_ALL) // This will use the default system sound/vibration
        .build()

    val notificationManager = NotificationManagerCompat.from(context)

    // Use a unique ID based on task name or timestamp
    val notificationId = taskName.hashCode()
    notificationManager.notify(notificationId, notification)
}

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Task Notifications"
        val descriptionText = "Notifications for scheduled tasks"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("taskChannel", name, importance).apply {
            description = descriptionText
        }

        // Register the channel with the system
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
