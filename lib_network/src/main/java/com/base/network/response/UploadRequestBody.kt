package com.base.network.response

import android.util.Log
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class ProgressRequestBody(private val file: File, private val listener: UploadProgressListener) : RequestBody() {

    override fun contentType(): MediaType? {
        return "application/vnd.android.package-archive".toMediaTypeOrNull()
    }

    override fun contentLength(): Long {
        return file.length()
    }

    override fun writeTo(sink: BufferedSink) {
        val fileLength = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val inputStream = FileInputStream(file)
        var uploaded: Long = 0

        inputStream.use { input->
            var read: Int
            while (input.read(buffer).also { read = it } != -1) {
                uploaded += read.toLong()
                Log.d("小文件上传进度","percentage：${(100 * uploaded / fileLength).toInt()}%")
                sink.write(buffer, 0, read)
                listener.onProgressUpdate((100 * uploaded / fileLength).toInt())
            }
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048
    }

    interface UploadProgressListener {
        fun onProgressUpdate(percentage: Int)
    }

}

/**
 * 用于上传文件的特定块的RequestBody
 * @param data 要上传的文件的ByteArray
 * @param listener 上传进度监听器
 */
class ChunkedProgressRequestBody(
    private val data: ByteArray,
    private val listener: UploadProgressListener
) : RequestBody() {

    // 返回当前块的内容类型
    override fun contentType(): MediaType? {
        return "application/octet-stream".toMediaTypeOrNull()
    }

    // 返回当前块的长度
    override fun contentLength(): Long {
        return data.size.toLong()
    }

    // 写入当前块的内容到输出流
    override fun writeTo(sink: BufferedSink) {
        var uploaded: Long = 0
        sink.write(data)
        uploaded += data.size
        listener.onProgressUpdate((100 * uploaded / contentLength()).toInt()) // 更新上传进度
    }

    interface UploadProgressListener {
        fun onProgressUpdate(percentage: Int)
    }
}



