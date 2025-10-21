package com.example.mt5monitor.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mt5monitor.ui.DashboardState
import com.example.mt5monitor.ui.components.AccountSummaryCard
import com.example.mt5monitor.ui.components.EASettingsForm
import com.example.mt5monitor.ui.components.RsiGauge
import com.example.mt5monitor.ui.components.SignalStatusChip

@Composable
fun DashboardScreen(
    state: DashboardState,
    onToggleAutoTrading: () -> Unit,
    onManualBuy: () -> Unit,
    onManualSell: () -> Unit,
    onCloseAll: () -> Unit,
    onSettingsChanged: (com.example.mt5monitor.domain.model.EASettings) -> Unit,
    onApplySettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(shape = MaterialTheme.shapes.large, tonalElevation = 6.dp) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RsiGauge(value = state.signal.rsiValue)
                Spacer(modifier = Modifier.height(12.dp))
                Text("MA(5): %.5f".format(state.signal.movingAverage), style = MaterialTheme.typography.titleMedium)
                SignalStatusChip(status = state.signal.status, modifier = Modifier.padding(top = 8.dp))
            }
        }

        AccountSummaryCard(account = state.account)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onToggleAutoTrading, modifier = Modifier.weight(1f)) {
                Text(if (state.settings.autoTradingEnabled) "AutoTrading ON" else "AutoTrading OFF")
            }
            Button(onClick = onManualBuy, modifier = Modifier.weight(1f)) {
                Text("BUY")
            }
            Button(onClick = onManualSell, modifier = Modifier.weight(1f)) {
                Text("SELL")
            }
        }

        Button(onClick = onCloseAll, modifier = Modifier.fillMaxWidth()) {
            Text("Close All Positions")
        }

        EASettingsForm(
            settings = state.settings,
            onSettingsChanged = onSettingsChanged,
            onApply = onApplySettings,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
