package com.base.network.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.base.network.flow.requestFlow
import com.base.network.response.BaseResponse
import com.base.framework.log.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

/**
 * @author HhHh
 * @date   2023/6/27
 * @desc   viewModel基类
 */
open class BaseViewModel : ViewModel() {
    /**
     * 运行在主线程中，可直接调用,（主要提供给livedata更改自身数值，也就是常用的UI数值）
     * @param errorBlock 错误回调
     * @param responseBlock 请求函数
     */
    fun launchUI(errorBlock: (Int?, String?) -> Unit , responseBlock: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            safeApiCall(errorBlock = errorBlock, responseBlock)
        }
    }

    /**
     * 需要运行在协程作用域中，这个函数的主要目标是执行一个可能会抛出异常的操作，并在出现异常时调用错误处理回调。它本身并不处理请求的结果，而是将结果直接返回给调用者。这意味着调用者需要自己处理请求的结果。（用于非常用UI数值的观察，比如获取接口的某个值，不需要常驻的UI数值）
     * @param errorBlock 错误回调
     * @param responseBlock 请求函数
     */
    suspend fun <T> safeApiCall(
        errorBlock: suspend (Int?, String?) -> Unit,
        responseBlock: suspend () -> T?
    ): T? {
        try {
            val data = responseBlock()
            return data
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtil.e(e)
            val exception = com.base.network.error.ExceptionHandler.handleException(e)
            errorBlock(exception.errCode, exception.errMsg)
        }
        return null
    }

    /**
     * 不依赖BaseRepository，运行在主线程中，可直接调用，（主要提供给livedata更改自身数值，也就是常用的UI数值）
     * @param errorCall 错误回调
     * @param responseBlock 请求函数
     * @param successBlock 请求回调
     */
    fun <T> launchUIWithResult(
        responseBlock: suspend () -> BaseResponse<T>?,
        errorCall: com.base.network.callback.IApiErrorCallback?,
        successBlock: (T?) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            val result = safeApiCallWithResult(errorCall = errorCall, responseBlock)
            successBlock(result)
        }
    }

    /**
     * 不依赖BaseRepository，需要在作用域中运行,这个函数在执行操作的同时处理返回的结果。如果请求成功，它将返回请求的数据。如果请求失败，它将调用错误处理回调。这意味着调用者不需要自己处理请求的结果，而是可以直接使用这个函数返回的数据。（用于非常用UI数值的观察，比如获取接口的某个值，不需要常驻的UI数值）
     * @param errorCall 错误回调
     * @param responseBlock 请求函数
     */
    private suspend fun <T> safeApiCallWithResult(
        errorCall: com.base.network.callback.IApiErrorCallback?,
        responseBlock: suspend () -> BaseResponse<T>?
    ): T? {
        try {
            val response = withContext(Dispatchers.IO) {
                withTimeout(10 * 1000) {
                    responseBlock()
                }
            } ?: return null

            if (response.isFailed()) {
                throw com.base.network.error.ApiException(response.codeN, response.detail)
            }
            return response.data
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtil.e(e)
            val exception = com.base.network.error.ExceptionHandler.handleException(e)
            if (com.base.network.error.ERROR.UNLOGIN.code == exception.errCode) {
                errorCall?.onLoginFail(exception.errCode, exception.errMsg)
            } else {
                errorCall?.onError(exception.errCode, exception.errMsg)
            }
        }
        return null
    }

    /**
     * flow 运行在主线程中，可直接调用，提供了一种简洁的方式来在ViewModel中进行网络请求，并在请求完成后更新UI。
     * @param errorCall 错误回调
     * @param requestCall 请求函数
     * @param showLoading 是否展示加载框
     * @param successBlock 请求结果
     */
    fun <T> launchFlow(
        errorCall: com.base.network.callback.IApiErrorCallback? = null,
        requestCall: suspend () -> BaseResponse<T>?,
        showLoading: ((Boolean) -> Unit)? = null,
        successBlock: (T?) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            val data = requestFlow(errorBlock = { code, error ->
                if (com.base.network.error.ERROR.UNLOGIN.code == code) {
                    errorCall?.onLoginFail(code, error)
                } else {
                    errorCall?.onError(code, error)
                }
            }, requestCall, showLoading)
            successBlock(data)
        }
    }

    /**
     * IO中处理请求-带基本Response类型
     */
    suspend fun <T> requestResponse(requestCall: suspend () -> BaseResponse<T>?): T? {
        val response = withContext(Dispatchers.IO) {
            withTimeout(10 * 1000) {
                requestCall()
            }
        } ?: return null

        if (response.isFailed()) {
            throw com.base.network.error.ApiException(response.codeN, response.detail)
        }
        return response.data
    }

}