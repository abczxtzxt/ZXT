package com.liebe.base_lib

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import com.bumptech.glide.Glide
import com.dating.app.lib.extend.doAsync
import com.dating.app.lib.util.AppUtil
import com.dating.app.lib.util.Storage
import com.liebe.base_lib.util.CrashHandler
import java.util.*
import kotlin.system.exitProcess

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 基础Application封装
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
open class BaseApplication : Application() {

    companion object {
        lateinit var gContext: BaseApplication
    }

    private val sActivities = ArrayList<BaseActivity>()

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        gContext = this

//        MultiDex.install(this)

        if (AppUtil.isMainProcess()) {
            doAsync {
                CrashHandler.init(Storage.getPath(Storage.TYPE_CRASH))
            }
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        if (AppUtil.isMainProcess()) {
            Glide.get(this).clearMemory()
        }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (AppUtil.isMainProcess()) {
            if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
                Glide.get(this).clearMemory()
            }
        }
    }

    fun isStarted(): Boolean {
        return sActivities.size > 0
    }

    fun addActivity(activity: BaseActivity) {
        sActivities.add(activity)
    }

    fun removeActivity(activity: BaseActivity) {
        sActivities.remove(activity)
    }

    fun removeActivityAndFinish(activity: BaseActivity) {
        if (sActivities.contains(activity)) {
            activity.finish()
            sActivities.remove(activity)
        }
    }

    fun getActivityByName(string: String): BaseActivity? {
        var activity: BaseActivity? = null
        for (sActivity in sActivities) {
            if (sActivity::class.java.simpleName == string) {
                activity = sActivity
            }
        }
        return activity
    }

    fun getActiveActivity(): BaseActivity? {
        var activity: BaseActivity? = null
        for (i in sActivities.indices.reversed()) {
            val act = sActivities[i]
            if (!act.isFinishing && !act.isDestroyed()) {
                activity = act
                break
            }
        }
        return activity
    }

    fun getActivityStack(): List<BaseActivity> {
        return sActivities
    }

    private fun clearTask() {
        for (i in sActivities.indices.reversed()) {
            val act = sActivities[i]
            if (!act.isFinishing) {
                sActivities.removeAt(i)
                act.finish()
            }
        }
    }

    fun appExist(isSystemExit: Boolean) {
        clearTask()

        if (isSystemExit) {
            exitProcess(0)
        }
    }
}