package com.liebe.base_comm.net

import com.google.gson.annotations.SerializedName

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 基础格式
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
open class BaseResult {
    @SerializedName("StateCode")
    var StateCode: Int = 0
    @SerializedName("StateInfo")
    var StateInfo: String = ""

    fun isSuccess(): Boolean {
        return StateCode == 200
    }
}