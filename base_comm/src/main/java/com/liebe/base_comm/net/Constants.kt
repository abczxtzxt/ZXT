package com.liebe.base_comm.net

import com.dating.app.lib.util.AppUtil

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description Host配置
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
object Host {
    //测试服务器
    private const val DEBUG_HOST = "http://192.168.1.88/"
//    private const val DEBUG_HOST = "http://192.168.1.222:8020/"
//    private const val DEBUG_HOST = "http://192.168.70.111/"

//    private const val RELEASE_HOST = "http://192.168.70.111/"

    //正式服务器
    private const val RELEASE_HOST = "https://iliebe.cn/"
    val BASE_URL: String
        get() = if (AppUtil.isDebug()) {
            DEBUG_HOST
        } else {
            RELEASE_HOST
        }
}

//扫脸是否可用
var faceEnable = true

//刷卡是否可用
var cardEnable = true


