package com.example.safegaurdviewer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val SafeGuardColorScheme = darkColorScheme(
    primary = CyberCyan,
    onPrimary = CyberBlack,
    primaryContainer = CyberElevated,
    onPrimaryContainer = CyberCyan,
    secondary = CyberGreen,
    onSecondary = CyberBlack,
    tertiary = CyberPurple,
    error = CyberRed,
    onError = TextPrimary,
    background = CyberBlack,
    onBackground = TextPrimary,
    surface = CyberSurface,
    onSurface = TextPrimary,
    surfaceVariant = CyberDarkSurface,
    onSurfaceVariant = TextSecondary,
    outline = CyberBorder,
    outlineVariant = CyberBorderBright,
)

@Composable
fun SafeGuardTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SafeGuardColorScheme,
        typography = SafeGuardTypography,
        content = content
    )
}