package com.example.safegaurdviewer.scanner

import android.content.Context
import android.net.Uri
import com.example.safegaurdviewer.network.ApiClient
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import com.example.safegaurdviewer.scanner.ScanOutput

object FileScanner {

    private const val API_KEY = "96bf27fd3d0dee600d159399083953c40175bf2f55d298babedea2649640149f"

    suspend fun scanFile(context: Context, uri: Uri): ScanOutput {

        try {

            // Convert URI → File
            val file = FileUtil.uriToFile(context, uri)

            println("Scanning file: ${file.name}")
            println("File size: ${file.length()} bytes")

            val requestFile =
                file.asRequestBody("application/octet-stream".toMediaTypeOrNull())

            val body = MultipartBody.Part.createFormData(
                "file",
                file.name,
                requestFile
            )

            // -------- Upload with retry --------

            var uploadResponse = ApiClient.service.uploadFile(API_KEY, body)
            val analysisId = uploadResponse.data.id

            println("Upload successful. Analysis ID: $analysisId")

            // -------- Poll analysis --------

            var status = "queued"

            var malicious = 0
            var suspicious = 0
            var harmless = 0
            var undetected = 0

            var attempts = 0
            val maxAttempts = 12   // ~1 minute

            while (status != "completed" && attempts < maxAttempts) {

                delay(5000)

                println("Checking analysis status...")

                val analysisResponse =
                    ApiClient.service.getAnalysis(API_KEY, analysisId)

                status = analysisResponse.data.attributes.status

                println("Current status: $status")

                if (status == "completed") {

                    val stats = analysisResponse.data.attributes.stats

                    malicious = stats.malicious
                    suspicious = stats.suspicious
                    harmless = stats.harmless
                    undetected = stats.undetected

                    println("Scan finished.")
                    println("Malicious: $malicious")
                    println("Suspicious: $suspicious")
                    println("Harmless: $harmless")
                    println("Undetected: $undetected")
                }

                attempts++
            }

            // -------- Calculate score --------

            val total =
                malicious + suspicious + harmless + undetected

            if (total == 0) return ScanOutput(0,0,0)

            val rawScore =
                ((malicious * 2 + suspicious).toFloat() / total * 100).toInt()

            val riskScore = rawScore.coerceAtMost(100)
            return ScanOutput(
                score = riskScore,
                malicious = malicious,
                total = total
            )

            return ScanOutput(
                score = riskScore,
                malicious = malicious,
                total = total
            )

        } catch (e: Exception) {

            println("SCAN ERROR: ${e.message}")
            e.printStackTrace()

            // fallback safe value
            return ScanOutput(10, 0, 0)
        }
    }
}