package com.base.framework.ext

import android.util.Log
import com.base.framework.BuildConfig


inline fun <reified T : Any> T.logD(msg: Any?) {
    if (BuildConfig.DEBUG) {
        val text = msg?.toString()?:"is null"
        val tag = "log.out : ${this::class.java.simpleName}"
        Log.d(tag, text)
    }
}

