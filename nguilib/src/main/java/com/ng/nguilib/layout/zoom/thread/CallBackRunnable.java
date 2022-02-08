package com.ng.nguilib.layout.zoom.thread;

public abstract class CallBackRunnable<T> implements Runnable {

    private boolean mCancle;

    public CallBackRunnable() {

    }

    @Override
    public final void run() {
        if (!mCancle) {
            final T t = job();
            ThreadPoolUtil.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    afterJob(t);
                }
            });

        }

    }

    public abstract T job();


    /**
     * run on ui thread
     */
    public void afterJob(T t) {
    }


    public void cancle() {
        mCancle = true;
    }
}
