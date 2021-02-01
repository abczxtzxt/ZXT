package com.dating.app.comm.net

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.dating.app.lib.mvvm.BaseRepository
import com.dating.app.lib.mvvm.StateConstants
import com.liebe.base_comm.net.*
import io.reactivex.subscribers.DisposableSubscriber

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 外部调用时，则需要自行处理相关逻辑，否则统一处理,需要知道网络访问状态，例如网络连接错误，需要传入repostory
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
class HttpResultSubscriber<T>(
    val repostory:BaseRepository?=null,
    owner: LifecycleOwner? = null,
    val onSuccess: ((data: T?) -> Unit) = {},
    val onFailed: ((e: ApiException) -> Unit) = {
        ErrorCode.handleCode(it.error)
    }
) : DisposableSubscriber<HttpResult<T>>(), LifecycleObserver {

    init {
        owner?.lifecycle?.addObserver(this)
    }

    override fun onComplete() {

    }

    override fun onNext(result: HttpResult<T>) {
        if (dealNext(result)) {
            onSuccess(result.Data)
            repostory?.postState(StateConstants.SUCCESS_STATE)
        } else {
            val apiException = dealError(ApiException(result.StateCode, result.StateInfo))
            onFailed(apiException)
            repostory?.postState(getState(apiException))
        }
    }

    override fun onError(t: Throwable) {
        onFailed(dealError(t))
        repostory?.postState(getState(dealError(t)))
    }

    fun getState(apiException: ApiException):String{
        val state:String
        when(apiException.error){
            ErrorCode.SERVER_EXCEPTION -> {
                state=StateConstants.SERVER_EXCEPTION
            }
            ErrorCode.ERROR_PARSE -> {
                state=StateConstants.PARSE_ERROR_STATE
            }
            else -> {
                state=StateConstants.UNKNOWN_STATE
            }
        }
        return state
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        if (!isDisposed) {
            dispose()
        }
    }
}