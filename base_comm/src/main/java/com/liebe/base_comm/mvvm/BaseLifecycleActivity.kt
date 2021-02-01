package com.dating.app.lib.mvvm

import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.liebe.base_lib.BaseActivity
import com.liebe.base_lib.util.Flog

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 封装MVVM模式activity，主用于界面展示
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */


abstract class BaseLifecycleActivity<T : BaseViewModel<*>> : BaseActivity() {

    protected var mViewModel: T? = null

    companion object {
        val TAG = this::class.java.name
    }

    @Suppress("UNCHECKED_CAST")
    override fun initViewModel() {
        super.initViewModel()
        mViewModel = vmProviders(this, getInstance<Any>(this, 0) as Class<T>)
        mViewModel?.let {
            dataObserver()
            it.mRepository?.loadState?.observe(this, observer)
        }
    }

    private fun <T : ViewModel> vmProviders(activity: AppCompatActivity, @NonNull modelClass: Class<T>): T {
        return ViewModelProviders.of(activity).get(modelClass)
    }

    abstract fun dataObserver()

    open fun visitSuccessObserver() {}

    open fun networkErrorObserver() {}

    open fun visitErrorObserver() {}

    protected var observer = Observer<String?> {
        it?.let {
            when (it) {
                StateConstants.NET_WORK_ERROR_STATE -> {
                    networkErrorObserver()
                }
                StateConstants.PARSE_ERROR_STATE -> {
                    visitErrorObserver()
                }
                StateConstants.UNKNOWN_STATE -> {
                    visitErrorObserver()
                }
                StateConstants.SUCCESS_STATE -> {
                    visitSuccessObserver()
                }
            }
        }
    }

}