package com.example.safegaurdviewer.network

data class UrlAnalysisResponse(
    val data: UrlAnalysisData
)

data class UrlAnalysisData(
    val attributes: UrlAnalysisAttributes
)

data class UrlAnalysisAttributes(
    val last_analysis_stats: UrlStats
)

data class UrlStats(
    val malicious: Int,
    val suspicious: Int,
    val harmless: Int,
    val undetected: Int
)