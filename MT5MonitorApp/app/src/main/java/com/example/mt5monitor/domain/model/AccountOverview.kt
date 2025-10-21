package com.example.mt5monitor.domain.model

data class AccountOverview(
    val balance: Double,
    val equity: Double,
    val profit: Double,
    val currency: String
)
