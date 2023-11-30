package com.base.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.multidex.BuildConfig
import com.alibaba.android.arouter.launcher.ARouter
import com.base.framework.helper.AppHelper
import com.base.framework.manager.ActivityManager
import com.tencent.mmkv.MMKV

class BaseApplication:Application() {
    override fun onCreate() {
        super.onCreate()

        AppHelper.init(this, BuildConfig.DEBUG)

        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (BuildConfig.DEBUG) {
            // 开启打印日志
            ARouter.openLog()
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug()
        }
        //初始化MMKV
        MMKV.initialize(this)

        registerActivityLifecycle()
    }


    /**
     * 注册Activity生命周期监听
     */
    private fun registerActivityLifecycle() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityDestroyed(activity: Activity) {
                ActivityManager.pop(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivityCreated(activity: Activity, p1: Bundle?) {
                ActivityManager.push(activity)
            }

            override fun onActivityResumed(activity: Activity) {

            }
        })
    }

}