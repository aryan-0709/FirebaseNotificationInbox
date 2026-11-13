package com.example.firebasenotificationinbox.notification

import android.Manifest
import android.app.PendingIntent
import com.example.firebasenotificationinbox.R
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.firebasenotificationinbox.MainActivity
import com.example.firebasenotificationinbox.notification.NotificationChannelManager.CHANNEL_ID
import kotlin.hashCode

object NotificationHelper {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showNotification(
        context: Context,
        id: String,
        title: String,
        description: String)
    {
        val notificaionId= id.hashCode()

val intent= Intent(context, MainActivity::class.java)
        val pendingintent= PendingIntent.getActivity(
            context,
            notificaionId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)



        val notification= NotificationCompat.Builder(context,
           CHANNEL_ID
       )
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingintent)
            .setAutoCancel(true)
            .build()



        NotificationManagerCompat.from(context)
            .notify(notificaionId,notification)
    }

}
