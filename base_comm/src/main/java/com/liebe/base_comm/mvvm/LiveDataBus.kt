package com.dating.app.lib.mvvm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dating.app.lib.mvvm.LiveDataBus.ObserverWrapper
import java.util.concurrent.ConcurrentHashMap

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 数据传递者，同时进行生命周期监听
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
class LiveDataBus private constructor() {

    private val mLiveBus: ConcurrentHashMap<Any, LiveBusData<Any>> = ConcurrentHashMap()

    companion object {
        val instance: LiveDataBus by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { LiveDataBus() }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> subscribe(eventKey: Any, tag: String? = null): MutableLiveData<T> {
        return subscribe(eventKey, tag, Any::class.java) as MutableLiveData<T>
    }

    fun <T> subscribe(eventKey: Any, tClass: Class<T>): MutableLiveData<T> {
        return subscribe(eventKey, null, tClass)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> subscribe(eventKey: Any, tag: String?, tClass: Class<T>): MutableLiveData<T> {
        val key = mergeEventKey(eventKey, tag)
        if (!mLiveBus.containsKey(key)) {
            mLiveBus[key] = LiveBusData(true)
        } else {
            val liveBusData = mLiveBus[key]
            liveBusData!!.isFirstSubscribe = false
        }

        return mLiveBus[key] as MutableLiveData<T>
    }

    fun <T> postEvent(eventKey: Any, value: T): MutableLiveData<T> {
        return postEvent(eventKey, null, value)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> postEvent(eventKey: Any, tag: String?=null, value: T): MutableLiveData<T> {
        val mutableLiveData = subscribe<Any>(mergeEventKey(eventKey, tag))
        mutableLiveData.postValue(value)
        return mutableLiveData as MutableLiveData<T>
    }

    fun clear(eventKey: Any) {
        clear(eventKey, null)
    }

    fun clear(eventKey: Any, tag: String?) {
        if (mLiveBus.size > 0) {
            val mEventKey = mergeEventKey(eventKey, tag)
            mLiveBus.remove(mEventKey)
        }
    }

    private fun mergeEventKey(eventKey: Any, tag: String?): String {
        return if (!tag.isNullOrEmpty()) {
            eventKey.toString() + tag
        } else {
            eventKey as String
        }
    }

    private class LiveBusData<T>(var isFirstSubscribe: Boolean) : MutableLiveData<T>() {

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, ObserverWrapper(observer, isFirstSubscribe))
        }
    }

    private class ObserverWrapper<T>(private val observer: Observer<T>?, private var isChanged: Boolean) : Observer<T> {
        override fun onChanged(t: T) {
            if (isChanged) {
                observer?.onChanged(t)
            } else {
                isChanged = true
            }
        }

    }
}