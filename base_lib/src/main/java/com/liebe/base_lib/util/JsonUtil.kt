package com.dating.app.lib.util

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.stream.JsonReader
import java.lang.reflect.Type

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description jsonUtil
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
object JsonUtil {

    val GSON = GsonBuilder().disableHtmlEscaping().create()

    fun <T> fromJson(json: String, type: Type): T {
        return GSON.fromJson(json, type)
    }

    fun <T> fromJson(json: String, clazz: Class<T>): T {
        return GSON.fromJson(json, clazz)
    }

    fun <T> fromJson(bytes: ByteArray, clazz: Class<T>): T {
        return GSON.fromJson(String(bytes), clazz)
    }

    fun <T> fromJson(json: JsonElement, clazz: Class<T>): T {
        return GSON.fromJson(json, clazz)
    }

    fun <T> fromJson(json: JsonElement, type: Type): T {
        return GSON.fromJson(json, type)
    }

    fun <T> fromJson(json: JsonReader, clazz: Class<T>): T {
        return GSON.fromJson(json, clazz)
    }

    fun <T> fromJson(json: JsonReader, type: Type): T {
        return GSON.fromJson(json, type)
    }

    fun toJson(src: Any): String {
        return GSON.toJson(src)
    }

    fun <T> convert(jsonString: String?, classOfT: Class<T>): T? {
        return if (jsonString == null || jsonString.isEmpty()) {
            null
        } else GSON.fromJson(jsonString, classOfT)
    }
}

