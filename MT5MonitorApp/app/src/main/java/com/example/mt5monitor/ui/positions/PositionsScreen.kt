package com.example.mt5monitor.ui.positions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mt5monitor.domain.model.Position
import com.example.mt5monitor.domain.model.TradingSignal
import com.example.mt5monitor.ui.PositionsState
import com.example.mt5monitor.ui.components.MiniSignalChart
import com.example.mt5monitor.ui.components.PositionList

@Composable
fun PositionsScreen(
    state: PositionsState,
    onClosePosition: (Position) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Open Positions", style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
        PositionList(
            positions = state.positions,
            onClose = onClosePosition,
            modifier = Modifier.weight(1f)
        )
        MiniSignalChart(points = state.chart, modifier = Modifier.fillMaxWidth().padding(top = 16.dp))
    }
}
