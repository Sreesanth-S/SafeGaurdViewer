package com.example.safegaurdviewer.ui.navigations

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.safegaurdviewer.ui.theme.*

@Composable
fun BottomNavigation(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = CyberDarkSurface,
        contentColor = TextSecondary,
        tonalElevation = 0.dp,
        modifier = Modifier.border(
            width = 1.dp,
            color = CyberBorder,
            shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)
        )
    ) {
        val navItems = listOf(
            Triple(Screen.Dashboard.route, Icons.Default.Dashboard, "Dashboard"),
            Triple(Screen.Scan.route, Icons.Default.Security, "Scan"),
            Triple(Screen.History.route, Icons.Default.History, "History"),
        )

        navItems.forEach { (route, icon, label) ->
            val isSelected = currentRoute == route
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                },
                selected = isSelected,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = route == Screen.Dashboard.route }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = CyberCyan,
                    selectedTextColor = CyberCyan,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextMuted,
                    indicatorColor = CyberCyan.copy(alpha = 0.12f)
                )
            )
        }
    }
}