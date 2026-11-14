package com.example.firebasenotificationinbox.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification")

data class NotificationEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val bannerImageURL: String?,
    val timestamp: Long,
    val receivedAt: Long = System.currentTimeMillis(),
    val isRead: Boolean=false

)
