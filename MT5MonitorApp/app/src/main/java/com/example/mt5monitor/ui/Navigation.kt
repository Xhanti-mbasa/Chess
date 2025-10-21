package com.example.mt5monitor.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.ViewList
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationItem(val route: String, val icon: ImageVector, val label: String) {
    Dashboard("dashboard", Icons.Rounded.Dashboard, "Dashboard"),
    Positions("positions", Icons.Rounded.ViewList, "Positions"),
    Settings("settings", Icons.Rounded.Settings, "Settings")
}
