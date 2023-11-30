package com.base.network.callback

import com.base.network.response.BaseResponse
import com.base.framework.log.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 */
suspend fun <T> safeApiCallWithResult(
    responseBlock: suspend () -> BaseResponse<T>?,
    errorCall: (e: Exception)->Unit
): T? {
    try {
        val response = withContext(Dispatchers.IO) {
            responseBlock()
        } ?: return null

        if (response.isFailed()) {
            throw com.base.network.error.ApiException(response.codeN, response.detail)
        }
        return response.data
    } catch (e: Exception) {
        e.printStackTrace()
        LogUtil.e(e)
        errorCall(e)
    }
    return null
}

/**
 * 不依赖BaseRepository，需要在作用域中运行,这个函数在执行操作的同时处理返回的结果。如果请求成功，它将返回请求的数据。如果请求失败，它将调用错误处理回调。这意味着调用者不需要自己处理请求的结果，而是可以直接使用这个函数返回的数据。（用于非常用UI数值的观察，比如获取接口的某个值，不需要常驻的UI数值）
 * @param errorCall 错误回调
 * @param responseBlock 请求函数
 */
suspend fun <T> safeApiCallWithResult(
    responseBlock: suspend () -> BaseResponse<T>?,
    errorCall: com.base.network.callback.IApiErrorCallback?
): T? {
    try {
        val response = withContext(Dispatchers.IO) {
            responseBlock()
        } ?: return null

        if (response.isFailed()) {
            throw com.base.network.error.ApiException(response.codeN, response.detail)
        }
        return response.data
    } catch (e: Exception) {
        e.printStackTrace()
        LogUtil.e(e)
        val exception = com.base.network.error.ExceptionHandler.handleException(e)

        withContext(Dispatchers.Main){
            if (com.base.network.error.ERROR.UNLOGIN.code == exception.errCode) {
                errorCall?.onLoginFail(exception.errCode, exception.errMsg)
            } else {
                errorCall?.onError(exception.errCode, exception.errMsg)
            }
        }
    }
    return null
}

suspend fun <T> safeApiCall(
    responseBlock: suspend () -> T?,
    errorCall: com.base.network.callback.IApiErrorCallback?
): T? {
    try {
        val response = withContext(Dispatchers.IO) {
            responseBlock()
        } ?: return null

        return response
    } catch (e: Exception) {
        e.printStackTrace()
        LogUtil.e(e)
        val exception = com.base.network.error.ExceptionHandler.handleException(e)

        withContext(Dispatchers.Main){
            if (com.base.network.error.ERROR.UNLOGIN.code == exception.errCode) {
                errorCall?.onLoginFail(exception.errCode, exception.errMsg)
            } else {
                errorCall?.onError(exception.errCode, exception.errMsg)
            }
        }
    }
    return null
}