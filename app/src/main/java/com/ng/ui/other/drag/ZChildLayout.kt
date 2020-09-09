package com.ng.ui.other.drag

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.ng.ui.other.drag.ZLayoutStretchListener


/**
 * 描述:  可拖动layout
 * @author Jzn
 * @date 2020/9/8
 */
class ZChildLayout : ConstraintLayout {
    private var mCanLift = false
    private lateinit var mCallBack: ZLayoutStretchListener
    private var mIndex:Int = 0

    fun setCallBack(index:Int,callBack: ZLayoutStretchListener) {
        this.mCallBack = callBack
        this.mIndex = index
    }


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        //设置举起监听事件
        setOnLongClickListener {
            //ToastUtil.show(context, "拿起: $mIndex")
            mCanLift = true
            onLift(it)
            return@setOnLongClickListener false
        }



        setOnTouchListener(object : OnTouchListener {
            override fun onTouch(p0: View, motionEvent: MotionEvent): Boolean {
                if (mCallBack == null) {
                    return false
                }
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        mCallBack.onStartLift(motionEvent)
                    }
                    MotionEvent.ACTION_UP -> {
                        mCanLift = false
                        mCallBack.onFinishLift()

                    }
                }
                if (mCanLift) {
                    mCallBack.onLift(mIndex,this@ZChildLayout, motionEvent)
                }

                return false
            }

        })
    }

    private fun onLift(it: View) {
        //it.visibility = View.INVISIBLE

    }


    fun addLayout() {

    }

    fun removeLayout() {


    }

    fun showDirection() {

    }
}