package com.liebe.base_comm.util.card

import android.content.Context
import android.content.res.Configuration
import android.view.InputDevice
import android.view.KeyEvent
import com.liebe.base_comm.AppSetting
import com.liebe.base_lib.util.Flog
import java.util.*
import kotlin.concurrent.timerTask


/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 读卡器工具类
 * @Author LiangZe
 * @Date 2020/4/21
 * @Version 2.0
 */
class ReadCardUtil(var onReadFinished: (content: String) -> Unit = {}) {
    //是否是大写字母
    private var isCapital = false
    private var readStringBuilder: StringBuilder = StringBuilder()

    @Volatile
    private var isWaitForPost: Boolean = false


    @Synchronized
    @Suppress("DEPRECATED_IDENTITY_EQUALS")
    fun dispatchKey(context: Context, event: KeyEvent?) {
        //检测输入设备是否是读卡器，是则读取内容
        event?.let {
            if (it.device == null) {
                return
            }
            if (it.keyCode == KeyEvent.KEYCODE_BACK || it.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || it.keyCode == KeyEvent.KEYCODE_VOLUME_UP) { //实体按键，若按键为返回、音量加减、返回false
                return
            }
            if (it.device.sources == InputDevice.SOURCE_KEYBOARD or InputDevice.SOURCE_DPAD or InputDevice.SOURCE_CLASS_BUTTON) { //虚拟按键返回false
                return
            }
            val cfg = context.getResources().getConfiguration()
            if (cfg.keyboard == Configuration.KEYBOARD_UNDEFINED) {
                return
            }

            if (isWaitForPost) {
                readStringBuilder.setLength(0)
                return
            }

            //读卡器内容读取
            val keyCode = it.keyCode
            if (keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT || keyCode == KeyEvent.KEYCODE_SHIFT_LEFT) {
                isCapital = it.action === KeyEvent.ACTION_DOWN
            }
            if (it.action == KeyEvent.ACTION_DOWN) {
                if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) { //字母
                    val char =
                        ((if (isCapital) 'A' else 'a').toInt() + keyCode - KeyEvent.KEYCODE_A).toChar()
                    Flog.d("ReadCardUtil", Character.toString(char))
                    readStringBuilder.append(char)
                } else if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) { //数字
                    val char = ('0'.toInt() + keyCode - KeyEvent.KEYCODE_0).toChar()
                    Flog.d("ReadCardUtil", Character.toString(char))
                    readStringBuilder.append(char)
                } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    val content = readStringBuilder.toString()

                    if (content.length != AppSetting.cardLength) {
                        //无效卡0
//                        initFormat()
                        if (content.contains(";")) {
                            var replace = content.replace(";", "")
                                .replace("?", "")
                                .replace(",", "")
                            if (replace.length > AppSetting.cardLength) {
                                replace = replace.substring(0, 8)
                            }
                            onReadFinished.invoke(replace)
                        } else {
                            var replace = content.replace(";", "")
                                .replace("?", "")
                                .replace(",", "")
                            if (replace.length > AppSetting.cardLength) {
                                replace = replace.substring(replace.length - 8, replace.length)
                            }
                            onReadFinished.invoke(replace)
                        }


                    } else {
                        onReadFinished.invoke(content)
                    }
                    readStringBuilder.setLength(0)
                } else {
                    //无效卡
//                    initFormat()
//                    readStringBuilder.setLength(0)
                }
            }

        }
    }

//    private fun initFormat() {
//        if (isWaitForPost) {
//            return
//        }
//        isWaitForPost = true
////        CardUtil.initFormat()
//        Timer().schedule(timerTask {
//            isWaitForPost = false
//        }, 500)
//    }

}