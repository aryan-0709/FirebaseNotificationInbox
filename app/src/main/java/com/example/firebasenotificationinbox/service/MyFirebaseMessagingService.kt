package com.example.firebasenotificationinbox.service

import android.Manifest
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.firebasenotificationinbox.data.local.NotificationDatabaseProvider
import com.example.firebasenotificationinbox.data.local.NotificationEntity
import com.example.firebasenotificationinbox.data.repository.NotificationRepository
import com.example.firebasenotificationinbox.notification.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseMessagingService: FirebaseMessagingService()

{
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

   @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
   override fun onMessageReceived(remoteMessage: RemoteMessage)
    {
       super.onMessageReceived(remoteMessage)
        Log.d("FCM_TEST", "Message received!")

        val data = remoteMessage.data
        Log.d("FCM_TEST", "Payload: $data")

        val title = data["title"] ?: "New Notification"
        val description = data["description"] ?: "No description available"
        val id = data["id"] ?: return
        val bannerImg = data["bannerImg"]
        val timestamp = data["timestamp"]

        // Create database and repository

        val database = NotificationDatabaseProvider.getDatabase(applicationContext)
        val repository = NotificationRepository(database.notificationDao())

        // Convert FCM payload into Room Entity

        val notification = NotificationEntity(
            id = id,
            title = title,
            description = description,
            bannerImageURL = bannerImg,
            timestamp = timestamp
                ?.toLongOrNull()
                ?.takeIf { it > 0L }
                ?: System.currentTimeMillis(),
            isRead = false
        )

        CoroutineScope(Dispatchers.IO).launch {

            try {
                repository.insertNotification(notification)             // Save notification in background
                Log.d("ROOM_DB", "Notification saved successfully")
            } catch (exception: Exception) {
                Log.e("ROOM_DB", "Failed to save notification", exception)
            }
        }

        NotificationHelper.showNotification(
            context = this,
            id = id,
            title = title,
            description = description
        )
    }
}
