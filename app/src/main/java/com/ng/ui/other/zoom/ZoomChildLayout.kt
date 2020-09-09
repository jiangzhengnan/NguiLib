package com.ng.ui.other.zoom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * 描述:
 * @author Jzn
 * @date 2020/9/9
 */
class ZoomChildLayout : ConstraintLayout {
    //子layout列表
    private var mChildLayoutList = arrayListOf<View>()


    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        initView()
    }

    private fun initView() {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val childCount = childCount
        mChildLayoutList.clear()
        for (i in 0 until childCount) {
            val childView: View = getChildAt(i)
            mChildLayoutList.add(childView)
            childView.measure(widthMeasureSpec, heightMeasureSpec)
        }

        mChildLayoutList.forEachIndexed { index, child ->
            //child.setCallBack(index, this)
        }
    }


}