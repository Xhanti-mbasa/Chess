package com.example.mt5monitor.domain.model

data class EASettings(
    val lotSize: Double,
    val stopLoss: Double,
    val takeProfit: Double,
    val rsiBuyLevel: Double,
    val rsiSellLevel: Double,
    val movingAveragePeriod: Int,
    val autoTradingEnabled: Boolean
)
