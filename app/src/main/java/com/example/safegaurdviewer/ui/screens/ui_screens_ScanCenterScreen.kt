package com.example.safegaurdviewer.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.safeguard.viewer.ui.components.RiskScoreMeter
import com.safeguard.viewer.ui.theme.SuspiciousYellow

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
            // Tab Selection
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
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
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {
        Text(
            text = "File Scanner",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { 
                isScanning = true
                scanScore = (0..100).random()
                hasScanned = true
                isScanning = false
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(Icons.Default.Folder, contentDescription = "Select")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Select File")
        }
        
        if (hasScanned) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "document.pdf",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "2.4 MB • SHA-256: a3f2e1...",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (isScanning) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                RiskScoreMeter(score = scanScore)
            }
        }
    }
}

@Composable
fun LinkScannerCard() {
    var linkInput by remember { mutableStateOf("") }
    var isScanning by remember { mutableStateOf(false) }
    var scanScore by remember { mutableStateOf(0) }
    var hasScanned by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {
        Text(
            text = "Link Scanner",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = linkInput,
            onValueChange = { linkInput = it },
            label = { Text("Paste suspicious URL") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Link, contentDescription = "Link") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Button(
            onClick = { 
                isScanning = true
                scanScore = (0..100).random()
                hasScanned = true
                isScanning = false
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Scan Link")
        }
        
        if (hasScanned && linkInput.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "Domain Info",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = linkInput,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (isScanning) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                RiskScoreMeter(score = scanScore)
            }
        }
    }
}

@Composable
fun APKAnalyzerCard() {
    var hasSelected by remember { mutableStateOf(false) }
    var isScanning by remember { mutableStateOf(false) }
    var scanScore by remember { mutableStateOf(0) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {
        Text(
            text = "APK Analyzer",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { 
                hasSelected = true
                isScanning = true
                scanScore = (25..75).random()
                isScanning = false
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(Icons.Default.Apps, contentDescription = "Upload")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Upload APK")
        }
        
        if (hasSelected) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    APKInfoRow("Package", "com.example.app")
                    APKInfoRow("Version", "2.1.0")
                    APKInfoRow("Developer", "Example Corp")
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Permissions (15)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        PermissionTag("android.permission.INTERNET")
                        PermissionTag("android.permission.READ_CONTACTS")
                        PermissionTag("android.permission.ACCESS_FINE_LOCATION")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (isScanning) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                RiskScoreMeter(score = scanScore)
            }
        }
    }
}

@Composable
fun APKInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun PermissionTag(permission: String) {
    Box(
        modifier = Modifier
            .background(
                color = SuspiciousYellow.copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp)
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