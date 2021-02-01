package com.liebe.base_comm

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description TODO
 * @Author LiangZe
 * @Date 2020/4/20
 * @Version 2.0
 */
interface AppSetting {
    companion object {

        //用户支付间隔时间
        var userPayIntervalDefalut = 3 * 1000L

        //刷卡器支持位数
        var cardLength = 8

        /*************sp存储KEY*************/
        //用户登录标识（未设计TOKEN）
        val user_login: String = "user_login"

        //用户打开语音播报标识
        val user_play_music: String = "user_play_music"

        //SiteID
        val siteId: String = "siteId"

        //token
        val token: String = "token"

        //当前管理员ID
        val manager_id = "manager_id"

        //当前管理员名称
        val manager_name = "manager_name"

        //是否打开消费限制
        val open_pay_limit = "open_pay_limit"

        //用户消费时间间隔
        var userPayInterval = "userPayInterval"

        //是否打开继电器开关
        val chopsticksMachine = "chopsticksMachine"

        //是否成功激活人脸识别
        val activeFace = "activeFace"

        //是否是第一次登陆
        val isFirstLogin = "isFirstLogin"

        //是否显示余额与消费金额
        val showPayAndBanlance = "showPayAndBanlance"

        //人脸识别距离
        val distanceFace="distanceFace"
        //是否活体检测
        val isLiveness="isLiveness"

        //消费方式设置
        val consumptionSetting = "consumptionSetting"
        val eatCount = "eatCount"

        //激活可用时间
        val activeTime = "activeTime"

        /*************sp存储KEY*************/

        val defalut_token = "Basic MTIzOmxpZWJlVGVzdFBj"

        //扫码支付 通讯地址
        val qrCodeHost: String = "47.103.211.189"
        val qrCodePort: Int = 8041

        //串口波特率
        var baudrate = 115200

        //输入串口地址
        var seriaPort = "/dev/ttyS1"

        //串口读卡器地址
        var cardSeriaPort = "/dev/ttyS3"

        //继电器端口号
        val GPIO_PORT = 229

        //继电器高电平
        val RELAY_HIGH_LEVEL = 1

        //继电器低电平
        val RELAY_LOWER_LEVEL = 0

        /**
         * 扫码
         */
        var canUseQRCode = false

        //是否开启双目摄像头
        var isUserIrCamera = false

        //是否使用usb读卡器
        var isUseUsbReadCard = true

        var isConnectMachine = true

        var isActive = true

        //是否等待出餐盘状态 出盘中  为 true  就不可以出盘
//        @Volatile
        var isWaitForPlate = false
            @Synchronized
            get
            @Synchronized
            set
    }
}