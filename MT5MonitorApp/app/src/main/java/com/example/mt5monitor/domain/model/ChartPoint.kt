package com.example.mt5monitor.domain.model

data class ChartPoint(
    val timestamp: Long,
    val price: Double,
    val rsi: Double,
    val ma: Double,
    val signalStatus: SignalStatus
)
