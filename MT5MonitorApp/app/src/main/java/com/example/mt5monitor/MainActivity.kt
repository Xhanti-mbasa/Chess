package com.example.mt5monitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mt5monitor.ui.MainViewModel
import com.example.mt5monitor.ui.NavigationItem
import com.example.mt5monitor.ui.dashboard.DashboardScreen
import com.example.mt5monitor.ui.positions.PositionsScreen
import com.example.mt5monitor.ui.settings.SettingsScreen
import com.example.mt5monitor.ui.theme.MT5MonitorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MT5MonitorTheme {
                val navController = rememberNavController()
                val viewModel: MainViewModel = hiltViewModel()
                val dashboardState = viewModel.dashboardState.collectAsState()
                val positionsState = viewModel.positionsState.collectAsState()
                val notifications = viewModel.notifications.collectAsState(initial = emptyList())

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val backStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = backStackEntry?.destination?.route
                            NavigationItem.values().forEach { item ->
                                NavigationBarItem(
                                    selected = currentRoute == item.route,
                                    onClick = {
                                        if (currentRoute != item.route) {
                                            navController.navigate(item.route) {
                                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                    icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                                    label = { Text(item.label) }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(navController = navController, startDestination = NavigationItem.Dashboard.route) {
                            composable(NavigationItem.Dashboard.route) {
                                DashboardScreen(
                                    state = dashboardState.value,
                                    onToggleAutoTrading = viewModel::toggleAutoTrading,
                                    onManualBuy = { viewModel.buy("EURUSD") },
                                    onManualSell = { viewModel.sell("EURUSD") },
                                    onCloseAll = viewModel::closeAll,
                                    onSettingsChanged = viewModel::updateSettings,
                                    onApplySettings = viewModel::refresh
                                )
                            }
                            composable(NavigationItem.Positions.route) {
                                PositionsScreen(
                                    state = positionsState.value,
                                    onClosePosition = { viewModel.closeAll() }
                                )
                            }
                            composable(NavigationItem.Settings.route) {
                                SettingsScreen(
                                    notifications = notifications.value,
                                    onBiometricToggle = {}
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
