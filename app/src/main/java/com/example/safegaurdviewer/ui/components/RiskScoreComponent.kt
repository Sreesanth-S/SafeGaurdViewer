package com.example.safegaurdviewer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.safegaurdviewer.ui.theme.*

@Composable
fun RiskScoreMeter(
    score: Int,
    modifier: Modifier = Modifier
) {
    val riskColor = when {
        score <= 30 -> CyberGreen
        score <= 70 -> CyberYellow
        else -> CyberRed
    }

    val riskLabel = when {
        score <= 30 -> "SAFE"
        score <= 70 -> "SUSPICIOUS"
        else -> "MALICIOUS"
    }

    val riskDescription = when {
        score <= 30 -> "No threats detected"
        score <= 70 -> "Potential risk identified"
        else -> "Threat detected — take action"
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Score circle
        Box(
            modifier = Modifier
                .size(130.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(riskColor.copy(alpha = 0.15f), Color.Transparent)
                    ),
                    shape = CircleShape
                )
                .border(2.dp, riskColor.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = score.toString(),
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = riskColor,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "/ 100",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Risk level badge
        Box(
            modifier = Modifier
                .background(riskColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                .border(1.dp, riskColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = riskLabel,
                color = riskColor,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 2.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = riskDescription,
            fontSize = 12.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Progress bar
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("RISK LEVEL", fontSize = 10.sp, color = TextMuted, fontFamily = FontFamily.Monospace, letterSpacing = 1.sp)
                Text("$score%", fontSize = 10.sp, color = riskColor, fontFamily = FontFamily.Monospace)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(CyberElevated)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(score / 100f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(riskColor.copy(alpha = 0.7f), riskColor)
                            )
                        )
                )
            }
        }
    }
}

@Composable
fun RiskIndicatorBadge(
    riskLevel: String,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, label) = when (riskLevel.lowercase()) {
        "safe" -> Triple(CyberGreen.copy(alpha = 0.1f), CyberGreen, "SAFE")
        "suspicious" -> Triple(CyberYellow.copy(alpha = 0.1f), CyberYellow, "SUSPICIOUS")
        "malicious" -> Triple(CyberRed.copy(alpha = 0.1f), CyberRed, "MALICIOUS")
        else -> Triple(TextSecondary.copy(alpha = 0.1f), TextSecondary, riskLevel.uppercase())
    }

    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(6.dp))
            .border(1.dp, textColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 1.sp
        )
    }
}