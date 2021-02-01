package com.liebe.base_comm.net

import com.dating.app.lib.util.AppUtil
import com.liebe.base_lib.BaseApplication
import com.liebe.base_lib.network.FakeInterceptor
import com.liebe.base_lib.network.JsonConverterFactory
import com.liebe.base_lib.network.LogInterceptor
import com.liebe.base_lib.network.ssl.SSLContextFactory
import com.liebe.base_lib.network.ssl.SimpleX509TrustManager
import com.liebe.base_lib.network.ssl.TrustedHostnameVerifier
import com.liebe.base_lib.util.Flog
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.stddating.baselib.network.CoroutineCallAdapterFactory
import okhttp3.ConnectionSpec
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 使用代理模式，将adapter与holder进行解耦
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
object ApiHelper {

    fun createRetrofitBuilder(): Retrofit.Builder = Retrofit.Builder()
        .client(HttpClient.CLIENT)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //support rxjava
        .addCallAdapterFactory(CoroutineCallAdapterFactory())// support coroutine
        .addConverterFactory(JsonConverterFactory(Gson()))//加密与解密处理工厂
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(HttpResult::class.java, Deserializer<HttpResult<*>>())
                    .registerTypeAdapter(BaseResult::class.java, Deserializer<BaseResult>())
                    .disableHtmlEscaping()
                    .create()
            )
        )

}

object HttpClient {
    private const val DEFAULT_CONNECT_TIMEOUT = 10L
    private const val DEFAULT_READ_TIMEOUT = 10L
    private const val DEFAULT_WRITE_TIMEOUT = 10L

    val CLIENT: OkHttpClient by lazy {
        createHttpClient()
    }

    private fun createHttpClient(): OkHttpClient {
        val dispatcher = Dispatcher()
        // 最大并发请求数
        dispatcher.maxRequests = 64
        // 每个Host最大并发请求数
        dispatcher.maxRequestsPerHost = 15

        val builder = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .dispatcher(dispatcher)
            .retryOnConnectionFailure(true)

        if (AppUtil.isDebug()) {
            builder.addInterceptor(FakeInterceptor(BaseApplication.gContext)) //fake net data
            builder.addNetworkInterceptor(LogInterceptor())
        }
        //添加Header
        builder.addInterceptor(HeaderInterceptor())
        // 配置SSL
        try {
            //解决：SSL handshake timed out ? ：https://blog.csdn.net/qq_16666847/article/details/54092297
            builder.connectionSpecs(arrayListOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT))
            builder.hostnameVerifier(TrustedHostnameVerifier.getTrustedVerifier())
            builder.sslSocketFactory(
                SSLContextFactory.getInstance().makeContext().socketFactory,
                SimpleX509TrustManager()
            )
        } catch (e: Exception) {
            Flog.e("icon_add ssl " + e.message)
        }
        return builder.build()
    }

}




object ApiPayHelper {

    fun createRetrofitBuilder(): Retrofit.Builder = Retrofit.Builder()
        .client(PayHttpClient.CLIENT)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //support rxjava
        .addCallAdapterFactory(CoroutineCallAdapterFactory())// support coroutine
        .addConverterFactory(JsonConverterFactory(Gson()))//加密与解密处理工厂
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(HttpResult::class.java, Deserializer<HttpResult<*>>())
                    .registerTypeAdapter(BaseResult::class.java, Deserializer<BaseResult>())
                    .disableHtmlEscaping()
                    .create()
            )
        )

}
//用于餐盘机支付 --- 特定时间2秒
object PayHttpClient {
    private const val DEFAULT_CONNECT_TIMEOUT = 2L
    private const val DEFAULT_READ_TIMEOUT = 3L
    private const val DEFAULT_WRITE_TIMEOUT = 3L

    val CLIENT: OkHttpClient by lazy {
        createHttpClient()
    }

    private fun createHttpClient(): OkHttpClient {
        val dispatcher = Dispatcher()
        // 最大并发请求数
        dispatcher.maxRequests = 64
        // 每个Host最大并发请求数
        dispatcher.maxRequestsPerHost = 15

        val builder = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .dispatcher(dispatcher)
            .retryOnConnectionFailure(false)

        if (AppUtil.isDebug()) {
            builder.addInterceptor(FakeInterceptor(BaseApplication.gContext)) //fake net data
            builder.addNetworkInterceptor(LogInterceptor())
        }
        //添加Header
        builder.addInterceptor(HeaderInterceptor())
        // 配置SSL
        try {
            //解决：SSL handshake timed out ? ：https://blog.csdn.net/qq_16666847/article/details/54092297
            builder.connectionSpecs(arrayListOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT))
            builder.hostnameVerifier(TrustedHostnameVerifier.getTrustedVerifier())
            builder.sslSocketFactory(
                SSLContextFactory.getInstance().makeContext().socketFactory,
                SimpleX509TrustManager()
            )
        } catch (e: Exception) {
            Flog.e("icon_add ssl " + e.message)
        }
        return builder.build()
    }

}