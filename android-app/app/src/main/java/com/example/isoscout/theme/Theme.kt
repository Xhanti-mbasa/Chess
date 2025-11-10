package com.example.isoscout.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF0057D9),
    onPrimary = Color.White,
    background = Color(0xFFF6F6F6)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFAFC7FF),
    onPrimary = Color(0xFF002B70),
    background = Color(0xFF121212)
)

@Composable
fun IsoScoutTheme(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}
