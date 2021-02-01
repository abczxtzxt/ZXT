package com.liebe.base_lib

import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dating.app.lib.permission.PermissionFactor
import com.dating.app.lib.util.fixInputMethodManagerLeak
import com.gyf.immersionbar.ktx.immersionBar
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.lang.reflect.Field


/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 基础Activity封装
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
abstract class BaseActivity : AppCompatActivity() {

    private var requestPermissionListener: PermissionFactor.RequestPermissionListener? = null
    private var isResume = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBar()
        initBeforeSetContent()
        super.onCreate(savedInstanceState)
        BaseApplication.gContext.addActivity(this)
        val layoutId = getLayoutId()
        if (layoutId > 0) setContentView(layoutId)
        initView()
        initViewModel()
    }


    open fun setStatusBar(needDarkFont: Boolean = false) {
        immersionBar() {
            statusBarColor(android.R.color.transparent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                statusBarDarkFont(needDarkFont, 0.2f)
            }
            fullScreen(true)
        }
    }


    open fun initBeforeSetContent() {}


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onResume() {
        super.onResume()
        isResume = true
    }

    override fun onPause() {
        super.onPause()
        isResume = false
    }

    override fun onDestroy() {
        fixInputMethodManagerLeak(this.applicationContext)
        BaseApplication.gContext.removeActivity(this)
        super.onDestroy()
    }

    fun isVisible() = isResume

    abstract fun getLayoutId(): Int

    abstract fun initView()

    open fun initViewModel() {

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        requestPermissionListener?.onRequestPermissionsResult(requestCode,permissions,grantResults)
    }

    fun addRequestPermissionListener(listener: PermissionFactor.RequestPermissionListener?){
        requestPermissionListener =  listener
    }


    /**
     * description:重设动画时间，避免开发者选项中动画时间设置对动画效果影响
     */
    fun resetAnimatorDurationScale() {
        try {
            val field: Field = ValueAnimator::class.java.getDeclaredField("sDurationScale")
            field.isAccessible = true;
            if (field.getFloat(null) == 0f) {
                field.setFloat(null, 1f);
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace();
        }
    }
}