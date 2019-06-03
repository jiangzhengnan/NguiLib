package com.ng.nguilib.utils

import android.content.Context

object DensityUtil {
    public fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}