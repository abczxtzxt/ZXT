package com.dating.app.comm.net

import android.os.SystemClock

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description onFailed外部调用时，则需要自行处理相关逻辑，否则统一处理
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
object Timestamp {

    private var mTimeDiff = System.currentTimeMillis() - SystemClock.elapsedRealtime()

    fun get(): Long {
        return SystemClock.elapsedRealtime() + mTimeDiff
    }
}