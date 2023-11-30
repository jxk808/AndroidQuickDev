package com.base.network.response

/**
 * @author HhHh
 * @date   2023/6/27
 * @desc   通用数据类
 */
data class BaseResponse<out T>(
    val codeN: Int = 200,//服务器状态码 这里0表示请求成功
    val detail: String = "",//错误信息
    val data: T?,
) {

    /**
     * 判定接口返回是否正常
     */
    fun isFailed(): Boolean {
        return codeN != 200
    }
}
