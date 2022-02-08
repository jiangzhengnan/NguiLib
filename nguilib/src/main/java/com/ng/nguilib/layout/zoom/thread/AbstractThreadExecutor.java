package com.ng.nguilib.layout.zoom.thread;


public abstract class AbstractThreadExecutor {
    private ThreadPool mPool;
    private Object mLock = new Object();

    public abstract ThreadPool initThreadManager();

    public void execut(Runnable task) {
        synchronized (mLock) {
            if (mPool == null) {
                mPool = initThreadManager();
            }
        }
        mPool.execute(task);
    }

    public void cancle(Runnable task){
        if(mPool != null)
        mPool.cancel(task);
    }

    public ThreadPool getThreadPool(){
        synchronized (mLock) {
            if (mPool == null) {
                mPool = initThreadManager();
            }
        }
        return mPool;
    }
}
