package com.example.safegaurdviewer.scanner

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object FileUtil {

    fun uriToFile(context: Context, uri: Uri): File {

        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw Exception("Cannot open file")

        val file = File(context.cacheDir, "scan_temp_file")

        val outputStream = FileOutputStream(file)

        inputStream.copyTo(outputStream)

        inputStream.close()
        outputStream.close()

        return file
    }
}