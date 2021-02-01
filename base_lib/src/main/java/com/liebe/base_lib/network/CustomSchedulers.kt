package com.dating.app.lib.network

import android.os.Looper
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 自定义 Schedulers.
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
object CustomSchedulers {

    private val MAIN_THREAD = RxAndroidPlugins.initMainThreadScheduler { CustomSchedulers.MainHolder.DEFAULT }

    private object MainHolder {
        internal val DEFAULT = AndroidSchedulers.from(Looper.getMainLooper(), true)
    }

    fun mainThread(): Scheduler {
        return RxAndroidPlugins.onMainThreadScheduler(MAIN_THREAD)
    }
}