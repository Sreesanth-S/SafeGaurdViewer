package com.example.safegaurdviewer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.safegaurdviewer.ui.components.RiskScoreMeter
import com.example.safegaurdviewer.ui.theme.MaliciousRed
import com.example.safegaurdviewer.ui.theme.SuspiciousYellow

@Composable
fun ThreatDetailsScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 80.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Threat Analysis",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.InsertDriveFile,
                        contentDescription = "File",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "bank_app.apk", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text(text = "Analyzed on Mar 7, 2026", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                RiskScoreMeter(score = 68)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(text = "Engine Detection Ratio", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .background(color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(12.dp))
                            .padding(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(0.05f)
                                .background(color = MaliciousRed, shape = RoundedCornerShape(10.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "3 out of 70 security engines flagged this as malicious",
                        fontSize = 13.sp,
                        color = SuspiciousYellow,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Text(text = "Threat Reasons", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ThreatReasonItem(icon = Icons.Default.Warning, reason = "Suspicious permissions", color = SuspiciousYellow)
                ThreatReasonItem(icon = Icons.Default.Gavel, reason = "Malicious domain reputation", color = MaliciousRed)
                ThreatReasonItem(icon = Icons.Default.Link, reason = "Embedded external links", color = SuspiciousYellow)
                ThreatReasonItem(icon = Icons.Default.NoAccounts, reason = "Suspicious user behavior pattern", color = SuspiciousYellow)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaliciousRed.copy(alpha = 0.1f), shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Dangerous, contentDescription = "Warning", tint = MaliciousRed, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Recommended Actions", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MaliciousRed)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "• Do not install or open this file\n• Delete from device immediately\n• Run full system scan",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ThreatReasonItem(icon: ImageVector, reason: String, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = reason, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = reason, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
        }
    }
}