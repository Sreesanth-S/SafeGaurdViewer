package com.example.safegaurdviewer.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.safegaurdviewer.executor.FileExecutor
import com.example.safegaurdviewer.scanner.FileScanner
import com.example.safegaurdviewer.scanner.LinkScanner
import com.example.safegaurdviewer.ui.components.RiskScoreMeter
import com.example.safegaurdviewer.ui.theme.SuspiciousYellow
import com.example.safegaurdviewer.data.ScanHistoryStore
import com.example.safegaurdviewer.data.ScanResult
import com.example.safegaurdviewer.data.IncomingFileHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ScanCenterScreen(navController: NavController) {

    var selectedTab by remember { mutableStateOf(0) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 80.dp),
        contentPadding = PaddingValues(16.dp)
    ) {

        item {
            Text(
                text = "Scan Center",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            TabRow(selectedTabIndex = selectedTab) {

                Tab(selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("File") })

                Tab(selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Link") })

                Tab(selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("APK") })
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        when (selectedTab) {
            0 -> item { FileScannerCard() }
            1 -> item { LinkScannerCard() }
            2 -> item { APKAnalyzerCard() }
        }
    }
}

@Composable
fun FileScannerCard() {

    var isScanning by remember { mutableStateOf(false) }
    var scanScore by remember { mutableStateOf(0) }
    var hasScanned by remember { mutableStateOf(false) }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val incomingUri = IncomingFileHolder.uri

    LaunchedEffect(incomingUri) {

        if (incomingUri != null) {

            scope.launch {

                isScanning = true

                val result = FileScanner.scanFile(context, incomingUri)

                val score = result.score

                scanScore = score
                hasScanned = true
                isScanning = false

                val riskLevel = when {
                    score <= 30 -> "Safe"
                    score <= 70 -> "Suspicious"
                    else -> "Malicious"
                }

                val formatter =
                    SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())

                ScanHistoryStore.history.add(
                    0,
                    ScanResult(
                        name = incomingUri.lastPathSegment ?: "external file",
                        type = "File",
                        riskLevel = riskLevel,
                        date = formatter.format(Date())
                    )
                )

                if (score < 30) {
                    FileExecutor.openFile(context, incomingUri)
                }
            }

            IncomingFileHolder.uri = null
        }
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->

        if (uri != null) {

            selectedUri = uri
            isScanning = true
            hasScanned = false

            scope.launch {

                try {

                    val result = withContext(Dispatchers.IO) {
                        FileScanner.scanFile(context, uri)
                    }

                    val score = result.score

                    scanScore = score
                    hasScanned = true
                    isScanning = false

                    val riskLevel = when {
                        score <= 30 -> "Safe"
                        score <= 70 -> "Suspicious"
                        else -> "Malicious"
                    }

                    val formatter =
                        SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())

                    ScanHistoryStore.history.add(
                        0,
                        ScanResult(
                            name = selectedUri?.lastPathSegment ?: "file",
                            type = "File",
                            riskLevel = riskLevel,
                            date = formatter.format(Date())
                        )
                    )

                    if (score < 30) {
                        FileExecutor.openFile(context, uri)
                    }

                } catch (e: Exception) {

                    isScanning = false
                    e.printStackTrace()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {

        Text("File Scanner", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { launcher.launch("*/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Folder, contentDescription = "Select File")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Select File")
        }

        if (selectedUri != null) {

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {

                Column {

                    Text(
                        text = selectedUri?.lastPathSegment ?: "Selected File",
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "Analyzing file...",
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isScanning) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (hasScanned && !isScanning) {
            RiskScoreMeter(score = scanScore)
        }
    }
}

@Composable
fun LinkScannerCard() {

    var linkInput by remember { mutableStateOf("") }
    var isScanning by remember { mutableStateOf(false) }
    var scanScore by remember { mutableStateOf(0) }
    var hasScanned by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {

        Text("Link Scanner", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = linkInput,
            onValueChange = { linkInput = it },
            label = { Text("Paste suspicious URL") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Link, null) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {

                if (linkInput.isEmpty()) return@Button

                scope.launch {

                    isScanning = true

                    val score = LinkScanner.scanUrl(linkInput)

                    scanScore = score
                    hasScanned = true
                    isScanning = false
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Scan Link")
        }

        if (hasScanned) {

            Spacer(modifier = Modifier.height(16.dp))

            if (isScanning) {

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

            } else {

                RiskScoreMeter(score = scanScore)

            }
        }
    }
}

@Composable
fun APKAnalyzerCard() {

    var scanScore by remember { mutableStateOf(0) }
    var hasScanned by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {

        Text("APK Analyzer", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->

            if (uri != null) {

                scope.launch {

                    val result = FileScanner.scanFile(context, uri)

                    scanScore = result.score
                    hasScanned = true
                }
            }
        }
        Button(
            onClick = {

                launcher.launch("application/vnd.android.package-archive")

            }
        ) {
            Text("Upload APK")
        }


        if (hasScanned) {

            Spacer(modifier = Modifier.height(16.dp))

            RiskScoreMeter(score = scanScore)
        }
    }
}

@Composable
fun PermissionTag(permission: String) {

    Box(
        modifier = Modifier
            .background(SuspiciousYellow.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
            .padding(6.dp)
    ) {

        Text(
            text = permission,
            fontSize = 10.sp,
            color = SuspiciousYellow
        )
    }
}