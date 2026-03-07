package com.example.safegaurdviewer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.safeguard.viewer.ui.theme.SafeGreen
import com.safeguard.viewer.ui.theme.SuspiciousYellow
import com.safeguard.viewer.ui.theme.MaliciousRed

@Composable
fun RiskScoreMeter(
    score: Int,
    modifier: Modifier = Modifier
) {
    val riskColor = when {
        score <= 30 -> SafeGreen
        score <= 70 -> SuspiciousYellow
        else -> MaliciousRed
    }
    
    val riskLevel = when {
        score <= 30 -> "Safe"
        score <= 70 -> "Suspicious"
        else -> "Malicious"
    }
    
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Circular Risk Score Display
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    color = riskColor.copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = score.toString(),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = riskColor
                )
                Text(
                    text = "/ 100",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Risk Level Badge
        Box(
            modifier = Modifier
                .background(
                    color = riskColor.copy(alpha = 0.2f),
                    shape = MaterialTheme.shapes.small
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = riskLevel,
                color = riskColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun RiskIndicatorBadge(
    riskLevel: String,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = when (riskLevel.lowercase()) {
        "safe" -> Pair(SafeGreen.copy(alpha = 0.15f), SafeGreen)
        "suspicious" -> Pair(SuspiciousYellow.copy(alpha = 0.15f), SuspiciousYellow)
        "malicious" -> Pair(MaliciousRed.copy(alpha = 0.15f), MaliciousRed)
        else -> Pair(Color.Gray.copy(alpha = 0.15f), Color.Gray)
    }
    
    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.small
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = riskLevel,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}