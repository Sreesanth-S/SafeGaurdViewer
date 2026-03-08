package com.example.safegaurdviewer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.safegaurdviewer.data.ScanHistoryStore
import com.example.safegaurdviewer.ui.navigations.Screen
import com.example.safegaurdviewer.ui.theme.*

@Composable
fun DashboardScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBlack),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 100.dp)
    ) {

        // ── Header ──────────────────────────────────────────────
        item {
            DashboardHeader()
            Spacer(modifier = Modifier.height(28.dp))
        }

        // ── Threat Status Bar ────────────────────────────────────
        item {
            ThreatStatusBar()
            Spacer(modifier = Modifier.height(28.dp))
        }

        // ── Quick Actions ────────────────────────────────────────
        item {
            SectionLabel(text = "QUICK ACTIONS")
            Spacer(modifier = Modifier.height(12.dp))
            QuickActionsGrid(navController)
            Spacer(modifier = Modifier.height(28.dp))
        }

        // ── Recent Activity ──────────────────────────────────────
        item {
            SectionLabel(text = "RECENT ACTIVITY")
            Spacer(modifier = Modifier.height(12.dp))

            val recentItems = ScanHistoryStore.history.take(3)

            if (recentItems.isEmpty()) {
                EmptyActivityCard()
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    recentItems.forEach { result ->
                        CyberActivityItem(
                            fileName = result.name,
                            riskLevel = result.riskLevel,
                            date = result.date,
                            type = result.type
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Text(
                text = "SAFEGUARD",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                color = CyberCyan,
                letterSpacing = 3.sp
            )
            Text(
                text = "VIEWER",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                color = TextPrimary,
                letterSpacing = 3.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Mobile Security Scanner",
                fontSize = 12.sp,
                color = TextSecondary,
                letterSpacing = 1.sp
            )
        }

        // Shield icon badge
        Box(
            modifier = Modifier
                .size(52.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(CyberCyan.copy(alpha = 0.2f), Color.Transparent)
                    ),
                    shape = RoundedCornerShape(14.dp)
                )
                .border(1.dp, CyberCyan.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = "Shield",
                tint = CyberCyan,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun ThreatStatusBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CyberSurface)
            .border(1.dp, CyberGreen.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Pulsing dot
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(CyberGreen, androidx.compose.foundation.shape.CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "ALL SYSTEMS SECURE",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberGreen,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Last scan: Today at 2:45 PM",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "0",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberGreen,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
                Text(
                    text = "THREATS",
                    fontSize = 9.sp,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(14.dp)
                .background(CyberCyan, RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            letterSpacing = 2.sp,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
        )
    }
}

@Composable
private fun QuickActionsGrid(navController: NavController) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CyberActionButton(
                icon = Icons.Default.InsertDriveFile,
                title = "Scan File",
                subtitle = "Files & docs",
                accentColor = CyberCyan,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Screen.Scan.route) }
            )
            CyberActionButton(
                icon = Icons.Default.Link,
                title = "Scan Link",
                subtitle = "URLs & links",
                accentColor = CyberPurple,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Screen.Scan.route) }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CyberActionButton(
                icon = Icons.Default.Apps,
                title = "Analyze APK",
                subtitle = "Android apps",
                accentColor = CyberYellow,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Screen.Scan.route) }
            )
            CyberActionButton(
                icon = Icons.Default.VerifiedUser,
                title = "Secure View",
                subtitle = "Sandboxed",
                accentColor = CyberGreen,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Screen.SecureViewer.route) }
            )
        }
    }
}

@Composable
private fun CyberActionButton(
    icon: ImageVector,
    title: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(CyberSurface)
            .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(accentColor.copy(alpha = 0.12f), RoundedCornerShape(10.dp))
                    .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun CyberActivityItem(
    fileName: String,
    riskLevel: String,
    date: String,
    type: String
) {
    val (accent, label) = when (riskLevel.lowercase()) {
        "safe" -> Pair(CyberGreen, "SAFE")
        "suspicious" -> Pair(CyberYellow, "SUSPICIOUS")
        "malicious" -> Pair(CyberRed, "MALICIOUS")
        else -> Pair(TextSecondary, riskLevel.uppercase())
    }

    val typeIcon = when (type) {
        "APK" -> Icons.Default.Apps
        "Link" -> Icons.Default.Link
        else -> Icons.Default.InsertDriveFile
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CyberSurface)
            .border(1.dp, CyberBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = typeIcon,
                    contentDescription = null,
                    tint = CyberCyan,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = fileName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = date,
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .background(accent.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                    .border(1.dp, accent.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = accent,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
private fun EmptyActivityCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CyberSurface)
            .border(1.dp, CyberBorder, RoundedCornerShape(12.dp))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "No scans yet",
                fontSize = 13.sp,
                color = TextSecondary
            )
        }
    }
}