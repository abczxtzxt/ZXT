package com.liebe.base_comm.net

import com.google.gson.annotations.SerializedName

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description json解析（data）
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
class HttpResult<T> : BaseResult() {
    @SerializedName("Data")
    var Data: T? = null
}