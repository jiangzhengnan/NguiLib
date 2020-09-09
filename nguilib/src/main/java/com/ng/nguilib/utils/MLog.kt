package com.ng.nguilib.utils

import android.util.Log

/**
 * @ProjectName: NguiLib
 * @Package: com.ng.nguilib.utils
 * @Description:
 * @Author: Eden
 * @CreateDate: 2019/6/15 11:45
 */
object MLog {
    private val TAG = "nangua"
    fun  d(msg: String?) {
        if (msg != null) {
            Log.d(TAG, msg)
        }
    }
}