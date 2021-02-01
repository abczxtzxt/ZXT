package com.dating.app.lib.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.liebe.base_lib.BaseApplication


/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 封装mvvm模式 viewmodel，主要用于业务逻辑处理
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
open class BaseViewModel<T : BaseRepository> constructor(application: Application = BaseApplication.gContext) :
    AndroidViewModel(application) {

    var mRepository: T? = null

    init {
        mRepository = getNewInstance(this, 0)
    }

    override fun onCleared() {
        super.onCleared()
        if (mRepository != null) {
            mRepository!!.unDisposable()
        }
    }

}
