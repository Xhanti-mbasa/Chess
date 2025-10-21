package com.example.mt5monitor.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mt5monitor.domain.model.SignalStatus

@Composable
fun SignalStatusChip(status: SignalStatus, modifier: Modifier = Modifier) {
    val color = when (status) {
        SignalStatus.BUY_READY -> Color(0xFF16FF96)
        SignalStatus.SELL_READY -> Color(0xFFFF5A5F)
        SignalStatus.NEUTRAL -> MaterialTheme.colorScheme.primary
    }
    AssistChip(
        modifier = modifier,
        onClick = {},
        colors = AssistChipDefaults.assistChipColors(containerColor = color.copy(alpha = 0.2f)),
        label = {
            Text(text = status.displayName, color = color)
        }
    )
}
