package com.liebe.base_comm.util

import android.content.Context

/**
 * create by zhaimi
 * description:
 */
object SignatureUtil {
    init {
        System.loadLibrary("signature")
    }

    external fun signature(context: Context, string: String): String
}