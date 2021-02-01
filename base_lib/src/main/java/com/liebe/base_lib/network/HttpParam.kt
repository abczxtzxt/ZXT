package com.dating.app.lib.network

import android.text.TextUtils
import androidx.collection.ArrayMap
import com.dating.app.lib.util.JsonUtil
import com.liebe.base_lib.util.AesUtil
import com.liebe.base_lib.util.Flog
import okhttp3.RequestBody

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description http参数处理
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */

class HttpParam : ArrayMap<String, String>() {

    fun put(key: String, value: Int) {
        put(key, value.toString())
    }

    fun put(key: String, value: Long) {
        put(key, value.toString())
    }

    fun put(key: String, value: Boolean) {
        put(key, value.toString())
    }

    fun put(key: String, value: Float) {
        put(key, value.toString())
    }

    fun put(key: String, value: Double) {
        put(key, value.toString())
    }

    override fun put(key: String, value: String): String? {
        // 过滤掉空字符串或null
        if (!TextUtils.isEmpty(value)) {
            super.put(key, value)
        }
        return value
    }

    fun removeAll(map: Map<String, String>) {
        for ((key) in map) {
            remove(key)
        }

    }

    /**
     * 默认排序输出
     */
    fun build(): String {
        return this.toSortedMap().asSequence().map { (key, value) -> "$key=$value" }
            .reduce { pre, next -> "$pre&$next" }
    }


    companion object {
        /**
         * 参数校验
         */
        fun valueOf(vararg pairs: Pair<String, Any?>) = HttpParam().apply {
            for ((key, value) in pairs) {
                when (value) {
                    null -> put(key, "")
                    is Boolean -> put(key, value)
                    is Int -> put(key, value)
                    is Long -> put(key, value)
                    is Float -> put(key, value)
                    is Double -> put(key, value)
                    is String -> put(key, value)
                    else -> {
                        // not support
                    }
                }
            }
        }
    }
}

//业务需求，map转string后加密，并返回map
fun encryParam(paramMap: Map<String, String>): Map<String, String> {
    val json = JsonUtil.toJson(paramMap)
    val encryString = AesUtil().encrypt(json)
    Flog.e("HttpParam", json)
    return mapOf("param" to encryString)
}

fun encryParamLogin(paramMap: Map<String, String>): Map<String, String> {
    return mapOf("param" to AesUtil().encrypt(paramMap.toString()))
}

//获取requestBody，以传送json
fun getRequestBody(any: Any): RequestBody {
    return RequestBody.create(
        okhttp3.MediaType.parse("application/json； charset=utf-8"),
        JsonUtil.toJson(any)
    )
}