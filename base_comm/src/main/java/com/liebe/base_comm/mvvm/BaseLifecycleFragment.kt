package com.dating.app.lib.mvvm

import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.liebe.base_lib.BaseFragment
import java.util.*

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 封装MVVM模式 fragment。主用于界面展示
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
abstract class BaseLifecycleFragment<T : BaseViewModel<*>> : BaseFragment() {

    protected var mViewModel: T? = null
    private val eventKeys = ArrayList<Any>()

    override fun initViewModel() {
        super.initViewModel()
        mViewModel = VMProviders(this, getInstance<Any>(this, 0) as Class<T>)
        mViewModel?.let {
            dataObserver()
            it.mRepository?.loadState?.observe(this, observer)
        }
    }

    protected fun <T : ViewModel> VMProviders(fragment: BaseFragment, @NonNull modelClass: Class<T>): T {
        return ViewModelProviders.of(fragment).get(modelClass)

    }

    protected fun <T> registerSubscriber(eventKey: Any, tClass: Class<T>): MutableLiveData<T> {

        return registerSubscriber(eventKey, null, tClass)
    }

    protected fun <T> registerSubscriber(eventKey: Any, tag: String?, tClass: Class<T>): MutableLiveData<T> {
        val event: String = if (tag.isNullOrEmpty()) {
            eventKey as String
        } else {
            eventKey.toString() + tag
        }
        eventKeys.add(event)
        return LiveDataBus.instance.subscribe(eventKey, tag, tClass)
    }

    abstract fun dataObserver()

    override fun onDestroyView() {
        super.onDestroyView()
        clearEvent()
    }

    private fun clearEvent() {
        if (eventKeys.size > 0) {
            for (i in eventKeys.indices) {
                LiveDataBus.instance.clear(eventKeys[i])
            }
        }
    }

    protected var observer = Observer<String?> {
        it?.let {
            when (it) {
                StateConstants.ERROR_STATE -> {

                }
                StateConstants.NET_WORK_STATE -> {

                }
                StateConstants.LOADING_STATE -> {

                }
                StateConstants.SUCCESS_STATE -> {

                }
            }
        }
    }
}