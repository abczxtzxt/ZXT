package com.ssdk.mylibrary

import android.util.Log
import com.ssdk.zxt_network.NetworkHelper


class ZXT {
    companion object {
        fun write() {
            Log.e("zxt", "我是write library 数据")
        }

        fun read() {
            Log.e("zxt", "我是read library 数据")
        }

        fun add(a: Int, b: Int): Int {
            return a + b
        }

        fun post() {
            NetworkHelper.post()
        }
    }

}