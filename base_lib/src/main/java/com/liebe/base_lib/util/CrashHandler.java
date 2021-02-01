package com.liebe.base_lib.util;


import android.content.Intent;

import com.dating.app.lib.util.AppUtil;
import com.liebe.base_lib.BaseApplication;

/**
 * Crash信息收集器
 */
/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description Crash信息收集器
 * @Author LiangZe
 * @Date 2020/4/17
 * @Version 2.0
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler handler;
    private Thread.UncaughtExceptionHandler exceptionHandler;
    private String dir;

    public static void init(String dir) {
        if (null == handler) {
            handler = new CrashHandler(dir);
        }
    }

    private CrashHandler(String dir) {
        this.dir = dir;
        exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (null != ex) {
            Flog.f(BaseApplication.gContext, dir, ex);
            ex.printStackTrace();
            // 弹出默认的crash的对话框
//            exceptionHandler.uncaughtException(thread, ex);

            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            BaseApplication context = BaseApplication.gContext;
            final Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}