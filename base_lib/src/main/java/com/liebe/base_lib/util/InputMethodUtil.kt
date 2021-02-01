package com.dating.app.lib.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.liebe.base_lib.BaseFragment

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 输入法管理工具
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
fun fixInputMethodManagerLeak(destContext: Context?) {
    if (destContext == null) {
        return
    }

    val imm = destContext.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return

    val arr = arrayOf("mCurRootView", "mServedView", "mNextServedView", "mLastSrvView")
    try {
        for (param in arr) {
            val f = imm.javaClass.getDeclaredField(param)
            if (!f.isAccessible) {
                f.isAccessible = true
            }
            val obj_get = f.get(imm)
            if (obj_get != null && obj_get is View) {
                if (obj_get.context === destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                    f.set(imm, null) // 置空，破坏掉path to gc节点
                } else {
                    // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                    break
                }
            }
        }
    } catch (t: Throwable) {
    }
}


fun showInputMethod(editText: EditText?) {
    editText?.let {
        try {
            val imeOptions = it.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imeOptions?.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun hiddenInputMethod(activity: Activity?) {
    activity?.let {
        try {
            val imeOptions = it.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            if (imeOptions != null && imeOptions.isActive && null != it.currentFocus) {
                imeOptions.hideSoftInputFromWindow(
                    it.currentFocus!!.applicationWindowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun hiddenInputMethod(fragment: BaseFragment?) {
    fragment?.activity?.let {
        try {
            val imeOptions = it.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            if (imeOptions != null && imeOptions.isActive && null != it.currentFocus) {
                imeOptions.hideSoftInputFromWindow(
                    it.currentFocus!!.applicationWindowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

