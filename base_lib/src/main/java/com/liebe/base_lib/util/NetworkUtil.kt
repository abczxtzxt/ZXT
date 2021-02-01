package com.dating.app.lib.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import com.liebe.base_lib.BaseApplication

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description NetworkUtil
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */

@SuppressLint("MissingPermission")
object NetworkUtil {

    private fun getConnManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    fun isConnected(context: Context? = null): Boolean {
        val net = getConnManager(context ?: BaseApplication.gContext).activeNetworkInfo
        return net != null && net.isConnected
    }
}
