package com.ng.nguilib.utils

import android.content.Context
import android.util.TypedValue

object DensityUtil {
    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    fun dip2pxFloat(context: Context, dipValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dipValue * scale + 0.5f
    }

    fun spTopx(context: Context, spVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.resources.displayMetrics).toInt()
    }
}