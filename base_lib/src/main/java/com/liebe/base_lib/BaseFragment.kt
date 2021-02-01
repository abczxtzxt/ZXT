package com.liebe.base_lib

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dating.app.lib.permission.PermissionFactor

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 基础fragment封装
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
abstract class BaseFragment : Fragment() {

    private var requestPermissionListener: PermissionFactor.RequestPermissionListener? = null

    lateinit var rootView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(getLayoutResId(), null, false)
        initView()
        initViewModel()
        return rootView
    }

    abstract fun getLayoutResId(): Int

    abstract fun initView()

    fun <T : View> findViewById(id: Int): T = rootView.findViewById(id)

    open fun initViewModel() {}


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        requestPermissionListener?.onRequestPermissionsResult(requestCode,permissions,grantResults)
    }

    fun addRequestPermissionListener(listener: PermissionFactor.RequestPermissionListener?){
        requestPermissionListener =  listener
    }
}