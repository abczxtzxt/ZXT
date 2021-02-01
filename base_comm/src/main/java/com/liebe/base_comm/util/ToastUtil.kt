package com.liebe.base_comm.util

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.liebe.base_comm.R
import com.liebe.base_lib.BaseApplication

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description ToastUtil
 * @Author LiangZe
 * @Date 2020/4/28
 * @Version 2.0
 */
@SuppressLint("InflateParams")
fun showToast(content: String?, time: Int = Toast.LENGTH_SHORT) {
    val toast = Toast(BaseApplication.gContext)
    val toastView =
        LayoutInflater.from(BaseApplication.gContext).inflate(R.layout.layout_toast, null).apply {
            this.findViewById<TextView>(R.id.tv_toast).apply {
                this.text = content
            }
        }
    toast.view = toastView
    toast.duration = time
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}

fun showToast(resId: Int?, time: Int = Toast.LENGTH_SHORT) {
    resId?.let {
        showToast(BaseApplication.gContext.resources.getString(resId),time)
    }
}