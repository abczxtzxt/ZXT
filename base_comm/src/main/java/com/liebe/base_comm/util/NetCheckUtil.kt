package com.liebe.base_comm.util

import android.content.Context
import android.net.ConnectivityManager
import com.liebe.base_lib.BaseApplication

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 网络是否可用检查工具类
 * @Author LiangZe
 * @Date 2020/4/24
 * @Version 2.0
 */
class NetCheckUtil {

    fun checkNet(): Boolean {
        return isMobileConnnection() || isWifiConnection()
    }

    /**
     * 判断手机接入点（APN）是否处于可以使用的状态
     */
    fun isMobileConnnection(): Boolean {
        val manager =
            BaseApplication.gContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * 判断当前wifi是否是处于可以使用状态
     */
    fun isWifiConnection(): Boolean {
        val manager =
            BaseApplication.gContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return networkInfo != null && networkInfo.isConnected();
    }
}