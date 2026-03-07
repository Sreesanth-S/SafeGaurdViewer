package com.example.safegaurdviewer.scanner

import android.content.Context
import android.net.Uri
import java.security.MessageDigest

object HashUtil {

    fun sha256(context: Context, uri: Uri): String {

        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw Exception("Unable to open file")

        val digest = MessageDigest.getInstance("SHA-256")

        val buffer = ByteArray(8192)
        var bytesRead: Int

        while (true) {
            bytesRead = inputStream.read(buffer)
            if (bytesRead == -1) break
            digest.update(buffer, 0, bytesRead)
        }

        inputStream.close()

        return digest.digest().joinToString("") {
            "%02x".format(it)
        }
    }
}