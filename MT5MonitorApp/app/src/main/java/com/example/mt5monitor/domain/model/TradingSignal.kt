package com.example.mt5monitor.domain.model

data class TradingSignal(
    val status: SignalStatus,
    val rsiValue: Double,
    val movingAverage: Double,
    val timestamp: Long = System.currentTimeMillis()
)

enum class SignalStatus(val displayName: String) {
    BUY_READY("BUY READY"),
    SELL_READY("SELL READY"),
    NEUTRAL("NEUTRAL")
}
