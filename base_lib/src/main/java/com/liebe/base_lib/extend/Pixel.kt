package com.dating.app.lib.extend

import android.content.Context
import com.liebe.base_lib.BaseApplication


/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 像素转换工具
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */

/**
 * int to Px
 * val width = 15.dp2Px()
 * val width = 15.dp2Px(context)
 */
fun Int.dp2Px(context: Context? = null): Int {
    val ctx = context ?: BaseApplication.gContext
    return (this * ctx.resources.displayMetrics.density + 0.5f).toInt()
}

/**
 * float to Px
 * val width = 1f.dp2Px()
 * val width = 1f.dp2Px(context)
 */
fun Float.dp2Px(context: Context? = null): Float {
    val ctx = context ?: BaseApplication.gContext
    return (this * ctx.resources.displayMetrics.density + 0.5f).toFloat()
}

/**
 * int sp to Px
 * val width = 15.sp2Px()
 * val width = 15.sp2Px(context)
 */
fun Int.sp2Px(context: Context? = null): Int {
    val ctx = context ?: BaseApplication.gContext
    return (this * ctx.resources.displayMetrics.scaledDensity + 0.5f).toInt()
}

/**
 * float sp to Px
 * val width = 1f.sp2Px()
 * val width = 1f.sp2Px(context)
 */
fun Float.sp2Px(context: Context? = null): Float {
    val ctx = context ?: BaseApplication.gContext
    return (this * ctx.resources.displayMetrics.scaledDensity + 0.5f).toFloat()
}