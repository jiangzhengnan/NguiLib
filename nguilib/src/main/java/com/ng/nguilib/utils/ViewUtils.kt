package com.ng.nguilib.utils

import android.content.Context

/**
 * 描述:
 * @author Jzn
 * @date 2020/11/9
 */
object ViewUtils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context?, dpValue: Float): Int {
        val scale = context?.resources?.displayMetrics?.density
        return (dpValue * scale!! + 0.5f).toInt()
    }

}