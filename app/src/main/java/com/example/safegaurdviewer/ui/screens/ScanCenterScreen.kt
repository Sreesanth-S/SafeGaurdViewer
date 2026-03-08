package com.example.safegaurdviewer.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.safegaurdviewer.data.IncomingFileHolder
import com.example.safegaurdviewer.data.ScanHistoryStore
import com.example.safegaurdviewer.data.ScanResult
import com.example.safegaurdviewer.executor.FileExecutor
import com.example.safegaurdviewer.scanner.FileScanner
import com.example.safegaurdviewer.scanner.LinkScanner
import com.example.safegaurdviewer.ui.components.RiskScoreMeter
import com.example.safegaurdviewer.ui.theme.CyberBorder
import com.example.safegaurdviewer.ui.theme.CyberBlack
import com.example.safegaurdviewer.ui.theme.CyberCyan
import com.example.safegaurdviewer.ui.theme.CyberElevated
import com.example.safegaurdviewer.ui.theme.CyberPurple
import com.example.safegaurdviewer.ui.theme.CyberSurface
import com.example.safegaurdviewer.ui.theme.CyberYellow
import com.example.safegaurdviewer.ui.theme.TextMuted
import com.example.safegaurdviewer.ui.theme.TextPrimary
import com.example.safegaurdviewer.ui.theme.TextSecondary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.net.URLEncoder


// ── file-private helpers ──────────────────────────────────────────────────────

private val scanMono: FontFamily = FontFamily.Monospace

private fun toRiskLabel(score: Int): String = when {
    score <= 30 -> "Safe"
    score <= 70 -> "Suspicious"
    else        -> "Malicious"
}

// kept strictly private so no name leaks to other files
private data class Tab(val label: String, val icon: ImageVector)

private val TAB_LIST = listOf(
    Tab("FILE", Icons.Default.InsertDriveFile),
    Tab("LINK", Icons.Default.Link),
    Tab("APK",  Icons.Default.Apps)
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun ScanCenterScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBlack),
        contentPadding = PaddingValues(
            start = 20.dp, end = 20.dp, top = 24.dp, bottom = 100.dp
        )
    ) {
        item {
            Text(
                text = "SCAN CENTER",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                fontFamily = scanMono,
                letterSpacing = 3.sp
            )
            Text(text = "Analyze files, links & APKs", fontSize = 13.sp, color = TextSecondary)
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            ScanTabBar(selectedTab = selectedTab, onTabSelected = { selectedTab = it })
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            when (selectedTab) {
                0    -> FileCard()
                1    -> LinkCard(navController)
                else -> ApkCard()
            }
        }
    }
}

// ── Tab bar ───────────────────────────────────────────────────────────────────

@Composable
private fun ScanTabBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CyberSurface)
            .border(1.dp, CyberBorder, RoundedCornerShape(12.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TAB_LIST.forEachIndexed { index, tab ->
            val sel   = selectedTab == index
            val bg    = if (sel) CyberCyan.copy(alpha = 0.15f) else Color.Transparent
            val bdr   = if (sel) CyberCyan.copy(alpha = 0.5f)  else Color.Transparent
            val tint  = if (sel) CyberCyan else TextSecondary
            val wt    = if (sel) FontWeight.Bold else FontWeight.Normal

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(bg)
                    .border(1.dp, bdr, RoundedCornerShape(10.dp))
                    .clickable { onTabSelected(index) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(tab.icon, contentDescription = tab.label, tint = tint, modifier = Modifier.size(15.dp))
                    Text(tab.label, fontSize = 11.sp, fontWeight = wt, color = tint, fontFamily = scanMono, letterSpacing = 1.sp)
                }
            }
        }
    }
}

// ── File card ─────────────────────────────────────────────────────────────────

@Composable
private fun FileCard() {
    var isScanning       by remember { mutableStateOf(false) }
    var scanScore        by remember { mutableStateOf(0) }
    var hasScanned       by remember { mutableStateOf(false) }
    var selectedFileName by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope   = rememberCoroutineScope()
    val incomingUri = IncomingFileHolder.uri

    LaunchedEffect(incomingUri) {
        if (incomingUri != null) {
            isScanning = true
            val result = FileScanner.scanFile(context, incomingUri)
            scanScore  = result.score
            hasScanned = true
            isScanning = false
            val fmt = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
            ScanHistoryStore.history.add(0, ScanResult(
                name = incomingUri.lastPathSegment ?: "external file",
                type = "File", riskLevel = toRiskLabel(result.score), date = fmt.format(Date())
            ))
            if (result.score < 30) FileExecutor.openFile(context, incomingUri)
            IncomingFileHolder.uri = null
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            selectedFileName = uri.lastPathSegment ?: "file"
            isScanning = true; hasScanned = false
            scope.launch {
                try {
                    val result = withContext(Dispatchers.IO) { FileScanner.scanFile(context, uri) }
                    scanScore  = result.score
                    hasScanned = true
                    isScanning = false
                    val fmt = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
                    ScanHistoryStore.history.add(0, ScanResult(
                        name = selectedFileName, type = "File",
                        riskLevel = toRiskLabel(result.score), date = fmt.format(Date())
                    ))
                    if (result.score < 30) FileExecutor.openFile(context, uri)
                } catch (e: Exception) { isScanning = false; e.printStackTrace() }
            }
        }
    }

    ScanShell(title = "FILE SCANNER", icon = Icons.Default.InsertDriveFile, accent = CyberCyan) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Brush.horizontalGradient(listOf(CyberCyan.copy(0.2f), CyberPurple.copy(0.1f))))
                .border(1.dp, CyberCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .clickable { launcher.launch("*/*") }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Icon(Icons.Default.FolderOpen, null, tint = CyberCyan, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("SELECT FILE", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CyberCyan, fontFamily = scanMono, letterSpacing = 1.sp)
            }
        }

        if (selectedFileName.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(CyberElevated).padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.InsertDriveFile, null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text(selectedFileName, fontSize = 12.sp, color = TextSecondary, fontFamily = scanMono)
            }
        }

        if (isScanning) { Spacer(Modifier.height(20.dp)); ScanSpinner(CyberCyan) }
        if (hasScanned && !isScanning) { Spacer(Modifier.height(20.dp)); RiskScoreMeter(score = scanScore) }
    }
}

