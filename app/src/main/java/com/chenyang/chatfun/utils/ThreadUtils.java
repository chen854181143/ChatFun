package com.chenyang.chatfun.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by fullcircle on 2016/12/31.
 */

public class ThreadUtils {
    //使用主线程的looper创建handler 这个handler一定执行在主线程的
    private static Handler handler = new Handler(Looper.getMainLooper());

    //单线程的线程池
   private static Executor executor = Executors.newSingleThreadExecutor();

    /**
     * 在子线程中执行代码
     * @param r
     */
    public static void runOnNonUIThread(final Runnable r){
    executor.execute(r);
    }

    /**
     * 在主线程中执行代码
     * @param r
     */
    public static void runOnMainThread(Runnable r){
        handler.post(r);
    }
}
