package com.base.network.repository

import com.base.network.error.ApiException
import com.base.network.response.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

/**
 * @author HhHh
 * @date   2023/6/27
 * @desc   基础仓库
 */
open class BaseRepository {

    /**
     * IO中处理请求
     */
    suspend fun <T> requestResponse(requestCall: suspend () -> BaseResponse<T>?): T? {
        val response = withContext(Dispatchers.IO) {
            withTimeout(10 * 1000) {
                requestCall()
            }
        } ?: return null

        if (response.isFailed()) {
            throw ApiException(response.codeN, response.detail)
        }
        return response.data
    }
}