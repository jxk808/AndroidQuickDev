package com.base.network.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface FileService {
    @Streaming
    @GET
    suspend fun downloadFile(@Url url:String): ResponseBody?
}