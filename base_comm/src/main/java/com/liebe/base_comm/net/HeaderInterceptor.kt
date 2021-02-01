package com.liebe.base_comm.net

import com.liebe.base_comm.util.getToken
import okhttp3.Interceptor
import okhttp3.Response

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 封装Header
 * @Author LiangZe
 * @Date 2020/4/27
 * @Version 2.0
 */

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val proceed = chain.proceed(
            chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader(
                    "Authorization",
                    getToken()
                )
                .build()
        )
        return proceed
    }
}