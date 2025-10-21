package com.example.mt5monitor.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mt5monitor.domain.model.NotificationType

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val timestamp: Long,
    val type: NotificationType
)
