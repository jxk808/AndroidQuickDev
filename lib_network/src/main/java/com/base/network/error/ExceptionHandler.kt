package com.base.network.error

import android.graphics.Color
import android.net.ParseException
import androidx.fragment.app.FragmentActivity
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import com.base.common.dialog.MessageDialog
import com.base.common.provider.MainServiceProvider
import com.base.common.provider.UserServiceProvider
import com.base.framework.manager.ActivityManager
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

/**
 * 统一错误处理工具类
 */
object ExceptionHandler {

    fun handleException(e: Throwable): ApiException {

        val ex: ApiException
        if (e is ApiException) {
            ex = ApiException(e.errCode, e.errMsg, e)
            if (ex.errCode == com.base.network.error.ERROR.UNLOGIN.code){
                //登录失效
                if (UserServiceProvider.isLogin()){
                    UserServiceProvider.clearUserInfo()
                    val messageDialog = MessageDialog.Builder(ActivityManager.top() as FragmentActivity).apply {
                        setTitle("温馨提示")
                        setMessage("您的账号已在其他设备登录，请确认 账号安全。")
                        setMessageTxtColor(Color.parseColor("#666666"))
                        setCancel(null)
                        setCancelable(false)
                        setCanceledOnTouchOutside(false)
                        setonConfirmListener {
                            it?.dismiss()
                            ActivityManager.top()?.let { it1 -> MainServiceProvider.toMain(it1) }
                            if (ActivityManager.tasksSize()>1){
                                ActivityManager.top()?.finish()
                            }

                        }
                    }.create()
                    messageDialog.show()

                }
            }
        } else if (e is com.base.network.error.NoNetWorkException) {
//            TipsToast.showTips("网络异常，请尝试刷新")
            ex =
                ApiException(com.base.network.error.ERROR.NETWORD_ERROR, e)
        } else if (e is HttpException) {
            ex = when (e.code()) {
                com.base.network.error.ERROR.UNAUTHORIZED.code -> ApiException(
                    com.base.network.error.ERROR.UNAUTHORIZED,
                    e
                )
                com.base.network.error.ERROR.FORBIDDEN.code -> ApiException(
                    com.base.network.error.ERROR.FORBIDDEN,
                    e
                )
                com.base.network.error.ERROR.NOT_FOUND.code -> ApiException(
                    com.base.network.error.ERROR.NOT_FOUND,
                    e
                )
                com.base.network.error.ERROR.REQUEST_TIMEOUT.code -> ApiException(
                    com.base.network.error.ERROR.REQUEST_TIMEOUT,
                    e
                )
                com.base.network.error.ERROR.GATEWAY_TIMEOUT.code -> ApiException(
                    com.base.network.error.ERROR.GATEWAY_TIMEOUT,
                    e
                )
                com.base.network.error.ERROR.INTERNAL_SERVER_ERROR.code -> ApiException(
                    com.base.network.error.ERROR.INTERNAL_SERVER_ERROR,
                    e
                )
                com.base.network.error.ERROR.BAD_GATEWAY.code -> ApiException(
                    com.base.network.error.ERROR.BAD_GATEWAY,
                    e
                )
                com.base.network.error.ERROR.SERVICE_UNAVAILABLE.code -> ApiException(
                    com.base.network.error.ERROR.SERVICE_UNAVAILABLE,
                    e
                )
                else -> ApiException(e.code(), e.message(), e)
            }
        } else if (e is JsonParseException
                || e is JSONException
                || e is ParseException
                || e is MalformedJsonException
        ) {
            ex = ApiException(com.base.network.error.ERROR.PARSE_ERROR, e)
        } else if (e is ConnectException) {
            ex =
                ApiException(com.base.network.error.ERROR.NETWORD_ERROR, e)
        } else if (e is javax.net.ssl.SSLException) {
            ex = ApiException(com.base.network.error.ERROR.SSL_ERROR, e)
        } else if (e is java.net.SocketException) {
            ex =
                ApiException(com.base.network.error.ERROR.TIMEOUT_ERROR, e)
        } else if (e is java.net.SocketTimeoutException) {
            ex =
                ApiException(com.base.network.error.ERROR.TIMEOUT_ERROR, e)
        } else if (e is java.net.UnknownHostException) {
            ex = ApiException(com.base.network.error.ERROR.UNKNOW_HOST, e)
        } else {
            ex = if (!e.message.isNullOrEmpty()) ApiException(
                1000,
                e.message!!,
                e
            )
            else ApiException(com.base.network.error.ERROR.UNKNOWN, e)
        }
        return ex
    }
}
