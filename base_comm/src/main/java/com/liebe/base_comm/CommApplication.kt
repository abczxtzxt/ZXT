package com.liebe.base_comm

import com.dating.app.lib.util.AppUtil
import com.liebe.base_lib.BaseApplication
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description TODO
 * @Author LiangZe
 * @Date 2020/4/20
 * @Version 2.0
 */
open class CommApplication  : BaseApplication(){
    override fun onCreate() {
        super.onCreate()
        if (AppUtil.isMainProcess()){
            initRouter()
        }
    }

    //初始化路由
    private fun initRouter() {
        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
    }
}