package com.liebe.base_comm.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nanchen.compresshelper.CompressHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description TODO
 * @Author Kong
 * @Date 2020/9/3
 * @Version 2.0
 */
public class BitmapUtil {

    //单位是KB
    private static final long sizeLimit = 300;

    /**
     * @param path 绝对路径
     * @return
     */
    public static Bitmap compressBitmap(String path) {
        File file = new File(path);
        long length = file.length();
        //小于临界值的时候直接返回不做处理
        //length 单位是B
        if (length <= sizeLimit * 1024) {
            return BitmapFactory.decodeFile(path);
        }
        File newFile = CompressHelper.getDefault(UiUtil.INSTANCE.getContext()).compressToFile(file);

        return BitmapFactory.decodeFile(newFile.getAbsolutePath());

//        //1920 * 1080
//        Bitmap bitmap = decodeSampledBitmapFromResource(path, 480, 270);
//        //旋转
//        bitmap = adjustPhotoRotation(bitmap, 90);
//
//        return compressBitmap(bitmap, sizeLimit);
    }

    /**
     * 压缩图片
     *
     * @param bitmap    被压缩的图片
     * @param sizeLimit 大小限制
     * @return 压缩后的图片
     */
    private static Bitmap compressBitmap(Bitmap bitmap, long sizeLimit) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);

        // 循环判断压缩后图片是否超过限制大小
        while (baos.toByteArray().length / 1024 > sizeLimit) {
            // 清空baos
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 10;
        }

        Bitmap newBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()), null, null);

        bitmap.recycle();

        return newBitmap;
    }

    private static Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 先将inJustDecodeBounds设置为true不会申请内存去创建Bitmap，返回的是一个空的Bitmap，但是可以获取
        //图片的一些属性，例如图片宽高，图片类型等等。
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // 计算inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // 加载压缩版图片
        options.inJustDecodeBounds = false;
        // 根据具体情况选择具体的解码方法
        return BitmapFactory.decodeFile(path, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 原图片的宽高
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // 计算inSampleSize值
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    /*
     *Bitmap转byte数组
     */
    public static byte[] BitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

}
