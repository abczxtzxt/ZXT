package com.dating.app.lib.network

import io.reactivex.FlowableTransformer
import io.reactivex.schedulers.Schedulers

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description  RxJava工具类
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */


object RxUtil {

    /**
     * 调度转换，默认在io线程执行，main线程订阅
     *
     *
     * 用于IO密集型任务，如异步阻塞IO操作，这个调度器的线程池会根据需要增长；
     * Schedulers.io()默认是一个CachedThreadScheduler，很像一个有线程缓存的新线程调度器
     *
     * @param <T>
     * @return
    </T> */
    fun <T> ioMain(): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream ->
            upstream
                .subscribeOn(Schedulers.io())
                .observeOn(CustomSchedulers.mainThread())
        }
    }

    /**
     * 调度转换，默认在新线程执行，main线程订阅
     *
     * @param <T>
     * @return
    </T> */
    fun <T> newMain(): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream ->
            upstream
                .subscribeOn(Schedulers.newThread())
                .observeOn(CustomSchedulers.mainThread())
        }
    }
}
