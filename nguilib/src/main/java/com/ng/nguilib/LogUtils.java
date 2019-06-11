package com.ng.nguilib;

import android.util.Log;

public class LogUtils {
    private static String TAG = "nangua";
    public static void d(String msg) {
        if (msg != null ) {
            Log.d(TAG,msg);
        }
    }
}
