package com.example.mt5monitor.domain.model

data class Position(
    val ticket: Long,
    val symbol: String,
    val type: PositionType,
    val entryPrice: Double,
    val currentPrice: Double,
    val profit: Double,
    val volume: Double,
    val openTime: Long
)

enum class PositionType { BUY, SELL }
