package com.base.framework.event

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)  // 用于标记是否有待处理的新数据

    private val TAG = "SingleLiveEvent"

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }

        // 观察LiveData时，如果pending为true（有新的数据），则会触发Observer
        super.observe(owner, Observer<T> { t ->
            if (pending.compareAndSet(true, false)) {  // 如果当前为true，则设置为false，并返回操作前的值
                observer.onChanged(t)
            }
        })
    }

    /**
     * 设置要发送的值，当有活跃的观察者观察时会触发事件，但仅仅是第一个活跃的观察者
     *
     * @param t 要发送的新值
     */
    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)  // 每次设置新的值时，先将pending设为true
        super.setValue(t)
    }

    /**
     * 设置一个null值，这个方法用于触发一次事件，没有实际的数据发送
     */
    @MainThread
    fun call() {
        value = null
    }
}
