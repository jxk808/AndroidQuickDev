package com.base.network.utils

import android.content.Context
import com.base.network.api.FileService
import com.base.network.bean.DownloadResult
import com.base.network.bean.DownloadUrlError
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

object FileUtils {
    suspend inline fun downloadFile(context:Context,url:String,onProgress: (progress:Int)->Unit): com.base.network.bean.DownloadResult {
        val endUrl = url.substringAfterLast('/',"")
        val name = endUrl.substringBeforeLast('.')

        val dir = File(context.cacheDir.path,"update_apk")

        if (!dir.exists()){
            dir.mkdirs()
        }

        val outPath = dir.path + "/" + name + ".apk"

        return downloadFile(url,File(outPath),onProgress)
    }

    suspend inline fun downloadFile(url:String, outFile: File, onProgress: (progress:Int)->Unit): com.base.network.bean.DownloadResult {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .writeTimeout(12, TimeUnit.SECONDS)
            .readTimeout(12, TimeUnit.SECONDS)
            .build()

        val beforeUrl = url.substringBeforeLast('/',"https://soft.shanchen.com")

        val baseUrl = "$beforeUrl/"

        val endUrl = url.substringAfterLast('/',"").ifEmpty {
            return com.base.network.bean.DownloadResult.Error(com.base.network.bean.DownloadUrlError())
        }

        try {
            Retrofit.Builder().client(okHttpClient).baseUrl(baseUrl).build()
                .create(com.base.network.api.FileService::class.java).downloadFile(endUrl)?.let { body ->
                    copyWithProgress(body, outFile){ progress ->
                        onProgress(progress)
                    }
                }
        } catch (e: Exception) {
            /*   if (saveFile.exists()) {
                   saveFile.delete()
               }*/

            return com.base.network.bean.DownloadResult.Error(e)
        }
        return com.base.network.bean.DownloadResult.Success(outFile.path)
    }

    inline fun copyWithProgress(body:ResponseBody,outFile:File,onProgress: (progress:Int)->Unit){
        val buffer = ByteArray(1024)
        val contentLength: Long = body.contentLength()
        var lastProgress = 0

        body.byteStream().use { input ->
            FileOutputStream(outFile).use { outputStream ->
                var length: Int
                var sum: Long = 0
                while (input.read(buffer).also { length = it } != -1) {
                    outputStream.write(buffer, 0, length)
                    sum += length.toLong()
                    val progress = (sum * 100 / contentLength).toInt()
                    if (progress > lastProgress) {
                        lastProgress = progress

                        onProgress(progress)
                    }
                }
                outputStream.flush()
            }
        }
    }
}