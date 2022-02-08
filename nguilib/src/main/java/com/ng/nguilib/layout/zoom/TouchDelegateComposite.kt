package com.ng.nguilib.layout.zoom

import android.graphics.Rect
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View


/**
 * 描述:
 * @author Jzn
 * @date 2020/11/7
 */
class TouchDelegateComposite(view: View) : TouchDelegate(Rect(), view) {
    private val delegates = mutableListOf<TouchDelegate>()

    fun addDelegate(delegate: TouchDelegate) {
        delegates.add(delegate)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var res = false
        for (delegate in delegates) {
            event.setLocation(event.x, event.y)
            res = delegate.onTouchEvent(event) || res
        }
        return res
    }
}