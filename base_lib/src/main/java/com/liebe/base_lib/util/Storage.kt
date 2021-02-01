package com.dating.app.lib.util

import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import com.liebe.base_lib.BaseApplication
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 文件存储工具
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
object Storage {


    private const val TAG = "Storage"

    private val DIR_HOME_HIDE =
        BaseApplication.gContext.getFilesDir().getAbsolutePath() + File.separator

    private val DIR_HOME_EXTERNAL =
        Environment.getExternalStorageDirectory()
            .toString() + File.separator + "LIEBE_IDPT" + File.separator

    private val DIR_IMAGE = DIR_HOME_HIDE + "image" + File.separator              // 图片临时文件目录

    private val DIR_CRASH =
        DIR_HOME_EXTERNAL + "crash" + File.separator                        // Crash日志目录

    private val DIR_LOG =
        DIR_HOME_EXTERNAL + "log" + File.separator                            // 日志收集文件目录

    private val DIR_FILE =
        DIR_HOME_EXTERNAL + "file" + File.separator                            // 文件下载目录

    private val DIR_FACE =
        DIR_HOME_HIDE + "face" + File.separator                                 //人脸识别存储目录

    private val DIR_ADVERTISEMENT = DIR_HOME_EXTERNAL + "ad" + File.separator    //广告存储目录

    private const val TYPE_APP_HOME = 0

    const val TYPE_IMAGE = 1
    const val TYPE_CRASH = 2
    const val TYPE_FILE = 4
    const val TYPE_FACE = 5
    const val TYPE_ADVERTISEMENT = 8

    fun getPath(type: Int): String {
        var dir = ""

        when (type) {
            TYPE_APP_HOME -> {
                dir = createDir(DIR_HOME_HIDE)
                addNoMediaFile(DIR_HOME_HIDE)
            }

            TYPE_IMAGE -> {
                dir = createDir(DIR_IMAGE)
                addNoMediaFile(DIR_IMAGE)
            }

            TYPE_CRASH -> dir =
                createDir(DIR_CRASH)
            TYPE_FILE -> dir =
                createDir(DIR_FILE)
            TYPE_FACE -> dir =
                createDir(DIR_FACE)
            TYPE_ADVERTISEMENT -> dir =
                createDir(DIR_ADVERTISEMENT)
            else -> {
            }
        }

        return if (dir.endsWith(File.separator)) {
            dir
        } else {
            dir + File.separator
        }
    }

    private fun createDir(filePath: String): String {
        val file = File(filePath)
        if (!file.exists() || !file.isDirectory) {
            val b = file.mkdirs()
            Log.e("Storage", "创建 $filePath 文件夹 " + if (b) "成功" else "失败")
        }
        return filePath
    }

    fun saveImage(bitmap: Bitmap): String {
        val path = getPath(TYPE_IMAGE) + createJpgName()
        Log.e(TAG, "path==$path")
        saveImage(bitmap, path)
        return path
    }

    private fun saveImage(bitmap: Bitmap, path: String, quality: Int = 100) {
        val saveFile = File(path)
        var bos: BufferedOutputStream? = null
        try {
            bos = BufferedOutputStream(FileOutputStream(saveFile))
            val format =
                if (path.endsWith(".png")) Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.JPEG
            bitmap.compress(format, quality, bos)
            bos.flush()
            Log.e(TAG, "Success==")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG, "IOException==")
        } finally {
            try {
                bos?.close()
            } catch (e: IOException) {

            }
        }
    }

    /**
     * 添加.nomedia文件，避免指定目录下的图片和视频被系统扫描到图库中（也屏蔽了其他软件的扫描）
     *
     * @param path
     */
    private fun addNoMediaFile(path: String) {
        val file = File(path)
        try {
            if (!file.exists() || !file.isFile) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
        } catch (e: IOException) {

        }
    }

    private fun createJpgName(): String {
        return "IMG_" + System.currentTimeMillis() + ".jpg"
    }


    fun saveFile(fileName: String, content: String) {
        val file = File(fileName)
        val parent = file.parentFile
        var fos: FileOutputStream? = null
        try {
            if (!parent.exists()) parent.mkdirs()
            if (!file.exists()) file.createNewFile()
            fos = FileOutputStream(fileName)
            fos.write(content.toByteArray())
            fos.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
