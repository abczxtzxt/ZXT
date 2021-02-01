package com.dating.app.lib.cache

import android.content.Context
import android.util.Base64
import android.util.LruCache
import com.dating.app.lib.extend.doAsyncResult
import com.dating.app.lib.extend.ifNull
import com.dating.app.lib.util.SerializableUtil
import com.liebe.base_lib.cache.DiskCache
import com.liebe.base_lib.cache.DiskCachedObject
import java.io.IOException
import java.io.Serializable

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 缓存管理
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
class CacheManager private constructor() {
    companion object {
        const val MAX_SIZE = 2 * 1024 * 1024
        val instance: CacheManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { CacheManager() }
    }

    private lateinit var mSoftCache: LruCache<String, DiskCachedObject>
    private lateinit var mDiskCache: DiskCache

    fun init(context: Context) {
        mDiskCache = DiskCache(context.applicationContext)
        mSoftCache = LruCache(MAX_SIZE)
    }

    fun exists(key: String): Boolean {
        var result = false
        try {
            result = (null != mSoftCache.get(key) || mDiskCache.contains(key))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return result
    }

    fun clear() {
        try {
            mSoftCache.evictAll()
            mDiskCache.clearCache()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun remove(key: String) {
        try {
            mSoftCache.remove(key)
            mDiskCache.setKeyValue(key, null)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Serializable> get(key: String): T? {
        var result: T? = null
        try {
            val memoryCache = mSoftCache.get(key)
            if (memoryCache != null && !memoryCache.isExpired) {
                result = memoryCache.payload as T
            } else {
                val value = mDiskCache.getValue(key)
                if (value != null) {
                    val diskCache = fromString<Any>(value) as? DiskCachedObject

                    diskCache?.let {
                        if (!it.isExpired) {
                            mSoftCache.put(key, diskCache)
                            result = it.payload as T
                        }
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }


    @Suppress("UNCHECKED_CAST")
    fun <T : Serializable> getAsync(
        key: String,
        onSuccess: ((data: T?) -> Unit) = {},
        onFailed: ((exception: Exception) -> Unit) = {}
    ) {
        doAsyncResult {
            get(key) as? T
        }.get()?.let {
            onSuccess(it)
        }.ifNull {
            onFailed(Exception())
        }
    }

    /**
     * 多出一个过期时间
     */
    fun put(key: String, `object`: Serializable, expiryTimeSeconds: Int = -1): Boolean {
        var result = false
        try {
            val cachedObject = DiskCachedObject(`object`, expiryTimeSeconds)
            val value = toString(cachedObject)
            mSoftCache.put(key, cachedObject)
            mDiskCache.setKeyValue(key, value)
            result = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }


    fun <T : Serializable> putAsync(
        key: String, `object`: Serializable,
        onSuccess: (() -> Unit) = {},
        onFailed: (() -> Unit) = {},
        expiryTimeSeconds: Int = -1
    ) {
        doAsyncResult {
            put(key, `object`, expiryTimeSeconds)
        }.get().let {
            if (it) {
                onSuccess()
            } else {
                onFailed()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> fromString(data: String): T? {
        val `object` = SerializableUtil.deserialize(Base64.decode(data.toByteArray(), Base64.DEFAULT))
        return if (`object` != null) {
            `object` as T
        } else null
    }

    private fun toString(`object`: Any): String {
        val bytes = SerializableUtil.serialize(`object`)
        return if (bytes != null) {
            Base64.encodeToString(bytes, Base64.DEFAULT)
        } else ""
    }


}