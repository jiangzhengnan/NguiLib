package com.ng.nguilib.layout.zoom.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadManager {

    //新起线程纳入线程池管理

    private static ThreadManager instance;

    /**
     * 返回线程池实例
     *
     * @return
     */
    public static ThreadManager getInstance() {

        if (instance == null) {
            synchronized (ThreadManager.class) {
                if (instance == null) {
                    instance = new ThreadManager();
                }
            }
        }
        return instance;
    }


    private static final int CORE_THREAD_SIZE = 0;
    private static final int MAX_THREAD_SIZE = 128;
    private static final long THREAD_KEEPALIVE = 2L;

    private ExecutorService executorService;

    private final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "ThreadManager Thread #" + mCount.getAndIncrement());
        }
    };

    private ThreadManager() {
        executorService = new ThreadPoolExecutor(CORE_THREAD_SIZE, MAX_THREAD_SIZE, THREAD_KEEPALIVE, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), sThreadFactory , new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            }
        });
    }


    /**
     * 在后台线程中执行一个Runnable
     *
     * @param command
     */
    public void execute( Runnable command) {

        executorService.execute(command);
    }

    public void newExecute( Runnable command) {

        ThreadPoolUtil.execute(new ProvityRunnable(command) {
            @Override
            public void job() {
            }
        });
        executorService.execute(command);
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
