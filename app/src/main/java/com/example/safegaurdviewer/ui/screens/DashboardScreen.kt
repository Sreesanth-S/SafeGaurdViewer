package com.example.safegaurdviewer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.safegaurdviewer.ui.components.QuickActionButton
import com.example.safegaurdviewer.ui.components.RecentActivityItem
import com.example.safegaurdviewer.ui.components.SecurityStatusCard
import com.example.safegaurdviewer.ui.navigations.Screen

@Composable
fun DashboardScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 80.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "SafeGuard Viewer",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Mobile Security Scanner",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            SecurityStatusCard(
                status = "Protected",
                lastScanTime = "Today at 2:45 PM",
                threatsDetected = 0
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text(
                text = "Quick Actions",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionButton(
                        icon = Icons.Default.InsertDriveFile,
                        title = "Scan File",
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onClick = { navController.navigate(Screen.Scan.route) }
                    )
                    QuickActionButton(
                        icon = Icons.Default.Link,
                        title = "Scan Link",
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onClick = { navController.navigate(Screen.Scan.route) }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionButton(
                        icon = Icons.Default.Apps,
                        title = "Analyze APK",
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onClick = { navController.navigate(Screen.Scan.route) }
                    )
                    QuickActionButton(
                        icon = Icons.Default.VerifiedUser,
                        title = "Secure Viewer",
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onClick = { navController.navigate(Screen.SecureViewer.route) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text(
                text = "Recent Activity",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                RecentActivityItem(
                    fileName = "file.pdf",
                    riskLevel = "Safe",
                    icon = Icons.Default.InsertDriveFile
                )
                RecentActivityItem(
                    fileName = "bank_app.apk",
                    riskLevel = "Suspicious",
                    icon = Icons.Default.Apps
                )
                RecentActivityItem(
                    fileName = "login-link.com",
                    riskLevel = "Malicious",
                    icon = Icons.Default.Link
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}