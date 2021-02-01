package com.liebe.base_comm.net
/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 封装错误码
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
class ApiException : RuntimeException {

    var error: ErrorCode

    constructor(t: Throwable, error: ErrorCode) : super(t) {
        this.error = error
    }

    constructor(code: Int, message: String) : super(message) {
        error = ErrorCode.parseError(code, message)
    }

    constructor(error: ErrorCode) : super(error.message) {
        this.error = error
    }
}