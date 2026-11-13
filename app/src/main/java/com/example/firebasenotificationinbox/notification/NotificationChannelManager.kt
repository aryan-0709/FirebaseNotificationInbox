package com.example.firebasenotificationinbox.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationChannelManager {

    const val CHANNEL_ID = "assignment"

    fun createNotificationChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Assignment",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Firebase notification inbox alerts"
            }

            val notificationManager =
                context.getSystemService(NotificationManager::class.java)

            notificationManager.createNotificationChannel(channel)
        }
    }
}