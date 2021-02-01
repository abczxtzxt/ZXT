package com.liebe.base_lib.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.dating.app.lib.util.AppUtil;
import com.dating.app.lib.util.Storage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 日志工具类
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
public final class Flog {

    private static final String DEFAULT_TAG = "Flog";

    private static final int TYPE_V = 0x01;
    private static final int TYPE_D = TYPE_V << 1;
    private static final int TYPE_I = TYPE_D << 1;
    private static final int TYPE_W = TYPE_I << 1;
    private static final int TYPE_E = TYPE_W << 1;

    private static final SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);

    private Flog() {
    }

    //*******************Log with ERROR*******************/
    public static void e(String msg) {
        log(DEFAULT_TAG, msg, TYPE_E);
    }

    public static void e(Exception e) {
        log(DEFAULT_TAG, Log.getStackTraceString(e), TYPE_E);
    }

    public static void e(Throwable tr) {
        log(DEFAULT_TAG, Log.getStackTraceString(tr), TYPE_E);
    }

    public static void e(String tag, String msg) {
        log(tag, msg, TYPE_E);
    }

    public static void e(String tag, Exception e) {
        log(tag, Log.getStackTraceString(e), TYPE_E);
    }

    public static void e(String tag, Throwable tr) {
        log(tag, Log.getStackTraceString(tr), TYPE_E);
    }

    public static void e(Object tag, String msg) {
        log(tag.getClass().getSimpleName(), msg, TYPE_E);
    }

    public static void e(Object tag, Exception e) {
        log(tag.getClass().getSimpleName(), Log.getStackTraceString(e), TYPE_E);
    }

    public static void e(Object tag, Throwable tr) {
        log(tag.getClass().getSimpleName(), Log.getStackTraceString(tr), TYPE_E);
    }

    public static void e(String tag, String msg, Throwable tr) {
        log(tag, msg + '\n' + Log.getStackTraceString(tr), TYPE_E);
    }

    //*******************Log with WARNING*******************/
    public static void w(String msg) {
        log(DEFAULT_TAG, msg, TYPE_W);
    }

    public static void w(Exception e) {
        log(DEFAULT_TAG, Log.getStackTraceString(e), TYPE_W);
    }

    public static void w(Throwable tr) {
        log(DEFAULT_TAG, Log.getStackTraceString(tr), TYPE_W);
    }

    public static void w(String tag, String msg) {
        log(tag, msg, TYPE_W);
    }

    public static void w(String tag, Exception e) {
        log(tag, Log.getStackTraceString(e), TYPE_W);
    }

    public static void w(String tag, Throwable tr) {
        log(tag, Log.getStackTraceString(tr), TYPE_W);
    }

    public static void w(Object tag, String msg) {
        log(tag.getClass().getSimpleName(), msg, TYPE_W);
    }

    public static void w(Object tag, Exception e) {
        log(tag.getClass().getSimpleName(), Log.getStackTraceString(e), TYPE_W);
    }

    public static void w(Object tag, Throwable tr) {
        log(tag.getClass().getSimpleName(), Log.getStackTraceString(tr), TYPE_W);
    }

    public static void w(String tag, String msg, Throwable tr) {
        log(tag, msg + '\n' + Log.getStackTraceString(tr), TYPE_W);
    }

    //*******************Log with INFO*******************/
    public static void i(String msg) {
        log(DEFAULT_TAG, msg, TYPE_I);
    }

    public static void i(String tag, String msg) {
        log(tag, msg, TYPE_I);
    }

    public static void i(Object tag, String msg) {
        log(tag.getClass().getSimpleName(), msg, TYPE_I);
    }

    //*******************Log with DEBUG*******************/
    public static void d(String msg) {
        log(DEFAULT_TAG, msg, TYPE_D);
    }

    public static void d(String tag, String msg) {
        log(tag, msg, TYPE_D);
    }

    public static void d(Object tag, String msg) {
        log(tag.getClass().getSimpleName(), msg, TYPE_D);
    }

    //*******************Log with VERBOSE*******************/
    public static void v(String msg) {
        log(DEFAULT_TAG, msg, TYPE_V);
    }

    public static void v(String tag, String msg) {
        log(tag, msg, TYPE_V);
    }

    public static void v(Object tag, String msg) {
        log(tag.getClass().getSimpleName(), msg, TYPE_V);
    }

    //*******************Log with FILE*******************/
    public static void f(final Context context,final  String dir,final  Throwable tr) {
        new Thread() {
            @Override
            public void run() {
                write(context, dir, tr);
            }
        }.start();
    }

    private static void log(String tag, String msg, int logType) {
        if (!AppUtil.INSTANCE.isDebug()) {
            StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[4];
            String fileInfo = "[" + stackTrace.getFileName() + "(" + stackTrace.getLineNumber() + ") " + stackTrace.getMethodName() + "] ";

            msg = fileInfo + msg;

            int index = 0;
            int maxLength = 4000;
            int countOfSub = msg.length() / maxLength;
            if (countOfSub > 0) {
                for (int i = 0; i < countOfSub; i++) {
                    String sub = msg.substring(index, index + maxLength);
                    print(tag, sub, logType);
                    index += maxLength;
                }
                print(tag, msg.substring(index, msg.length()), logType);
            } else {
                print(tag, msg, logType);
            }
        }
    }

    private static void print(String tag, String msg, int logType) {
        switch (logType) {
            case TYPE_V:
                Log.v(tag, msg);
                break;

            case TYPE_D:
                Log.d(tag, msg);
                break;

            case TYPE_I:
                Log.i(tag, msg);
                break;

            case TYPE_W:
                Log.w(tag, msg);
                break;

            case TYPE_E:
                Log.e(tag, msg);
                break;

            default:
                break;
        }
    }

    private static void write(Context context, String dir, Throwable ex) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : obtainSimpleInfo(context).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }
        sb.append(obtainExceptionInfo(ex));

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String fileName = dir + sFormat.format(new Date()) + ".txt";
            Storage.INSTANCE.saveFile(fileName, sb.toString());
        }
    }

    /**
     * 获取系统未捕捉的错误信息
     *
     * @param throwable
     * @return
     */
    private static String obtainExceptionInfo(Throwable throwable) {
        if (throwable == null) return "";
        StringWriter writer = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(writer);
        throwable.printStackTrace(mPrintWriter);
        mPrintWriter.close();
        return writer.toString();
    }

    /**
     * 获取一些简单的信息,软件版本，手机版本，型号等信息存放在HashMap中
     */
    private static HashMap<String, String> obtainSimpleInfo(Context context) {
        HashMap<String, String> map = new HashMap<>();
        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (mPackageInfo != null) {
            map.put("versionName", mPackageInfo.versionName);
            map.put("versionCode", "" + mPackageInfo.versionCode);
        }
        map.put("MODEL", "" + Build.MODEL);
        map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
        map.put("PRODUCT", "" + Build.PRODUCT);
        return map;
    }
}
