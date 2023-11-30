package com.base.network.interceptor

import android.util.Base64
import android.util.Log
import com.base.network.utils.Encryptor
import okhttp3.Interceptor
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.Buffer

class EncryptionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalBody = originalRequest.body

        val buffer = Buffer()
        originalBody?.writeTo(buffer)
        val originalBodyString = buffer.readUtf8()
        Log.d("okhttp","originalBodyString:$originalBodyString")

        // 这里进行加密
        val encryptedBodyString = encrypt(originalBodyString)

        val newRequestBody = encryptedBodyString.toRequestBody(originalBody?.contentType())
        val newRequest = originalRequest.newBuilder().method(originalRequest.method, newRequestBody).build()

        return chain.proceed(newRequest)
    }

    private fun encrypt(data: String?): String {
        val s = "$data&product=[SC]"

        val bytes = Base64.encode(
            com.base.network.utils.Encryptor.encrypt(
                s.replace("\\[".toRegex(), "").replace("]".toRegex(), "")
                    .replace(
                        " ".toRegex(),
                        "_"
                    )
                    .toByteArray()
            ),
            Base64.DEFAULT
        )
        return if (bytes == null || bytes.isEmpty()) {
            ""
        } else String(bytes)
    }

}