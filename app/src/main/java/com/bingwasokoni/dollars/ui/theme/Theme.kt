package com.bingwasokoni.dollars.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = NeonGreen,
    secondary = WarningOrange,
    tertiary = DangerRed,
    background = DarkBackground,
    surface = CardBackground,
    onPrimary = DarkBackground,
    onSecondary = DarkBackground,
    onTertiary = LightText,
    onBackground = LightText,
    onSurface = LightText,
)

@Composable
fun DollarsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
