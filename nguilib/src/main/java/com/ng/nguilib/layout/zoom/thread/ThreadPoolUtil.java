package com.ng.nguilib.layout.zoom.thread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {

    private final static int CORE_POOL_SIZE = 1;
    private final static int MAX_POOL_SIZE = 6;
    private final static int KEEP_ALIVE_TIME = 60;
    private static int sCorePoolSize = CORE_POOL_SIZE;

    private static AbstractThreadExecutor sExecutor;
    private static Handler sUiHandler = new Handler(Looper.getMainLooper());
    private static HandlerThread sHandlerThread = new HandlerThread("single-task-thread");
    private static Handler sWorkThreadHandle;

    static {
        sCorePoolSize = Runtime.getRuntime().availableProcessors() - 1;
        if (sCorePoolSize < CORE_POOL_SIZE) {
            sCorePoolSize = CORE_POOL_SIZE;
        }
        if (sCorePoolSize > MAX_POOL_SIZE) {
            sCorePoolSize = MAX_POOL_SIZE;
        }
        sHandlerThread.start();
        sWorkThreadHandle = new Handler(sHandlerThread.getLooper());
        sExecutor = new ThreadExecutor();
    }


    /**
     * post到线程池
     *
     * @param task
     */
    public static void execute(ProvityRunnable task) {
        sExecutor.execut(task);
    }

    /**
     * post到线程池
     *
     * @param task
     */
    public static void execute(CallBackRunnable task) {
        sExecutor.execut(task);
    }

    /**
     * UI线程
     *
     * @param task
     */
    public static void runOnUiThread(Runnable task) {
        sUiHandler.post(task);
    }

    public static void runOnUiThread(Runnable task, long delayMillis) {
        sUiHandler.postDelayed(task, delayMillis);
    }


    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    /**
     * 这个方法如果在主线程中调用就会直接执行，如果不是就会post到主线程
     *
     * @return
     */
    public static void  runOnMainThread(Runnable task) {
        if (isMainThread()) {
            task.run();
        } else {
            runOnUiThread(task);
        }
    }

    /**
     * 单个队列线程
     *
     * @param task
     */
    public static void runOnSequenceThread(ProvityRunnable task) {
        sWorkThreadHandle.post(task);
    }

    public static void cancle(Runnable task) {
        sUiHandler.removeCallbacks(task);
        sExecutor.cancle(task);
        sWorkThreadHandle.removeCallbacks(task);
    }

    public static class ThreadExecutor extends AbstractThreadExecutor {
        @Override
        public ThreadPool initThreadManager() {
            return ThreadPool.build("sdk-thread-pool", sCorePoolSize, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, false);
        }
    }

    public static ExecutorService getExecutorService() {
        return sExecutor.getThreadPool().getWorkPool();
    }
}
