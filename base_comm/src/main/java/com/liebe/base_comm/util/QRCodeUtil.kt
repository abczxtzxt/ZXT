package com.liebe.base_comm.util

import android.graphics.Bitmap
import android.graphics.Matrix
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.*


/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 二维码生成
 * @Author LiangZe
 * @Date 2020/5/20
 * @Version 2.0
 */
object QRCodeUtil {
    private var IMAGE_HALFWIDTH = 50

    /**
     * 生成二维码
     *
     * @param text 文字或网址
     * @param size 生成二维码的大小
     * @return bitmap
     */
    fun createQRCode(text: String?, size: Int=500): Bitmap? {
        return try {
            val hints: Hashtable<EncodeHintType, String> = Hashtable()
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8")
            val bitMatrix = QRCodeWriter().encode(
                text,
                BarcodeFormat.QR_CODE, size, size, hints
            )
            val pixels = IntArray(size * size)
            for (y in 0 until size) {
                for (x in 0 until size) {
                    if (bitMatrix[x, y]) {
                        pixels[y * size + x] = -0x1000000
                    } else {
                        pixels[y * size + x] = -0x1
                    }
                }
            }
            val bitmap = Bitmap.createBitmap(
                size, size,
                Bitmap.Config.ARGB_8888
            )
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size)
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 生成带logo的二维码
     * @param text
     * @param size
     * @param mBitmap
     * @return
     */
    fun createQRCodeWithLogo(text: String?, size: Int, mBitmap: Bitmap): Bitmap? {
        var mBitmap = mBitmap
        return try {
            IMAGE_HALFWIDTH = size / 10
            val hints: Hashtable<EncodeHintType, Any> = Hashtable()
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8")
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H)
            val bitMatrix = QRCodeWriter().encode(
                text,
                BarcodeFormat.QR_CODE, size, size, hints
            )
            //将logo图片按martix设置的信息缩放
            mBitmap = Bitmap.createScaledBitmap(mBitmap, size, size, false)
            val width = bitMatrix.getWidth() //矩阵高度
            val height = bitMatrix.getHeight() //矩阵宽度
            val halfW = width / 2
            val halfH = height / 2
            val m = Matrix()
            val sx = 2.toFloat() * IMAGE_HALFWIDTH / mBitmap.width
            val sy = (2.toFloat() * IMAGE_HALFWIDTH
                    / mBitmap.height)
            m.setScale(sx, sy)
            //设置缩放信息
            //将logo图片按martix设置的信息缩放
            mBitmap = Bitmap.createBitmap(
                mBitmap, 0, 0,
                mBitmap.width, mBitmap.height, m, false
            )
            val pixels = IntArray(size * size)
            for (y in 0 until size) {
                for (x in 0 until size) {
                    if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH && y > halfH - IMAGE_HALFWIDTH && y < halfH + IMAGE_HALFWIDTH
                    ) { //该位置用于存放图片信息
                        //记录图片每个像素信息
                        pixels[y * width + x] = mBitmap.getPixel(
                            x - halfW
                                    + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH
                        )
                    } else {
                        if (bitMatrix[x, y]) {
                            pixels[y * size + x] = -0xc84e62
                        } else {
                            pixels[y * size + x] = -0x1
                        }
                    }
                }
            }
            val bitmap = Bitmap.createBitmap(
                size, size,
                Bitmap.Config.ARGB_8888
            )
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size)
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }
}