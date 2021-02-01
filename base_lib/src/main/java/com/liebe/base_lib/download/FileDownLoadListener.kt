package com.liebe.base_lib.download

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 文件下载监听
 * @Author LiangZe
 * @Date 2020/5/19
 * @Version 2.0
 */
interface FileDownLoadListener {
    fun start()
    fun opProgress(progress: Int)
    fun onFinish(path: String)
    fun onFail(errorInfo: String?)
}