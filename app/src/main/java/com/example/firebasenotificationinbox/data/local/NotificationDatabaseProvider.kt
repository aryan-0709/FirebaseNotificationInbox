package com.example.firebasenotificationinbox.data.local

import android.content.Context
import androidx.room.Room

object NotificationDatabaseProvider {

    @Volatile
    private var INSTANCE: NotificationDatabase? = null

    fun getDatabase(context: Context): NotificationDatabase {

        return INSTANCE ?: synchronized(this) {

            val instance = Room.databaseBuilder(
                context.applicationContext,
                NotificationDatabase::class.java,
                "notification_database"
            ).build()

            INSTANCE = instance
            instance
        }
    }
}