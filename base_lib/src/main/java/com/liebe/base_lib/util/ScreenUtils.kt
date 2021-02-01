package com.dating.app.lib.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import com.liebe.base_lib.BaseApplication


/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description ScreenUtil
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
object ScreenUtils {
    fun getScreenHeight(): Int {
        val mWindowManager = BaseApplication.gContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val mDisplay = mWindowManager.defaultDisplay
        val mDisplayMetrics = DisplayMetrics()
        mDisplay.getRealMetrics(mDisplayMetrics)
        return mDisplayMetrics.heightPixels
    }

    fun getScreenWidth(): Int {
        val mWindowManager = BaseApplication.gContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val mDisplay = mWindowManager.defaultDisplay
        val mDisplayMetrics = DisplayMetrics()
        mDisplay.getRealMetrics(mDisplayMetrics)
        return mDisplayMetrics.widthPixels
    }

}