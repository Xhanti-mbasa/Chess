package com.example.mt5monitor.ui.settings

import androidx.biometric.BiometricManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mt5monitor.domain.model.NotificationEvent
import com.example.mt5monitor.ui.components.NotificationList

@Composable
fun SettingsScreen(
    notifications: List<NotificationEvent>,
    onBiometricToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val biometricManager = BiometricManager.from(context)
    val biometricStatus = remember { mutableStateOf(biometricManager.canAuthenticate()) }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Security", style = MaterialTheme.typography.titleLarge)
        Text("Biometric status: ${statusToText(biometricStatus.value)}", modifier = Modifier.padding(top = 8.dp))
        Button(onClick = onBiometricToggle, modifier = Modifier.padding(top = 12.dp)) {
            Text("Configure Biometrics")
        }
        Text("Notifications", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 24.dp))
        NotificationList(events = notifications, modifier = Modifier.weight(1f))
    }
}

private fun BiometricManager.canAuthenticate(): Int = this.canAuthenticate(
    BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
)

private fun statusToText(status: Int): String = when (status) {
    BiometricManager.BIOMETRIC_SUCCESS -> "Ready"
    BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> "Not enrolled"
    BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> "No hardware"
    else -> "Unavailable"
}
