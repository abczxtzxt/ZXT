package com.liebe.base_comm.util.card

import android.app.PendingIntent.getBroadcast
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import com.liebe.base_lib.BaseApplication
import com.liebe.base_lib.util.Flog


/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description TODO
 * @Author Kong
 * @Date 2020/9/7
 * @Version 2.0
 */
object CardUtil {

    private val TAG: String = javaClass.name
    private const val ACTION_USB_PERMISSION = "com.template.USB_PERMISSION"

    fun initFormat() {
        initFormat(null)
    }

    fun initFormat(l: ((success: Boolean) -> Unit)?) {
        val context = BaseApplication.gContext
        val manager = context
            .getSystemService(Context.USB_SERVICE) as UsbManager
        val deviceList = manager.deviceList
        val keySet: Set<String> = deviceList.keys
        for (s in keySet) {
            val usbDevice = deviceList[s]
            val vendorId = usbDevice!!.vendorId
            val productId = usbDevice.productId
            if (vendorId == 65535 && productId == 53) {
                //找到USB设备
                if (!manager.hasPermission(usbDevice)) {
                    Flog.d(TAG, "没有权限")
                    //没有授权，请求授权
                    //广播
                    val broadcastReceiver = object : BroadcastReceiver() {
                        override fun onReceive(context: Context, intent: Intent?) {
                            if (intent == null) {
                                return
                            }
                            val action = intent.action
                            if (action == ACTION_USB_PERMISSION) {
                                context.unregisterReceiver(this)
                                val usbDevice: UsbDevice? =
                                    intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                                if (intent.getBooleanExtra(
                                        UsbManager.EXTRA_PERMISSION_GRANTED,
                                        false
                                    )
                                ) {
                                    if (null != usbDevice) {
                                        val openAndInit = openAndInit(usbDevice, manager)
                                        if (openAndInit) {
                                            l?.invoke(true)
                                            return
                                        }
                                    }

                                    l?.invoke(false)
                                }
                                Flog.d(TAG, "反注册广播")
                                context.unregisterReceiver(this)
                            }
                        }
                    }
                    Flog.d(TAG, "注册广播")
                    context.registerReceiver(broadcastReceiver, IntentFilter(ACTION_USB_PERMISSION))

                    val mPermissionIntent =
                        getBroadcast(BaseApplication.gContext, 0, Intent(ACTION_USB_PERMISSION), 0)

                    manager.requestPermission(usbDevice, mPermissionIntent)
                    return
                } else {
                    Flog.d(TAG, "有权限")
                    val openAndInit = openAndInit(usbDevice, manager)
                    if (openAndInit) {
                        l?.invoke(true)
                        return
                    }
                }
                break
            }
        }
        //失败回调
        l?.invoke(false)
    }


    private fun openAndInit(
        usbDevice: UsbDevice?,
        manager: UsbManager?
    ): Boolean {
        Flog.d(TAG, "开始设置读卡器格式")
        //读卡器USB
        val api = ICReaderApi.getInstance(usbDevice, manager)
        if (api.isOk) {
            //准备就绪

            //准备初始化数据
            val sendCode = ByteArray(8)

            //8位16进制
            sendCode[0] = 0x0C
            //不加分号
            sendCode[1] = 0x00
            //不加问号
            sendCode[2] = 0x00
            //不加逗号
            sendCode[3] = 0x00
                    //加回车
            sendCode[4] = 0x01
            //不加蜂鸣器
            sendCode[5] = 0x00
            //默认写法
            sendCode[6] = 0x01
            //写自定义数据
            sendCode[7] = 0x01

            try {
                val buffer = ByteArray(1)
                val result = api.API_SetSerNum(sendCode, buffer)
                var text: String? = ""
                text = showStatue(text, result)
                text = showStatue(text, buffer[0].toInt())


                if (result == 0x00 || buffer[0].toInt() == 0x00) {
                    Flog.d(TAG, "读卡器格式设置成功")
                    return true
                }
//                showToast(text)
                Flog.d(TAG, "读卡器格式设置失败")
            } catch (e: Exception) {
                Flog.d(TAG, "读卡器格式设置失败")
//                        showToast("读卡器格式写入失败", Toast.LENGTH_SHORT)
            }
        }
        return false

    }


    private fun showStatue(text: String?, Code: Int): String? {
        var text = text
        var msg: String? = ""
        when (Code) {
            0x00 -> msg = "命令执行成功 ....."
            0x01 -> msg = "命令操作失败 ....."
            0x02 -> msg = "地址校验错误 ....."
            0x03 -> msg = "没有选择COM口 ....."
            0x04 -> msg = "读写器返回超时 ....."
            0x05 -> msg = "数据包流水号不正确 ....."
            0x07 -> msg = "接收异常 ....."
            0x0A -> msg = "参数值超出范围 ....."
            0x80 -> msg = "参数设置成功 ....."
            0x81 -> msg = "参数设置失败 ....."
            0x82 -> msg = "通讯超时....."
            0x83 -> msg = "卡不存在....."
            0x84 -> msg = "接收卡数据出错....."
            0x85 -> msg = "未知的错误....."
            0x87 -> msg = "输入参数或者输入命令格式错误....."
            0x89 -> msg = "输入的指令代码不存在....."
            0x8A -> msg = "在对于卡块初始化命令中出现错误....."
            0x8B -> msg = "在防冲突过程中得到错误的序列号....."
            0x8C -> msg = "密码认证没通过....."
            0x8F -> msg = "输入的指令代码不存在....."
            0x90 -> msg = "卡不支持这个命令....."
            0x91 -> msg = "命令格式有错误....."
            0x92 -> msg = "在命令的FLAG参数中，不支持OPTION 模式....."
            0x93 -> msg = "要操作的BLOCK不存在....."
            0x94 -> msg = "要操作的对象已经别锁定，不能进行修改....."
            0x95 -> msg = "锁定操作不成功....."
            0x96 -> msg = "写操作不成功....."
        }
        msg += '\n'
        text += msg
        return text
    }
}