package com.base.framework.ext

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


/**
 * 倒计时
 */
fun countDownCoroutines(
    total: Int,
    scope: CoroutineScope,
    dispatcher: CoroutineContext,
    onTick: ((Int) -> Unit)?= null,
    onStart: (() -> Unit)? = null,
    onFinish: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
): Job {
    return flow {
        for (i in total downTo 0) {
            emit(i)
            delay(1000)
        }
    }
            .flowOn(dispatcher)
            .onStart { onStart?.invoke() }
            .onCompletion { onFinish?.invoke() }//like java finally
            .onEach { onTick?.invoke(it) }
            .launchIn(scope)
}

object CountDownManager {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    val remainingTime = MutableLiveData<Int>()

    fun startVerificationCodeCountDown( total: Int,
                        onTick: ((Int) -> Unit)? = { remainingTime.value = it },
                        onStart: (() -> Unit)? = null,
                        onFinish: (() -> Unit)?  = { remainingTime.value = -1 }) {
        countDownCoroutines(total,scope,Dispatchers.IO,onTick,onStart,onFinish){
            stopCountDown()
        }
    }

    private fun stopCountDown() {
        job.cancel()
    }
}