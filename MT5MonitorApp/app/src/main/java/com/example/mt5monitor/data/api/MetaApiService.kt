package com.example.mt5monitor.data.api

import com.example.mt5monitor.domain.model.ChartPoint
import com.example.mt5monitor.domain.model.EASettings
import com.example.mt5monitor.domain.model.Position
import com.example.mt5monitor.domain.model.TradingSignal
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MetaApiService {
    @GET("accounts/{accountId}/signal")
    suspend fun fetchTradingSignal(@Path("accountId") accountId: String): TradingSignal

    @GET("accounts/{accountId}/positions")
    suspend fun fetchOpenPositions(@Path("accountId") accountId: String): List<Position>

    @GET("accounts/{accountId}/chart")
    suspend fun fetchChart(@Path("accountId") accountId: String): List<ChartPoint>

    @POST("accounts/{accountId}/settings")
    suspend fun updateSettings(
        @Path("accountId") accountId: String,
        @Body settings: EASettings
    ): EASettings

    @POST("accounts/{accountId}/execute")
    suspend fun executeTrade(
        @Path("accountId") accountId: String,
        @Body request: TradeCommandRequest
    ): TradeCommandResponse
}

data class TradeCommandRequest(
    val command: TradeCommand,
    val symbol: String? = null
)

data class TradeCommandResponse(
    val success: Boolean,
    val message: String
)

enum class TradeCommand { BUY, SELL, CLOSE_ALL, TOGGLE_AUTOTRADING }
