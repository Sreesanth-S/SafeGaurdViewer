package com.example.safegaurdviewer.scanner

data class ScanOutput(
    val score: Int,
    val malicious: Int,
    val total: Int
)