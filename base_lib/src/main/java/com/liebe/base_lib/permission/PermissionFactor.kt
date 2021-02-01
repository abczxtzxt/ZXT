package com.dating.app.lib.permission

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.liebe.base_lib.BaseActivity
import com.liebe.base_lib.BaseFragment
import com.liebe.base_lib.action.ChainCallback
import com.liebe.base_lib.action.Factor

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 权限管理工具
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
class PermissionFactor: Factor {

    companion object{
        private const val PERMISSIONS_REQUEST_CODE = 42
    }

    private var activity: BaseActivity? = null
    private var fragment: BaseFragment? = null
    private var isActivity = true //是否为activity

    private var unPermissions: MutableList<String> = mutableListOf()
    private var permissions: MutableList<String> = mutableListOf()

    private var callback: ChainCallback? = null

    /**
     * @param act :Activity
     * @param permissions: 需要请求的permission,注意 setting的permission不可以申请了
     * @param onFailed: 请求失败的回调
     * @param interrupt: 是否中断: 请求成功后,是否中断操作
     */
    constructor(act:BaseActivity, permissions:MutableList<String>, onFailed: () -> Unit = {},
                interrupt: Boolean = false){
        isActivity = true
        this.activity = act
        this.permissions.addAll(permissions)
        this.activity?.addRequestPermissionListener(object :RequestPermissionListener{
            override fun onRequestPermissionsResult(
                requestCode: Int,
                permissions: Array<out String>,
                grantResults: IntArray
            ) {
                if(PERMISSIONS_REQUEST_CODE == requestCode){
                    for (index in permissions.indices) {
                        val permission = permissions[index]
                        if(grantResults[index] == PackageManager.PERMISSION_GRANTED){
                            unPermissions.remove(permission)
                        }
                    }
                }
                if(unPermissions.isNotEmpty()){
                    onFailed()
                    callback?.onInterrupt()
                }else{
                    if (interrupt) {
                        callback?.onInterrupt()
                    } else {
                        callback?.onContinue()
                    }
                }
                activity!!.addRequestPermissionListener(null)
            }

        })
    }

    /**
     * @param frag :Activity
     * @param permissions: 需要请求的permission,注意 setting的permission不可以申请了
     * @param onFailed: 请求失败的回调
     * @param interrupt: 是否中断: 请求成功后,是否中断操作
     */
    constructor(frag: BaseFragment, permissions:MutableList<String>, onFailed: () -> Unit = {},
                interrupt: Boolean = true){
        isActivity = false
        this.fragment = frag
        this.permissions.addAll(permissions)
        this.fragment?.addRequestPermissionListener(object :RequestPermissionListener{
            override fun onRequestPermissionsResult(
                requestCode: Int,
                permissions: Array<out String>,
                grantResults: IntArray
            ) {
                if(PERMISSIONS_REQUEST_CODE == requestCode){
                    for (index in permissions.indices) {
                        val permission = permissions[index]
                        if(grantResults[index] == PackageManager.PERMISSION_GRANTED){
                            unPermissions.remove(permission)
                        }
                    }
                }

                if(unPermissions.isNotEmpty()){
                    onFailed()
                    callback?.onInterrupt()
                }else{
                    if (interrupt) {
                        //中断之后,就不做任何操作了
                        callback?.onInterrupt()
                    } else {
                        callback?.onContinue()
                    }
                }
                fragment?.addRequestPermissionListener(null)
            }

        })
    }

    override fun isValid(): Boolean {
        val context = if(isActivity){
            activity
        }else{
            fragment?.context
        }
        unPermissions.clear()
        context?.let {
            permissions.forEach {
                if(PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                        context, it)){
                    unPermissions.add(it)
                }
            }
        }
        return unPermissions.isEmpty()
    }

    override fun doAction(callback: ChainCallback?) {
        if(isActivity){
            activity?.let {
                this.callback = callback
                ActivityCompat.requestPermissions(
                    activity!!,
                    unPermissions.toTypedArray(), PERMISSIONS_REQUEST_CODE
                )
            }
        }else{
            fragment?.let {
                this.callback = callback
                it.requestPermissions(
                    unPermissions.toTypedArray(), PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }


    interface RequestPermissionListener{
        fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    }


}