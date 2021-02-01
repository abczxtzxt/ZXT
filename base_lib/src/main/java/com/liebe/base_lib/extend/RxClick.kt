package com.dating.app.lib.extend

import android.annotation.SuppressLint
import android.os.Looper
import android.view.View
import androidx.annotation.RestrictTo
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import io.reactivex.disposables.Disposables
import java.util.concurrent.TimeUnit

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 防止单位时间多次点击
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
class RxClick internal constructor(private val view: View) : Observable<Any>() {

    override fun subscribeActual(observer: Observer<in Any>) {
        if (!checkMainThread(observer)) {
            return
        }
        val listener = Listener(view, observer)
        observer.onSubscribe(listener)
        view.setOnClickListener(listener)
    }

    private fun checkMainThread(observer: Observer<*>): Boolean {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            observer.onSubscribe(Disposables.empty())
            observer.onError(IllegalStateException("Expected to be called on the main thread but was " + Thread.currentThread().name))
            return false
        }
        return true
    }

    internal class Listener(private val view: View, private val observer: Observer<in Any>) : MainThreadDisposable(),
        View.OnClickListener {

        override fun onClick(v: View) {
            if (!isDisposed) {
                observer.onNext(Notification.INSTANCE)
            }
        }

        override fun onDispose() {
            view.setOnClickListener(null)
        }
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    enum class Notification {
        INSTANCE
    }
}

/**
 * 扩展View的Click回调，解决短时内重复点击的问题
 *
 * exp:
 * ImageView(context).click{ doSomeThing() }
 */
@SuppressLint("CheckResult")
fun View.click(delayTime: Long = 500, onClick: (v: View) -> Unit) {
    RxClick(this).throttleFirst(delayTime, TimeUnit.MILLISECONDS).subscribe { onClick(this) }
}
