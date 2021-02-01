package com.dating.app.lib.extend

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description kotlin扩展，如果object为空，需要执行的部分
 *              val object = null
 *              object.ifNull{ doSomeThing() }
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
fun Any?.ifNull(block: () -> Unit) {
    if (this == null) block()
    if (this is String) { //如果是String,Empty也要执行
        if (this.isEmpty()) {
            block()
        }
    }
}