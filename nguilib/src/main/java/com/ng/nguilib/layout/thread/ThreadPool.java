package com.ng.nguilib.layout.thread;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    private static ScheduledExecutorService sScheduledExecutorService = null; // 调度线程池
    private ThreadPoolExecutor mWorkPool = null; // 线程池
    private Byte[] mLock = new Byte[0];
    private String mName;
    private Queue<Runnable> mTasksQueue = new ConcurrentLinkedQueue<>(); // 等待任务队列
    private static HashMap<String, ThreadPool> sPoolMap = new HashMap<>();

    private ThreadPool(int corePoolSize,
                       int maximumPoolSize,
                       long keepAliveTime,
                       TimeUnit unit, boolean isPriority) {

        synchronized (ThreadPool.this) {
            if (sScheduledExecutorService == null) {
                sScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
                sScheduledExecutorService.scheduleAtFixedRate(new TaskRunable(), 0, 1500,
                        TimeUnit.MILLISECONDS);
            }
            BlockingQueue<Runnable> queue = isPriority ? new PriorityBlockingQueue<Runnable>(16)
                    : new LinkedBlockingQueue<Runnable>(16);
            mWorkPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, queue,
                    new TaskRejectedExecutionHandler());
        }
    }

    private class TaskRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            mTasksQueue.offer(r);
        }
    }

    public static ThreadPool build(String threadPoolName,
                                   int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                   boolean isPriority) {
        ThreadPool threadPoolManager = null;
        synchronized (sPoolMap) {
            threadPoolManager = sPoolMap.get(threadPoolName);
            if (threadPoolManager == null) {
                threadPoolManager = new ThreadPool(corePoolSize,
                        maximumPoolSize, keepAliveTime, unit, isPriority);
                threadPoolManager.mName = threadPoolName;
                sPoolMap.put(threadPoolName, threadPoolManager);

            }
        }
        return threadPoolManager;
    }

    private class TaskRunable implements Runnable {
        @Override
        public void run() {
            executeWaitTask();
        }
    }


    private void executeWaitTask() {
        synchronized (mLock) {
            if (nextTask()) {
                Runnable runnable = mTasksQueue.poll();
                if (runnable != null) {
                    execute(runnable);
                }
            }
        }
    }

    public void execute(Runnable task) {
        if (task != null) {
            mWorkPool.execute(task);
        }

    }

    private boolean nextTask() {
        return !mTasksQueue.isEmpty();
    }


    public void cancel(Runnable task) {
        if (task != null) {
            synchronized (mLock) {
                if (mTasksQueue.contains(task)) {
                    mTasksQueue.remove(task);
                }
            }
            mWorkPool.remove(task);
        }
    }

    public ThreadPoolExecutor getWorkPool() {
        return mWorkPool;
    }
}
