package com.base.network.interceptor

import com.base.common.provider.UserServiceProvider
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author HhHh
 * @date   2023/6/27
 * @desc   头信息拦截器
 * 添加头信息
 */
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newBuilder = request.newBuilder()
        newBuilder.addHeader("Content-type", "application/json; charset=utf-8")

        //添加用户token
        val token = UserServiceProvider.getUserInfo()?.token ?: ""
        newBuilder.addHeader("Authorization", token)

//        val host = request.url.host
//        val url = request.url.toString()
//
//        //给有需要的接口添加Cookies
//        if (!host.isNullOrEmpty()  && (url.contains(COLLECTION_WEBSITE)
//                        || url.contains(NOT_COLLECTION_WEBSITE)
//                        || url.contains(ARTICLE_WEBSITE)
//                        || url.contains(COIN_WEBSITE))) {
//            val cookies = CookiesManager.getCookies()
//            LogUtil.e("HeaderInterceptor:cookies:$cookies", tag = "smy")
//            if (!cookies.isNullOrEmpty()) {
//                newBuilder.addHeader(KEY_COOKIE, cookies)
//            }
//        }
        return chain.proceed(newBuilder.build())
    }
}