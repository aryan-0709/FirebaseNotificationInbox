package com.example.firebasenotificationinbox.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL(
            "ALTER TABLE notification ADD COLUMN receivedAt INTEGER NOT NULL DEFAULT 0"
        )

        database.execSQL(
            "UPDATE notification SET receivedAt = timestamp"
        )
    }
}