package com.example.safegaurdviewer.data

object LinkScanResultHolder {
    var scannedUrl: String = ""

    // VirusTotal overall result
    var isMalicious: Boolean = false

    // Detection ratio (example: "3/70")
    var detectionRatio: String = ""

    // Risk score (0–100)
    var riskScore: Int = 0

    // Threat reasons list
    var threatReasons: List<String> = emptyList()

    // Function to clear old result when new scan starts
    fun clear() {
        scannedUrl = ""
        isMalicious = false
        detectionRatio = ""
        riskScore = 0
        threatReasons = emptyList()
    }
}