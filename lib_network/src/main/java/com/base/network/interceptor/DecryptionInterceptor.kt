package com.base.network.interceptor

import android.util.Base64
import android.util.Log
import com.base.network.utils.Encryptor
import com.base.network.utils.Encryptor.uncompress
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.lang.Exception

class DecryptionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        val encryptedBodyString = originalResponse.body?.string()
        // 这里进行解密
        val decryptedBodyString = decrypt(encryptedBodyString)
        Log.d("okhttp","decryptedData:$decryptedBodyString")

        val newResponseBody = decryptedBodyString.toResponseBody(originalResponse.body?.contentType())
        return originalResponse.newBuilder().body(newResponseBody).build()
    }

    private fun decrypt(encrypted: String?): String {
        try {
            val bytes: ByteArray =
                uncompress(
                    Encryptor.decrypt(
                        Base64.decode(
                            encrypted,
                            Base64.DEFAULT
                        )
                    )
                )
            return if (bytes.isEmpty()) {
                ""
            } else String(bytes)
        }catch (e:Exception){
            return ""
        }
    }
}
