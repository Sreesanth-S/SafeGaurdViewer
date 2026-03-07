package com.example.safegaurdviewer.network

data class AnalysisResponse(
    val data: AnalysisData
)

data class AnalysisData(
    val attributes: AnalysisAttributes
)

data class AnalysisAttributes(
    val status: String,
    val stats: AnalysisStats
)

data class AnalysisStats(
    val malicious: Int,
    val suspicious: Int,
    val harmless: Int,
    val undetected: Int
)