// ── Link card ─────────────────────────────────────────────────────────────────

@Composable
private fun LinkCard(navController: NavController) {
    var linkInput  by remember { mutableStateOf("") }
    var isScanning by remember { mutableStateOf(false) }
    var scanScore  by remember { mutableStateOf(0) }
    var hasScanned by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    ScanShell(title = "LINK SCANNER", icon = Icons.Default.Link, accent = CyberPurple) {
        OutlinedTextField(
            value = linkInput,
            onValueChange = { linkInput = it },
            placeholder = { Text("https://example.com", fontSize = 13.sp, color = TextMuted, fontFamily = scanMono) },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Link, null, tint = CyberPurple, modifier = Modifier.size(18.dp)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CyberPurple, unfocusedBorderColor = CyberBorder,
                focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary,
                cursorColor = CyberPurple,
                focusedContainerColor = CyberElevated, unfocusedContainerColor = CyberElevated
            ),
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
        )
        Spacer(Modifier.height(12.dp))

        val empty = linkInput.isEmpty()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(if (empty) CyberElevated else CyberPurple.copy(alpha = 0.2f))
                .border(1.dp, if (empty) CyberBorder else CyberPurple.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .clickable {
                    if (!empty) {
                        val target = linkInput
                        scope.launch {
                            isScanning = true
                            val score  = LinkScanner.scanUrl(target)
                            scanScore  = score; hasScanned = true; isScanning = false
                            if (score < 30) {
                                val encodedUrl = URLEncoder.encode(target, "UTF-8")
                                navController.navigate("secure_viewer/$encodedUrl")
                            }

                            val fmt = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
                            ScanHistoryStore.history.add(0, ScanResult(
                                name = target, type = "Link",
                                riskLevel = toRiskLabel(score), date = fmt.format(Date())
                            ))
                        }
                    }
                }
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("SCAN LINK", fontSize = 13.sp, fontWeight = FontWeight.Bold,
                color = if (empty) TextMuted else CyberPurple, fontFamily = scanMono, letterSpacing = 1.sp)
        }

        if (isScanning) { Spacer(Modifier.height(20.dp)); ScanSpinner(CyberPurple) }
        if (hasScanned && !isScanning) { Spacer(Modifier.height(20.dp)); RiskScoreMeter(score = scanScore) }
    }
}

// ── APK card ──────────────────────────────────────────────────────────────────

@Composable
private fun ApkCard() {
    var scanScore  by remember { mutableStateOf(0) }
    var hasScanned by remember { mutableStateOf(false) }
    var isScanning by remember { mutableStateOf(false) }
    var apkName    by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope   = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            apkName = uri.lastPathSegment ?: "app.apk"; isScanning = true
            scope.launch {
                val result = FileScanner.scanFile(context, uri)
                scanScore = result.score; hasScanned = true; isScanning = false
                val fmt = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
                ScanHistoryStore.history.add(0, ScanResult(
                    name = apkName, type = "APK",
                    riskLevel = toRiskLabel(result.score), date = fmt.format(Date())
                ))
            }
        }
    }

    ScanShell(title = "APK ANALYZER", icon = Icons.Default.Apps, accent = CyberYellow) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(CyberYellow.copy(alpha = 0.08f))
                .border(1.dp, CyberYellow.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Warning, null, tint = CyberYellow, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(8.dp))
            Text("APK files can contain malware. Always scan before installing.", fontSize = 11.sp, color = CyberYellow.copy(alpha = 0.9f))
        }
        Spacer(Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(CyberYellow.copy(alpha = 0.12f))
                .border(1.dp, CyberYellow.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .clickable { launcher.launch("application/vnd.android.package-archive") }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Icon(Icons.Default.Upload, null, tint = CyberYellow, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("UPLOAD APK", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CyberYellow, fontFamily = scanMono, letterSpacing = 1.sp)
            }
        }

        if (apkName.isNotEmpty()) {
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(CyberElevated).padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Apps, null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text(apkName, fontSize = 12.sp, color = TextSecondary, fontFamily = scanMono)
            }
        }

        if (isScanning) { Spacer(Modifier.height(20.dp)); ScanSpinner(CyberYellow) }
        if (hasScanned && !isScanning) { Spacer(Modifier.height(20.dp)); RiskScoreMeter(score = scanScore) }
    }
}

// ── Shared private composables ────────────────────────────────────────────────

@Composable
private fun ScanShell(
    title: String,
    icon: ImageVector,
    accent: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CyberSurface)
            .border(1.dp, accent.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(accent.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                    .border(1.dp, accent.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = accent, modifier = Modifier.size(16.dp))
            }
            Spacer(Modifier.width(10.dp))
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = accent, fontFamily = scanMono, letterSpacing = 1.sp)
        }
        Spacer(Modifier.height(18.dp))
        content()
    }
}

@Composable
private fun ScanSpinner(color: Color) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(color = color, modifier = Modifier.size(36.dp), strokeWidth = 2.dp)
        Spacer(Modifier.height(10.dp))
        Text("SCANNING...", fontSize = 11.sp, color = color, fontFamily = scanMono, letterSpacing = 2.sp)
    }
}