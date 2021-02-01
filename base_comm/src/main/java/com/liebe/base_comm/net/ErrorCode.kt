package com.liebe.base_comm.net

import com.liebe.base_lib.util.Flog

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 错误码统一处理
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
enum class ErrorCode constructor( val code: Int, var message: String){

    ERROR_NETWORK(-100, "系统异常"),
    ERROR_PARSE(-101, "解析错误"),
    UNKNOWN(-102, "未知错误"),

    INVALID_SIGNATURE(-5, "校验码错误"),
    INVALID_SESSION(-4, "会话失效"),
    INVALID_TIMESTAMP(-3, "时间差异较大"),
    INVALID_NONCESTR(-2, "无效随机码"),
    SERVER_EXCEPTION(500, "服务器异常"),
    ERROR_TOKEN(400,"token失效"),
    ERROE_PERMISSION(401,"没有权限访问"),
    SUCCESS(200, "成功"),
    FAILURE(1, "失败"),
    FORBIDDEN(2, "无权操作"),
    FREQUENT(3, "操作频繁"),
    DUPLICATE(4, "重复操作"),
    NO_DATA(5, "数据不存在"),
    NO_QUOTA(6, "配额不足"),
    CUSTOM_SUCC(10, "操作成功，强制提示"),
    CUSTOM_FAIL(11, "操作失败，强制提示");

    override fun toString(): String {
        return "[$code:$message]"
    }

    companion object {
        /**
         * 将code 和message 转为 ErrorCode
         */
        fun parseError(code: Int, msg: String): ErrorCode {
            for (errorCode in ErrorCode.values()) {
                if (errorCode.code == code) {
                    return errorCode
                }
            }
            return UNKNOWN
        }

        fun handleCode(code: ErrorCode): Boolean {
            return when (code) {
                ERROR_NETWORK -> {
                    Flog.e("ErrorCode 需要用toast 处理")//TODO
                    true
                }
                ERROR_PARSE -> {
                    Flog.e("ErrorCode 需要用toast 处理")//TODO
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}