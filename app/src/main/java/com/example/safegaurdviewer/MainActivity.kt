package com.example.safegaurdviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.safegaurdviewer.ui.screens.DashboardScreen
import com.example.safegaurdviewer.ui.screens.ScanCenterScreen
import com.example.safegaurdviewer.ui.screens.ScanHistoryScreen
import com.example.safegaurdviewer.ui.screens.SecureViewerScreen
import com.example.safegaurdviewer.ui.screens.ThreatDetailsScreen
import com.example.safegaurdviewer.ui.navigations.BottomNavigation
import com.example.safegaurdviewer.ui.navigations.Screen
import com.example.safegaurdviewer.ui.theme.SafeGuardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SafeGuardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SafeGuardApp()
                }
            }
        }
    }
}

@Composable
fun SafeGuardApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigation(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(navController)
            }
            composable(Screen.Scan.route) {
                ScanCenterScreen(navController)
            }
            composable(Screen.History.route) {
                ScanHistoryScreen(navController)
            }
            composable(Screen.Settings.route) {
                Surface(modifier = Modifier.fillMaxSize()) { }
            }
            composable(Screen.SecureViewer.route) {
                SecureViewerScreen(navController)
            }
            composable(Screen.ThreatDetails.route) {
                ThreatDetailsScreen(navController)
            }
        }
    }
}