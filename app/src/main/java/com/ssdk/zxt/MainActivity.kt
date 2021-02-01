package com.ssdk.zxt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.liebe.base_lib.util.Flog
import com.ssdk.mylibrary.ZXT

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ZXT.write()
        ZXT.post()
        Flog.e("测试成功了")
    }
}