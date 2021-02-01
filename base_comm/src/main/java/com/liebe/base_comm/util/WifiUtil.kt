package com.liebe.base_comm.util

import android.content.Context
import android.net.wifi.WifiManager

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Author Kong
 * @Date 2020/9/22
 * @Version 2.0
 */
object WifiUtil {


    fun open(){
        val wifiManager =
            UiUtil.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
        val status = wifiManager.wifiState
        if (status != WifiManager.WIFI_STATE_ENABLED) {
            //关闭状态则打开
            wifiManager.isWifiEnabled = true
        }
    }


}