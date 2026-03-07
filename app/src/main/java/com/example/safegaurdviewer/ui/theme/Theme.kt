package com.example.safegaurdviewer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val SafeGuardColorScheme = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF1E88E5),
    onPrimary = White,
    primaryContainer = DarkBlue,
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFFE3F2FD),
    secondary = SafeGreen,
    onSecondary = DarkBlue,
    error = MaliciousRed,
    onError = White,
    background = DarkBlue,
    onBackground = White,
    surface = DarkGray,
    onSurface = White,
    surfaceVariant = BorderGray,
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFFB0B0B0)
)

@Composable
fun SafeGuardTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SafeGuardColorScheme,
        typography = SafeGuardTypography,
        content = content
    )
}