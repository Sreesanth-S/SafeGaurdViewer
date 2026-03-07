package com.example.safegaurdviewer.scanner

import android.util.Base64
import com.example.safegaurdviewer.network.ApiClient

object LinkScanner {

    private const val API_KEY = "96bf27fd3d0dee600d159399083953c40175bf2f55d298babedea2649640149f"

    suspend fun scanUrl(url: String): Int {

        try {

            val fixedUrl =
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    "https://$url"
                else url

            val encoded = Base64.encodeToString(
                fixedUrl.toByteArray(),
                Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
            )

            val response =
                ApiClient.service.getUrlReport(API_KEY, encoded)

            val stats = response.data.attributes.last_analysis_stats

            val malicious = stats.malicious
            val suspicious = stats.suspicious
            val harmless = stats.harmless
            val undetected = stats.undetected

            val total = malicious + suspicious + harmless + undetected

            if (total == 0) return 0

            val rawScore =
                ((malicious * 2 + suspicious).toFloat() / total * 100).toInt()

            return rawScore.coerceAtMost(100)

        } catch (e: Exception) {

            println("LINK SCAN ERROR: ${e.message}")
            e.printStackTrace()

            return 10
        }
    }
}