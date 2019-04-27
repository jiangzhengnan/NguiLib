package com.ng.ui;

import android.util.Log;

public class LogUtils {
    private static String TAG = "pumpkin";
    public static void d(String msg) {
        if (msg != null ) {
            Log.d(TAG,msg);
        }
    }
}
