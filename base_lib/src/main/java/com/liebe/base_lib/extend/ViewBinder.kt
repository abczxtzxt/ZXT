@file:Suppress("UNCHECKED_CAST")

package com.dating.app.lib.extend

import android.app.Activity
import android.view.View
import com.liebe.base_lib.BaseFragment

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 扩展findViewByid
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
fun <V : View> Activity.bind(id: Int): Lazy<V> = lazy {
    viewFinder(id) as V
}

fun <V : View> BaseFragment.bind(id: Int): Lazy<V> = lazy {
    viewFinder(id) as V
}

fun <V : View> View.bind(id: Int): Lazy<V> = lazy {
    viewFinder(id) as V
}

private val Activity.viewFinder: Activity.(Int) -> View?
    get() = { findViewById(it) }

private val BaseFragment.viewFinder: BaseFragment.(Int) -> View?
    get() = { findViewById(it) }

private val View.viewFinder: View.(Int) -> View?
    get() = { findViewById(it) }