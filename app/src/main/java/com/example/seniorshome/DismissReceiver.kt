package com.example.seniorshome

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationManager
import android.media.MediaPlayer

class DismissReceiver : BroadcastReceiver() {

    companion object {
        var mediaPlayer: MediaPlayer? = null // Shared MediaPlayer instance to stop the alarm sound
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("notificationId", 1)

        // Cancel the notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)

        // Stop the alarm sound if it's playing
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
                it.release()
            }
            mediaPlayer = null // Reset the mediaPlayer instance
        }
    }
}
