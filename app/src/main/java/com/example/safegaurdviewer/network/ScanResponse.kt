package com.example.safegaurdviewer.network

data class ScanResponse(
    val data: ScanData
)

data class ScanData(
    val attributes: ScanAttributes
)

data class ScanAttributes(
    val last_analysis_stats: ScanStats
)

data class ScanStats(
    val harmless: Int,
    val malicious: Int,
    val suspicious: Int,
    val undetected: Int
)