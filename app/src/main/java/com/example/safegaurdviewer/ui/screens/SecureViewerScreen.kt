package com.example.safegaurdviewer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.safegaurdviewer.ui.navigations.Screen
import com.example.safegaurdviewer.ui.theme.CyberBorder
import com.example.safegaurdviewer.ui.theme.CyberBlack
import com.example.safegaurdviewer.ui.theme.CyberCyan
import com.example.safegaurdviewer.ui.theme.CyberElevated
import com.example.safegaurdviewer.ui.theme.CyberGreen
import com.example.safegaurdviewer.ui.theme.CyberRed
import com.example.safegaurdviewer.ui.theme.CyberSurface
import com.example.safegaurdviewer.ui.theme.TextPrimary
import com.example.safegaurdviewer.ui.theme.TextSecondary
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.viewinterop.AndroidView
import java.net.URLDecoder



@Composable
fun SecureViewerScreen(navController: NavController,url:String) {
    val decodedUrl = URLDecoder.decode(url, "UTF-8")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBlack)
    ) {
        // Top banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(CyberCyan.copy(alpha = 0.1f), CyberBlack)))
                .border(1.dp, CyberCyan.copy(alpha = 0.25f), RoundedCornerShape(0.dp))
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).background(CyberCyan, CircleShape))
                Spacer(modifier = Modifier.width(10.dp))
                Icon(Icons.Default.VerifiedUser, null, tint = CyberCyan, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "SECURE SANDBOX — External actions disabled",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberCyan,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // File viewer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(CyberBlack)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                        loadUrl(decodedUrl)
                    }
                }
            )

        }

        // Restrictions + actions panel
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(CyberSurface)
                .border(1.dp, CyberBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .padding(20.dp)
        ) {
            // Action buttons
            Row(modifier = Modifier.fillMaxWidth()) {
                SvActionBtn(
                    modifier = Modifier.fillMaxWidth(),
                    bg = CyberElevated,
                    borderColor = CyberBorder,
                    onClick = { navController.navigate(Screen.Scan.route) {
                        popUpTo(Screen.Scan.route) { inclusive = false }
                        launchSingleTop = true
                    }
                    }
                ) {
                    Icon(Icons.Default.Close, null, tint = TextSecondary, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Close", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
private fun SvRow(icon: ImageVector, label: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(color.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                .border(1.dp, color.copy(alpha = 0.25f), RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = color, modifier = Modifier.size(14.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = label, fontSize = 13.sp, color = TextPrimary)
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
    }
}

@Composable
private fun SvActionBtn(
    modifier: Modifier,
    bg: Color,
    borderColor: Color,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}