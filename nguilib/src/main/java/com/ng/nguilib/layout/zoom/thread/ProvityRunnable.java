package com.ng.nguilib.layout.zoom.thread;

import android.os.Process;
import android.text.TextUtils;


public abstract class ProvityRunnable implements Runnable, Comparable<ProvityRunnable> {
    private Runnable mExRunable;
    private int mProvity = Process.THREAD_PRIORITY_BACKGROUND;
    private String mName;
    private boolean mCancle;
    public ProvityRunnable(){

    }

    public ProvityRunnable(Runnable runnable){
        mExRunable = runnable;
    }

    public ProvityRunnable(String name){
        mName = name;
    }


    @Override
    public int compareTo(ProvityRunnable another) {
        return this.mProvity > another.mProvity ? 1
                : this.mProvity < another.mProvity ? -1 : 0;
    }

    @Override
    public final void run() {
    	
        Process.setThreadPriority(mProvity);
        if (!TextUtils.isEmpty(mName)) {
            Thread.currentThread().setName(mName);
        }
        
        Process.setThreadPriority(this.mProvity);
        if(!TextUtils.isEmpty(this.mName)) {
            Thread.currentThread().setName(this.mName);
        }
        if(!mCancle){
        	 job();
        	 if(mExRunable != null){
        	     mExRunable.run();
             }
        	 ThreadPoolUtil.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					afterJob();
				}
			});
             
    	}
       
    }

    public abstract void job();

    public int getProvity() {
        return mProvity;
    }

    /**
     * run on ui thread
     */
    public void afterJob() {
    }


    public ProvityRunnable buildProvity(int provity) {
        mProvity = provity;
        return this;
    }

    public ProvityRunnable buildName(String name) {
        mName = name;
        return this;
    }

    public String getName() {
        return mName;
    }
    
    public void cancle(){
    	mCancle = true;
    }
}
