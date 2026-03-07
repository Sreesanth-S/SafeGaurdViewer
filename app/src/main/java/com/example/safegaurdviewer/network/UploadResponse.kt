package com.example.safegaurdviewer.network

data class UploadResponse(
    val data: UploadData
)

data class UploadData(
    val id: String
)