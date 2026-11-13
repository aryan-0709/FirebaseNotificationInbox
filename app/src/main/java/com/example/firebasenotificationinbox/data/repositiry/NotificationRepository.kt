package com.example.firebasenotificationinbox.data.repositiry

import com.example.firebasenotificationinbox.data.local.NotificationDao
import com.example.firebasenotificationinbox.data.local.NotificationEntity
import kotlinx.coroutines.flow.Flow

class NotificationRepository(
private val notificationDao: NotificationDao)
{
    suspend fun insertNotification(notification: NotificationEntity) {
        notificationDao.insertNotification(notification)
    }

    fun getAllNotifications(): Flow<List<NotificationEntity>> {
        return notificationDao.getAllNotifications()
    }

    suspend fun markAsRead(notificationId: String) {
        notificationDao.markAsRead(notificationId)
    }
}