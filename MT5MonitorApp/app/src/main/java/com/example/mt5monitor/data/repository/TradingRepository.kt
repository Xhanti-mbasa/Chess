package com.example.mt5monitor.data.repository

import com.example.mt5monitor.data.api.MetaApiService
import com.example.mt5monitor.data.api.TradeCommand
import com.example.mt5monitor.data.api.TradeCommandRequest
import com.example.mt5monitor.domain.model.AccountOverview
import com.example.mt5monitor.domain.model.ChartPoint
import com.example.mt5monitor.domain.model.EASettings
import com.example.mt5monitor.domain.model.NotificationEvent
import com.example.mt5monitor.domain.model.NotificationType
import com.example.mt5monitor.domain.model.Position
import com.example.mt5monitor.domain.model.TradingSignal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

interface TradingRepository {
    val signal: Flow<TradingSignal>
    val positions: Flow<List<Position>>
    val notifications: Flow<List<NotificationEvent>>
    val chart: Flow<List<ChartPoint>>
    val account: Flow<AccountOverview>
    val settings: Flow<EASettings>

    suspend fun refreshAll()
    suspend fun toggleAutoTrading()
    suspend fun executeBuy(symbol: String)
    suspend fun executeSell(symbol: String)
    suspend fun closeAllPositions()
    suspend fun updateSettings(settings: EASettings)
}

@Singleton
class TradingRepositoryImpl @Inject constructor(
    private val apiService: MetaApiService
) : TradingRepository {

    private val signalState = MutableStateFlow(
        TradingSignal(status = com.example.mt5monitor.domain.model.SignalStatus.NEUTRAL, rsiValue = 50.0, movingAverage = 0.0)
    )
    private val positionsState = MutableStateFlow<List<Position>>(emptyList())
    private val notificationsState = MutableStateFlow<List<NotificationEvent>>(emptyList())
    private val chartState = MutableStateFlow<List<ChartPoint>>(emptyList())
    private val accountState = MutableStateFlow(AccountOverview(0.0, 0.0, 0.0, "USD"))
    private val settingsState = MutableStateFlow(
        EASettings(
            lotSize = 0.1,
            stopLoss = 20.0,
            takeProfit = 40.0,
            rsiBuyLevel = 10.0,
            rsiSellLevel = 91.0,
            movingAveragePeriod = 5,
            autoTradingEnabled = false
        )
    )

    override val signal: Flow<TradingSignal> = signalState
    override val positions: Flow<List<Position>> = positionsState
    override val notifications: Flow<List<NotificationEvent>> = notificationsState
    override val chart: Flow<List<ChartPoint>> = chartState
    override val account: Flow<AccountOverview> = accountState
    override val settings: Flow<EASettings> = settingsState

    override suspend fun refreshAll() {
        val accountId = currentAccountId()
        runCatching { apiService.fetchTradingSignal(accountId) }
            .onSuccess { signalState.value = it }
        runCatching { apiService.fetchOpenPositions(accountId) }
            .onSuccess { positionsState.value = it }
        runCatching { apiService.fetchChart(accountId) }
            .onSuccess { chartState.value = it }
    }

    override suspend fun toggleAutoTrading() {
        val updated = settingsState.value.copy(autoTradingEnabled = !settingsState.value.autoTradingEnabled)
        updateSettings(updated)
        sendNotification("Auto Trading", "Auto trading ${if (updated.autoTradingEnabled) "enabled" else "disabled"}", NotificationType.RISK)
    }

    override suspend fun executeBuy(symbol: String) {
        executeTrade(TradeCommand.BUY, symbol)
    }

    override suspend fun executeSell(symbol: String) {
        executeTrade(TradeCommand.SELL, symbol)
    }

    override suspend fun closeAllPositions() {
        executeTrade(TradeCommand.CLOSE_ALL, null)
    }

    override suspend fun updateSettings(settings: EASettings) {
        val accountId = currentAccountId()
        runCatching { apiService.updateSettings(accountId, settings) }
            .onSuccess { settingsState.value = it }
            .onFailure { settingsState.value = settings }
    }

    private suspend fun executeTrade(command: TradeCommand, symbol: String?) {
        val accountId = currentAccountId()
        val response = runCatching {
            apiService.executeTrade(accountId, TradeCommandRequest(command, symbol))
        }
        val notification = response.fold(
            onSuccess = {
                NotificationEvent(
                    id = System.currentTimeMillis(),
                    title = "Trade Executed",
                    message = it.message,
                    timestamp = System.currentTimeMillis(),
                    type = command.toNotificationType()
                )
            },
            onFailure = {
                NotificationEvent(
                    id = System.currentTimeMillis(),
                    title = "Trade Failed",
                    message = it.localizedMessage ?: "Unknown error",
                    timestamp = System.currentTimeMillis(),
                    type = NotificationType.RISK
                )
            }
        )
        notificationsState.value = notificationsState.value + notification
    }

    private fun sendNotification(title: String, message: String, type: NotificationType) {
        val notification = NotificationEvent(
            id = System.currentTimeMillis(),
            title = title,
            message = message,
            timestamp = System.currentTimeMillis(),
            type = type
        )
        notificationsState.value = notificationsState.value + notification
    }

    private fun currentAccountId(): String = "demo-account"
}

private fun TradeCommand.toNotificationType(): NotificationType = when (this) {
    TradeCommand.BUY, TradeCommand.SELL -> NotificationType.SIGNAL
    TradeCommand.CLOSE_ALL -> NotificationType.POSITION
    TradeCommand.TOGGLE_AUTOTRADING -> NotificationType.RISK
}
