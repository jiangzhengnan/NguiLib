package com.ng.ngcommon.util;

import android.util.Log;

/**
 * Created by jiangzn on 16/9/28.
 */
public class LogUtils {
    private static String TAG = "nangua";
    public static void d(String msg) {
        if (msg != null ) {
            Log.d(TAG,msg);
        }
    }
}
