package com.example.mt5monitor.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mt5monitor.domain.model.EASettings

@Composable
fun EASettingsForm(
    settings: EASettings,
    onSettingsChanged: (EASettings) -> Unit,
    onApply: () -> Unit,
    modifier: Modifier = Modifier
) {
    val localSettings = remember(settings) { mutableStateOf(settings) }

    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        NumericField(label = "Lot Size", value = localSettings.value.lotSize) {
            localSettings.value = localSettings.value.copy(lotSize = it)
            onSettingsChanged(localSettings.value)
        }
        NumericField(label = "Stop Loss", value = localSettings.value.stopLoss) {
            localSettings.value = localSettings.value.copy(stopLoss = it)
            onSettingsChanged(localSettings.value)
        }
        NumericField(label = "Take Profit", value = localSettings.value.takeProfit) {
            localSettings.value = localSettings.value.copy(takeProfit = it)
            onSettingsChanged(localSettings.value)
        }
        NumericField(label = "RSI Buy", value = localSettings.value.rsiBuyLevel) {
            localSettings.value = localSettings.value.copy(rsiBuyLevel = it)
            onSettingsChanged(localSettings.value)
        }
        NumericField(label = "RSI Sell", value = localSettings.value.rsiSellLevel) {
            localSettings.value = localSettings.value.copy(rsiSellLevel = it)
            onSettingsChanged(localSettings.value)
        }
        NumericField(label = "MA Period", value = localSettings.value.movingAveragePeriod.toDouble()) {
            localSettings.value = localSettings.value.copy(movingAveragePeriod = it.toInt())
            onSettingsChanged(localSettings.value)
        }
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Auto Trading", modifier = Modifier.weight(1f))
            Switch(
                checked = localSettings.value.autoTradingEnabled,
                onCheckedChange = {
                    localSettings.value = localSettings.value.copy(autoTradingEnabled = it)
                    onSettingsChanged(localSettings.value)
                }
            )
        }
        Button(onClick = onApply, modifier = Modifier.align(Alignment.End)) {
            Text("Apply")
        }
    }
}

@Composable
private fun NumericField(label: String, value: Double, onValueChange: (Double) -> Unit) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        value = value.toString(),
        onValueChange = { newValue ->
            newValue.toDoubleOrNull()?.let(onValueChange)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}
