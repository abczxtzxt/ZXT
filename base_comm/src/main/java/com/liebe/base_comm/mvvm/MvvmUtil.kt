package com.dating.app.lib.mvvm

import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
fun <T> getNewInstance(`object`: Any?, i: Int): T? {
    `object`?.let {
        try {
            return ((`object`.javaClass
                .genericSuperclass as ParameterizedType).actualTypeArguments[i] as Class<T>)
                .newInstance()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }
    return null

}

@Suppress("UNCHECKED_CAST")
fun <T> getInstance(`object`: Any?, i: Int): T? {
    return if (`object` != null) {
        (`object`.javaClass
            .genericSuperclass as ParameterizedType)
            .actualTypeArguments[i] as T
    } else null

}