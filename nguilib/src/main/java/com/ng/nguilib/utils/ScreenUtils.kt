package com.ng.nguilib.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * 描述:
 * @author Jzn
 * @date 2020/11/6
 */
object ScreenUtils {
    /**
     * 获取屏幕宽度 非Activity中
     *
     * @param context
     * @return
     */
    fun getWidth(context: Context): Int {
        val dm = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }
}