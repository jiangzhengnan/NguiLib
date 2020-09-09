package com.ng.ui.other.drag

import android.view.MotionEvent
import android.view.View

/**
 * 描述:
 * @author Jzn
 * @date 2020/9/8
 */
interface ZLayoutStretchListener {

    fun onStartLift(motionEvent: MotionEvent)


    fun onLift(index:Int,view: View,motionEvent: MotionEvent)

    fun onFinishLift()
}