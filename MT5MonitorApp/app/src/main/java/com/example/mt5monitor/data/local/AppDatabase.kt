package com.example.mt5monitor.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [NotificationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
}
