package com.example.safegaurdviewer.ui.navigations

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Scan : Screen("scan")
    object History : Screen("history")
    object Settings : Screen("settings")
    object SecureViewer : Screen("secure_viewer")
    object ThreatDetails : Screen("threat_details")
}