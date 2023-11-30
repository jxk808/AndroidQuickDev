package com.base.network.interceptor

import com.base.framework.helper.AppHelper
import com.base.framework.manager.AppManager
import com.base.framework.utils.DeviceInfoUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.net.URLEncoder

/**
 * @author HhHh
 * @date   2023/6/27
 * @desc   公共参数拦截器
 */
class PublicParameterInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newBuilder = request.newBuilder().apply {
            addHeader("device-type", "android")
            addHeader("app-version", AppManager.getAppVersionName(AppHelper.getApplication()))
            addHeader("device-id", DeviceInfoUtils.androidId)
            addHeader("device-os-version", AppManager.getDeviceBuildRelease())//获取手机系统版本号
            val deviceNameStr = AppManager.getDeviceBuildBrand().plus("_")
                    .plus(AppManager.getDeviceBuildModel())
            addHeader("device-name", URLEncoder.encode(deviceNameStr, "UTF-8"))//获取设备类型
        }

        return chain.proceed(newBuilder.build())
    }
}