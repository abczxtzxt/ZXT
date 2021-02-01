package com.dating.app.lib.task

import java.util.concurrent.ScheduledThreadPoolExecutor
/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 线程池
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
class TaskExecutor private constructor() : ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2) {
    companion object {
        private const val LOCK = 0x01

        private var sExecutor: TaskExecutor? = null

        val instance: TaskExecutor
            get() {
                synchronized(LOCK) {
                    if (null == sExecutor) {
                        synchronized(LOCK) {
                            sExecutor = TaskExecutor()
                        }
                    }
                }
                return sExecutor!!
            }
    }
}