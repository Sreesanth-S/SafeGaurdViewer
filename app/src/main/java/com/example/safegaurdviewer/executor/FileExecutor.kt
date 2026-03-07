package com.example.safegaurdviewer.executor

import android.content.Context
import android.content.Intent
import android.net.Uri

object FileExecutor {

    fun openFile(context: Context, uri: Uri) {

        val intent = Intent(Intent.ACTION_VIEW)

        intent.setData(uri)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        context.startActivity(intent)
    }
}