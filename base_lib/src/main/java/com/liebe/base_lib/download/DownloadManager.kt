package com.liebe.base_lib.download

import com.liebe.base_lib.util.Flog
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.*
import java.util.concurrent.Executors


/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 文件下载管理
 * @Author LiangZe
 * @Date 2020/5/19
 * @Version 2.0
 */
class DownloadManager {

    private val sBufferSize = 8192
    private var couldDownload: Boolean = true
    private var downloadFilePath: String = ""

    fun startDownload(
        downloadUrl: String,
        filePath: String,
        fileDownLoadListener: FileDownLoadListener
    ) {
        couldDownload = true
        downloadFilePath = filePath
        //对Url进行拆分
        val baseUrl = downloadUrl.substring(0, downloadUrl.lastIndexOf("/") + 1)

        val useUrl = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1, downloadUrl.length)
        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .callbackExecutor(Executors.newSingleThreadExecutor())
            .build()

        retrofit.create(DownloadServices::class.java).download(useUrl)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    fileDownLoadListener.onFail("文件下载失败:${t.message}")
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    writePesponseToDist(filePath, response, fileDownLoadListener)
                }

            })
    }


    fun stopDownload() {
        couldDownload = false
    }

    //response写入磁盘中，运行在子线程
    private fun writePesponseToDist(
        filePath: String,
        response: Response<ResponseBody>,
        fileDownLoadListener: FileDownLoadListener
    ) {
        response.body()?.let {
            witeFileFromIs(
                File(filePath),
                it.byteStream(),
                it.contentLength(),
                fileDownLoadListener
            )
        }
    }


    private fun witeFileFromIs(
        file: File,
        inputStream: InputStream,
        totalLength: Long,
        fileDownLoadListener: FileDownLoadListener
    ) {
        //开始下载
        fileDownLoadListener.start()

        if (!file.parentFile.exists()) file.parentFile.mkdirs()
        if (!file.exists()) file.createNewFile()

        var os: OutputStream? = null
        var currentLength = 0L

        try {
            os = BufferedOutputStream(FileOutputStream(file))
            val data = ByteArray(sBufferSize)
            var length = 0

            while (inputStream.read(data, 0, sBufferSize).also({
                    length = it
                }) != -1 && couldDownload) {
                os.write(data, 0, length)
                currentLength += length
                Flog.e("DownloadManager", "文件下载中:" + currentLength + "    " + totalLength)
                //下载进度
                fileDownLoadListener.opProgress((currentLength * 100 / totalLength).toInt())
            }

            //下载完成
            if (currentLength != totalLength) {
                //删除已下载的不完全文件
                file.delete()
                Flog.e("DownloadManager", "文件下载不完全，文件已删除")
            }else{
                fileDownLoadListener.onFinish(file.absolutePath)
            }

        } catch (e: IOException) {
            e.printStackTrace()
            fileDownLoadListener.onFail(e.message)
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            try {
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }
}