package com.dating.app.lib.mvvm

import androidx.lifecycle.MutableLiveData
import com.dating.app.lib.extend.ifNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 封装mvvm模式 repository，主用于网络访问与数据传输
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
open class BaseRepository {
private var mCompositeDisposable: CompositeDisposable? = null

var loadState: MutableLiveData<String> = MutableLiveData()


fun postState(state: String) {
    loadState.postValue(state)
}

protected fun postData(eventKey: Any, t: Any) {
    postData(eventKey, null, t)
}

protected fun postData(eventKey: Any, tag: String?, t: Any) {
    LiveDataBus.instance.postEvent(eventKey, tag, t)
}

protected fun addDisposable(disposable: Disposable) {
    mCompositeDisposable.ifNull {
        mCompositeDisposable = CompositeDisposable()
    }
    mCompositeDisposable!!.add(disposable)
}

fun unDisposable() {
    mCompositeDisposable?.let {
        if (!it.isDisposed) {
            it.dispose()
        }
    }
}
}