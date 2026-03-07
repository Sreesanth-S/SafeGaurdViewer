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
import com.example.safegaurdviewer.ui.components.RiskScoreMeter
import com.example.safegaurdviewer.ui.theme.SuspiciousYellow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            TabRow(selectedTabIndex = selectedTab) {

                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("File") }
                )

                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Link") }
                )

                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("APK") }
                )
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

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->

        if (uri != null) {

            selectedUri = uri
            isScanning = true
            hasScanned = false

            scope.launch {

                try {

                    val score = withContext(Dispatchers.IO) {
                        FileScanner.scanFile(context, uri)
                    }

                    scanScore = score
                    hasScanned = true
                    isScanning = false

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

        Text(
            text = "File Scanner",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

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
                    .background(
                        MaterialTheme.colorScheme.background,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            ) {

                Column {

                    Text(
                        text = selectedUri?.lastPathSegment ?: "Selected File",
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "Analyzing file...",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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
    var scanScore by remember { mutableStateOf(0) }
    var hasScanned by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {

        Text(
            text = "Link Scanner",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = linkInput,
            onValueChange = { linkInput = it },
            label = { Text("Paste suspicious URL") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Default.Link, contentDescription = "Link")
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                scanScore = (0..100).random()
                hasScanned = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Scan Link")
        }

        if (hasScanned) {

            Spacer(modifier = Modifier.height(16.dp))

            RiskScoreMeter(score = scanScore)
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

        Text(
            text = "APK Analyzer",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scanScore = (25..75).random()
                hasScanned = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Icon(Icons.Default.Apps, contentDescription = "Upload APK")
            Spacer(modifier = Modifier.width(8.dp))
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
            .background(
                SuspiciousYellow.copy(alpha = 0.15f),
                RoundedCornerShape(6.dp)
            )
            .padding(6.dp)
    ) {

        Text(
            text = permission,
            fontSize = 10.sp,
            color = SuspiciousYellow
        )
    }
}