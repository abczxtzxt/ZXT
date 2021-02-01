package com.liebe.base_comm.util

import android.content.res.AssetManager
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.liebe.base_lib.BaseApplication

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description TODO
 * @Author Kong
 * @Date 2020/9/2
 * @Version 2.0
 */
object UiUtil {

    val mHandler = Handler(Looper.getMainLooper())


    fun getContext() = BaseApplication.gContext

    fun getResources() = getContext().resources

    fun getColor(res: Int) = ContextCompat.getColor(getContext(), res)

    fun getDrawable(res: Int) = ContextCompat.getDrawable(getContext(), res)

    fun getString(res: Int) = getContext().getString(res)

    fun getAssets()=getContext().assets



    fun postDelayed(r:Runnable,time:Long){
        mHandler.postDelayed(r,time)
    }

    fun removeCallbacks(r:Runnable){
        mHandler.removeCallbacks(r)
    }



}