package com.ng.ui.other.drag

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View

/**
 * 描述:
 * @author Jzn
 * @date 2020/9/8
 */
object ViewUtils {

    fun getBitmapFromView(view: View): Bitmap {
        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null) //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas) else  //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.GRAY)
        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        return returnedBitmap
    }


    fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight: Int = getStatusBarByResId(context)
        if (statusBarHeight <= 0) {
            statusBarHeight = getStatusBarByReflex(context)
        }
        return statusBarHeight
    }

    /**
     * 通过状态栏资源id来获取状态栏高度
     *
     * @param context
     * @return
     */
    private fun getStatusBarByResId(context: Context): Int {
        var height = 0
        //获取状态栏资源id
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            try {
                height = context.resources.getDimensionPixelSize(resourceId)
            } catch (e: Exception) {
            }
        }
        return height
    }

    /**
     * 通过反射获取状态栏高度
     *
     * @param context
     * @return
     */
    private fun getStatusBarByReflex(context: Context): Int {
        var statusBarHeight = 0
        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val `object` = clazz.newInstance()
            val height = clazz.getField("status_bar_height")[`object`].toString().toInt()
            statusBarHeight = context.resources.getDimensionPixelSize(height)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return statusBarHeight
    }
}