package com.liebe.base_comm.net

import com.dating.app.lib.util.JsonUtil
import com.google.gson.*
import com.liebe.base_lib.util.Flog
import retrofit2.HttpException
import java.io.IOException
import java.lang.reflect.Type

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 错误初步解析
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
internal const val TAG = "Request"

fun dealNext(result: BaseResult): Boolean {
    return when (result.StateCode) {
        ErrorCode.SUCCESS.code -> {
            true
        }
        else -> {
            false
        }
    }
}

fun dealError(t: Throwable): ApiException {
    return when (t) {
        is HttpException -> {
            val result = t.response().errorBody()?.string() ?: t.message()
            Flog.w(TAG, "HttpException ${t.code()} $result", t)
            if (t.code() >= 500) {
                // 500以上表示服务器有问题
                ApiException(ErrorCode.SERVER_EXCEPTION.code, ErrorCode.SERVER_EXCEPTION.message)
            } else {
                try {
                    // 尝试解析错误
                    parseError(JsonParser().parse(result), true)
                } catch (e: Throwable) {
                    ApiException(ErrorCode.UNKNOWN.code, ErrorCode.UNKNOWN.message)
                }
            }
        }
        is JsonParseException, is JsonIOException, is JsonSyntaxException -> {
            // 数据解析异常
            Flog.w(TAG, "JsonException: ${t.message}", t)
            ApiException(ErrorCode.ERROR_PARSE.code, ErrorCode.ERROR_PARSE.message)
        }
        is IOException -> {
            // 网络异常，ConnectException、SocketTimeoutException均属于IOException
            Flog.w(TAG, "IOException: ${t.message}", t)
            ApiException(ErrorCode.ERROR_NETWORK.code, ErrorCode.ERROR_NETWORK.message)
        }
        is ApiException -> {
            // API异常
            Flog.w(TAG, "ApiException ${t.error.code} ${t.message}", t)
            t
        }
        else -> {
            // 未知异常
            Flog.w(TAG, "UnknownException: ${t.message}", t)
            ApiException(ErrorCode.UNKNOWN.code, ErrorCode.UNKNOWN.message)
        }
    }
}

private fun parseError(json: JsonElement, shoTips: Boolean): ApiException {
    if (json.isJsonObject) {
        val obj = json.asJsonObject
        val code = obj.get("code")?.asInt
        val msg = obj.get("msg")?.asString ?: ""
        code?.let {
            return ApiException(code, msg)
        }
    }
    return ApiException(ErrorCode.UNKNOWN)
}

class Deserializer<T> : JsonDeserializer<T> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): T? {
        return JsonUtil.fromJson(json, typeOfT)
    }
}
