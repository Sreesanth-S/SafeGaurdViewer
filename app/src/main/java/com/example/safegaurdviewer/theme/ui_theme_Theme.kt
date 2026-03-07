package com.example.safegaurdviewer.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// SafeGuard Colors
val DarkBlue = Color(0xFF0B1F3A)
val SafeGreen = Color(0xFF2ECC71)
val SuspiciousYellow = Color(0xFFF1C40F)
val MaliciousRed = Color(0xFFE74C3C)
val White = Color(0xFFFFFFFF)
val LightGray = Color(0xFFF5F5F5)
val DarkGray = Color(0xFF1A1A1A)
val BorderGray = Color(0xFF2D2D2D)

private val SafeGuardColorScheme = darkColorScheme(
    primary = Color(0xFF1E88E5),
    onPrimary = White,
    primaryContainer = DarkBlue,
    onPrimaryContainer = Color(0xFFE3F2FD),
    secondary = SafeGreen,
    onSecondary = DarkBlue,
    error = MaliciousRed,
    onError = White,
    background = DarkBlue,
    onBackground = White,
    surface = DarkGray,
    onSurface = White,
    surfaceVariant = BorderGray,
    onSurfaceVariant = Color(0xFFB0B0B0)
)

@Composable
fun SafeGuardTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SafeGuardColorScheme,
        typography = SafeGuardTypography,
        content = content
    )
}