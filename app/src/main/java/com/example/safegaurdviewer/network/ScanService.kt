package com.example.safegaurdviewer.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ScanService {

    @GET("api/v3/files/{hash}")
    suspend fun getReport(
        @Header("x-apikey") apiKey: String,
        @Path("hash") hash: String
    ): ScanResponse

    @Multipart
    @POST("api/v3/files")
    suspend fun uploadFile(
        @Header("x-apikey") apiKey: String,
        @Part file: MultipartBody.Part
    ): UploadResponse

    @GET("api/v3/analyses/{id}")
    suspend fun getAnalysis(
        @Header("x-apikey") apiKey: String,
        @Path("id") id: String
    ): AnalysisResponse
}