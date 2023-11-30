package com.base.network.manager

import android.content.Context
import android.util.Log
import com.base.network.constant.BASE_URL
import com.base.network.interceptor.CookiesInterceptor
import com.base.network.interceptor.DecryptionInterceptor
import com.base.network.interceptor.EncryptionInterceptor
import com.base.network.interceptor.HeaderInterceptor
import com.base.framework.helper.AppHelper
import com.base.framework.utils.NetworkUtil
import com.sum.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigInteger
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

/**
 * @author HhHh
 * @date   2023/6/27
 * @desc  网络请求管理类
 */
object HttpManager {
    private val encryptedRetrofit: Retrofit

    val normalRetrofit: Retrofit by lazy {
        val context = AppHelper.getApplication()

        Retrofit.Builder()
            .client(initOkHttpClient(context))
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    init {
        encryptedRetrofit = Retrofit.Builder()
                .client(initEncryptedOkHttpClient())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    fun buildNormalRetrofit(context: Context,isEncrypted:Boolean): Retrofit = Retrofit.Builder()
        .client(initOkHttpClient(context,isEncrypted))
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * 获取 apiService
     */
    fun <T> create(apiService: Class<T>): T {
        return encryptedRetrofit.create(apiService)
    }

    /**
     * 获取 apiService
     */
    fun <T> createPlaintext(apiService: Class<T>): T {
        return normalRetrofit.create(apiService)
    }

    /**
     * 初始化OkHttp
     */
    private fun initEncryptedOkHttpClient(): OkHttpClient {
        val build = OkHttpClient.Builder()
                .connectTimeout(12, TimeUnit.SECONDS)
                .writeTimeout(12, TimeUnit.SECONDS)
                .readTimeout(12, TimeUnit.SECONDS)
        // 添加参数拦截器
        build.addInterceptor(CookiesInterceptor())
        build.addInterceptor(HeaderInterceptor())
        build.addInterceptor(EncryptionInterceptor())
        build.addInterceptor(DecryptionInterceptor())

        //日志拦截器
        val logInterceptor = HttpLoggingInterceptor { message: String ->
            if(message.length<=100){
                Log.i("okhttp", "data:$message")
            }
        }
        if (!BuildConfig.DEBUG){
            logInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        }else{
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }

        build.addInterceptor(logInterceptor)
        build.addInterceptor(logInterceptor)

        //网络状态拦截
        build.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                if (NetworkUtil.isConnected(AppHelper.getApplication())) {
                    val request = chain.request()
                    return chain.proceed(request)
                } else {
                    throw com.base.network.error.NoNetWorkException(com.base.network.error.ERROR.NETWORD_ERROR)
                }
            }
        })
        return build.build()
    }


    /**
     * 初始化OkHttp
     */
    private fun initOkHttpClient(context: Context,isEncrypted:Boolean = false): OkHttpClient {
        val build = OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
        // 添加参数拦截器
//        build.addInterceptor(CookiesInterceptor())

        if (isEncrypted){
            build.addInterceptor(EncryptionInterceptor())
            build.addInterceptor(DecryptionInterceptor())
        }

        //日志拦截器
        val logInterceptor = HttpLoggingInterceptor { message: String ->
            if(message.length<=100){
                Log.i("okhttp", "data:$message")
            }
        }
        if (!BuildConfig.DEBUG){
            logInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        }else{
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }

        build.addInterceptor(logInterceptor)
        build.addInterceptor(MD5LoggingInterceptor())

        //网络状态拦截
        build.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                if (NetworkUtil.isConnected(context)) {
                    val request = chain.request()
                    return chain.proceed(request)
                } else {
                    throw com.base.network.error.NoNetWorkException(com.base.network.error.ERROR.NETWORD_ERROR)
                }
            }
        })
        return build.build()
    }

    class MD5LoggingInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val requestBody = originalRequest.body

            if (requestBody != null) {
                val buffer = okio.Buffer()
                requestBody.writeTo(buffer)
                val bytes = buffer.readByteArray()
                val md5 = calculateMD5(bytes)
                println("第MD5 of request body: $md5")
            }

            return chain.proceed(originalRequest)
        }

        private fun calculateMD5(data: ByteArray): String {
            val md = MessageDigest.getInstance("MD5")
            val digest = md.digest(data)
            val bigInt = BigInteger(1, digest)
            return bigInt.toString(16).padStart(32, '0')
        }
    }

}
