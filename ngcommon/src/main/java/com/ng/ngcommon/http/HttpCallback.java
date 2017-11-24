package com.ng.ngcommon.http;

import android.content.Context;

import okhttp3.Request;

/**
 * Created by hd_01 on 2016/10/4.
 */

public abstract class HttpCallback {
    /**
     * UI Thread
     *
     * @param request
     */
    public void onBefore(Request request) {

    }

    /**
     * UI Thread
     *
     * @param
     */
    public void onAfter() {
    }

    /**
     * UI Thread
     *
     * @param progress
     */
    public void inProgress(float progress) {

    }

    public void onError(Request request, Exception e, Context context){

    }

    public abstract void onResponse(String response);

}
