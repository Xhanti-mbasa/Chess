package com.example.mt5monitor.domain.model

data class NotificationEvent(
    val id: Long,
    val title: String,
    val message: String,
    val timestamp: Long,
    val type: NotificationType
)

enum class NotificationType {
    SIGNAL,
    POSITION,
    RISK
}
