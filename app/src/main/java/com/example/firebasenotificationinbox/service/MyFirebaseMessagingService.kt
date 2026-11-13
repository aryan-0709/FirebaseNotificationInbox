package com.example.firebasenotificationinbox.service

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.firebasenotificationinbox.notification.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService()

{
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("Fcm_token",token)

    }

   @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
   override fun onMessageReceived(remoteMessage: RemoteMessage)
    {
       super.onMessageReceived(remoteMessage)
        Log.d("FCM_TEST", "Message received!")

        val data = remoteMessage.data

        Log.d("FCM_TEST", "Payload: $data")



        val title= data["title"]?:"New Notification"
        val description =data["description"]?:"No description available"
        val id= data["id"]?:return
        val bannerImg= data["bannerImg"]
        val timestamp=data["timestamp"]?:System.currentTimeMillis().toString()

        NotificationHelper.showNotification(
            context = this,
            id = id,
            title = title,
            description = description
        )
    }
}