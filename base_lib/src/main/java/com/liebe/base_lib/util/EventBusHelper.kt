package com.dating.app.lib.util

import org.greenrobot.eventbus.EventBus

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 包装EventBus
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
object EventBusHelper {

    private val EVENT_BUS = EventBus.getDefault()

    fun post(event: Any) {
        EVENT_BUS.post(event)
    }

    fun register(subscriber: Any) {
        if (!EVENT_BUS.isRegistered(subscriber)) {
            EVENT_BUS.register(subscriber)
        }
    }

    fun unregister(subscriber: Any) {
        if (EVENT_BUS.isRegistered(subscriber)) {
            EVENT_BUS.unregister(subscriber)
        }
    }

    fun postSticky(event: Any) {
        EVENT_BUS.postSticky(event)
    }
}