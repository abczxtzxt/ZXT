package com.dating.app.lib.util

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.text.TextUtils
import com.dating.app.lib.cache.CacheManager
import com.dating.app.lib.extend.ifNull
import com.liebe.base_lib.BaseApplication
import com.liebe.base_lib.BuildConfig
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description app util
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
object AppUtil {

    private var sDebug: Boolean? = null
    private var sDeviceID: String = ""

    /**
     * Debug mode or not
     */
    fun isDebug(): Boolean {
        if (sDebug == null) {
            val context = BaseApplication.gContext
            sDebug =
                (context.applicationInfo != null && (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0))
        }
        return sDebug as Boolean
    }

    /**
     *Determine if it is a main thread
     */
    fun isMainProcess(): Boolean {
        val info: PackageInfo
        try {
            val context = BaseApplication.gContext
            info = context.packageManager.getPackageInfo(context.packageName, 0)
            val packageNames = info.packageName
            if (!TextUtils.isEmpty(packageNames) && packageNames == getCurrentProcessName()) {
                return true
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return false
    }

    private fun getCurrentProcessName(): String? {
        val pid = android.os.Process.myPid()
        val manager = BaseApplication.gContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (appProcess in manager.runningAppProcesses) {
            if (appProcess.pid == pid) {
                return appProcess.processName
            }
        }
        return null
    }


    /**
     * 程序是否在前台运行
     * @return
     */
    fun isAppOnForeground(): Boolean {
        val activityManager = BaseApplication.gContext.getSystemService(
            Context.ACTIVITY_SERVICE
        ) as ActivityManager

        val packageName = BaseApplication.gContext.packageName

        val appProcesses = activityManager
            .runningAppProcesses ?: return false

        for (appProcess in appProcesses) {
            if (appProcess.processName == packageName && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true
            }
        }
        return false
    }

    fun getOSVersion(): String {
        return android.os.Build.VERSION.RELEASE
    }

    fun getDevice(): String {
        return android.os.Build.MODEL
    }

    fun getVersionName(): String {
        var versionName = "1.0"
        val manager = BaseApplication.gContext.packageManager
        try {
            val info = manager.getPackageInfo(BaseApplication.gContext.packageName, 0)
            versionName = info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionName
    }

    fun getVersionCode(): Int {
        var versionCode: Int = 1
        val manager = BaseApplication.gContext.packageManager
        try {
            val info = manager.getPackageInfo(BaseApplication.gContext.packageName, 0)
            versionCode = info.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCode
    }

    fun getDeviceID(): String {
        if (sDeviceID.isEmpty()) {
            CacheManager.instance.get<String>("DeviceID")?.let {
                sDeviceID = it
                sDeviceID
            }.ifNull {
                sDeviceID = getMac()
                CacheManager.instance.putAsync<String>("DeviceID", sDeviceID)
            }
        }
        return sDeviceID
    }
    private fun getUUID(): String {
        if (sDeviceID.isEmpty()) {
            CacheManager.instance.get<String>("DeviceUUID")?.let {
                sDeviceID = it
                sDeviceID
            }.ifNull {
                sDeviceID = UUID.randomUUID().toString()
            }
        }
        return sDeviceID
    }

    //获取mac地址,防止android6.0以上获取mac 地址返回虚拟02:00:00:00:00:00 的问题
    private fun getMac(): String {
        try {
            val networkInterfaces = NetworkInterface.getNetworkInterfaces()
            while (networkInterfaces.hasMoreElements()) {
                val networkInterface = networkInterfaces.nextElement()
                if ("wlan0" == networkInterface.name) {
                    val hardwareAddress = networkInterface.hardwareAddress
                    if (hardwareAddress.isEmpty()) {
                        continue
                    }
                    val buf = StringBuilder()
                    for (b in hardwareAddress!!) {
                        buf.append(String.format("%02X:", b))
                    }
                    if (buf.isNotEmpty()) {
                        buf.deleteCharAt(buf.length - 1)
                    }
                    return buf.toString().replace(":", "", false)
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

        return getUUID()
    }

}
