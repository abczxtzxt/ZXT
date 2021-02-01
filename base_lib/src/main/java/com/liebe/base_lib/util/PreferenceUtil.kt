package com.dating.app.lib.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.liebe.base_lib.BaseApplication
import java.util.*

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description sp管理工具
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
object PreferenceUtil {

    private const val PREF_NAME = "Settings"

    private var sPref: SharedPreferences? = null

    init {
        sPref = BaseApplication.gContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    /**
     * 删除某值
     *
     * @param key
     */
    fun remove(key: String) {
        sPref?.edit {
            remove(key)
        }
    }

    /**
     * 保存Long型值
     *
     * @param key   关键字
     * @param value 值
     */
    fun setLong(key: String, value: Long) {
        sPref?.edit {
            putLong(key, value)
        }
    }

    /**
     * 获取指定关键字对应的Long型值
     *
     * @param key          关键字
     * @param defaultValue 默认值
     * @return key对应的value
     */
    fun getLong(key: String, defaultValue: Long): Long {
        return sPref!!.getLong(key, defaultValue)
    }

    /**
     * 获取指定关键字对应的Long型值
     *
     * @param key 关键字
     * @return key对应的value，默认-1
     */
    fun getLong(key: String): Long {
        return sPref!!.getLong(key, -1)
    }

    /**
     * 保存Float型值
     *
     * @param key   关键字
     * @param value 值
     */
    fun setFloat(key: String, value: Float) {
        sPref?.edit {
            putFloat(key, value)
        }
    }

    /**
     * 获取指定关键字对应的Float型值
     *
     * @param key          关键字
     * @param defaultValue 默认值
     * @return key对应的value
     */
    fun getFloat(key: String, defaultValue: Float): Float {
        return sPref!!.getFloat(key, defaultValue)
    }

    /**
     * 获取指定关键字对应的Float型值
     *
     * @param key 关键字
     * @return key对应的value，默认-1
     */
    fun getFloat(key: String): Float {
        return sPref!!.getFloat(key, -1.0f)
    }

    /**
     * 保存Int型值
     *
     * @param key   关键字
     * @param value 值
     */
    fun setInt(key: String, value: Int) {
        sPref?.edit {
            putInt(key, value)
        }
    }

    /**
     * 获取指定关键字对应的Int型值
     *
     * @param key          关键字
     * @param defaultValue 默认值
     * @return key对应的value
     */
    fun getInt(key: String, defaultValue: Int): Int {
        return sPref!!.getInt(key, defaultValue)
    }

    /**
     * 获取指定关键字对应的Int型值
     *
     * @param key 关键字
     * @return key对应的value，默认-1
     */
    fun getInt(key: String): Int {
        return sPref!!.getInt(key, -1)
    }

    /**
     * 保存指定Key对应的String
     *
     * @param key   关键字
     * @param value 值
     */
    fun setString(key: String, value: String) {
        sPref?.edit {
            putString(key, value)
        }
    }

    /**
     * 获取指定Key对应的字符串值,有默认值
     *
     * @param key          关键字
     * @param defaultValue 默认值
     * @return key对应的value
     */
    fun getString(key: String, defaultValue: String): String {
        return sPref!!.getString(key, defaultValue)
    }

    /**
     * 获取指定Key对应的String值
     *
     * @param key 关键字
     * @return key对应的value，默认为空字符串""
     */
    fun getString(key: String): String {
        return sPref!!.getString(key, "")
    }

    /**
     * 保存指定Key对应的Boolean值
     *
     * @param key   关键字
     * @param value 值
     */
    fun setBoolean(key: String, value: Boolean) {
        sPref?.edit {
            putBoolean(key, value)
        }
    }

    /**
     * 获取指定Key对应的boolean值
     *
     * @param key          关键字
     * @param defaultValue 默认值
     * @return key对应的value
     */
    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sPref!!.getBoolean(key, defaultValue)
    }

    /**
     * 获取指定Key对应的boolean值
     *
     * @param key 关键字
     * @return key对应的value, 默认为false
     */
    fun getBoolean(key: String): Boolean {
        return sPref!!.getBoolean(key, false)
    }

    /**
     * 是否包含指定Key
     *
     * @param key
     * @return
     */
    operator fun contains(key: String): Boolean {
        return sPref!!.contains(key)
    }

    /**
     * 保存set
     *
     * @param key
     * @param value
     */
    fun putSet(key: String, value: Set<String>) {
        sPref!!.edit().putStringSet(key, value).apply()
    }

    /**
     * 获取指定Key对应得set
     *
     * @param key
     * @return
     */
    fun getSet(key: String): Set<String>? {
        return sPref!!.getStringSet(key, HashSet())
    }
}
