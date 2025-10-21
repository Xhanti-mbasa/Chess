package com.example.mt5monitor.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mt5monitor.data.repository.TradingRepository
import com.example.mt5monitor.domain.model.EASettings
import com.example.mt5monitor.domain.model.Position
import com.example.mt5monitor.domain.model.SignalStatus
import com.example.mt5monitor.domain.model.TradingSignal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TradingRepository
) : ViewModel() {

    val dashboardState: StateFlow<DashboardState> = combine(
        repository.signal,
        repository.account,
        repository.settings
    ) { signal, account, settings ->
        DashboardState(
            signal = signal,
            account = account,
            settings = settings
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardState())

    val positionsState: StateFlow<PositionsState> = combine(
        repository.positions,
        repository.chart
    ) { positions, chart ->
        PositionsState(positions = positions, chart = chart)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PositionsState())

    val notifications = repository.notifications

    fun refresh() {
        viewModelScope.launch { repository.refreshAll() }
    }

    fun toggleAutoTrading() {
        viewModelScope.launch { repository.toggleAutoTrading() }
    }

    fun buy(symbol: String) {
        viewModelScope.launch { repository.executeBuy(symbol) }
    }

    fun sell(symbol: String) {
        viewModelScope.launch { repository.executeSell(symbol) }
    }

    fun closeAll() {
        viewModelScope.launch { repository.closeAllPositions() }
    }

    fun updateSettings(settings: EASettings) {
        viewModelScope.launch { repository.updateSettings(settings) }
    }
}

data class DashboardState(
    val signal: TradingSignal = TradingSignal(SignalStatus.NEUTRAL, 50.0, 0.0),
    val account: com.example.mt5monitor.domain.model.AccountOverview = com.example.mt5monitor.domain.model.AccountOverview(0.0, 0.0, 0.0, "USD"),
    val settings: EASettings = EASettings(0.1, 20.0, 40.0, 10.0, 91.0, 5, false)
)

data class PositionsState(
    val positions: List<Position> = emptyList(),
    val chart: List<com.example.mt5monitor.domain.model.ChartPoint> = emptyList()
)
