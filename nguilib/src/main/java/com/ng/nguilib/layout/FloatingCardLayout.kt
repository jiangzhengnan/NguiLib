package com.ng.nguilib.layout

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.ng.nguilib.utils.ViewUtils


/**
 *    @author : jiangzhengnan.jzn@alibaba-inc.com
 *    @creation   : 2022/02/01
 *    @description   :
 */
class FloatingCardLayout : RelativeLayout {

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    //data
    var mDataList: List<FloatingCardItem> = arrayListOf()

    //view
    var mViewList: ArrayList<View> = arrayListOf()

    //卡片宽度
    private var mItemCardWidth = 0

    //卡片高度
    private var mItemCardHeight = 0

    //卡片翻转角度
    private var mItemCardRotationX = 0f

    //底部卡片间距
    private var mItemCardInterval = 0

    private var LAYOUT_MARGIN = 0

    init {
        LAYOUT_MARGIN = ViewUtils.dip2px(context, 16f)
        mItemCardInterval = ViewUtils.dip2px(context, 50f)
        mItemCardWidth = ViewUtils.dip2px(context, 250f)
        mItemCardRotationX = 40.0f
        mItemCardHeight = mItemCardWidth * 3 / 5
    }

    fun setData(dataList: List<FloatingCardItem>) {
        this.mDataList = dataList
        refreshLayout()
    }

    fun slideUp() {

    }

    fun slideDown() {

    }


    private fun refreshLayout() {
        if (mDataList.isEmpty()) {
            return
        }
        //后面的叠起来
        for (index in mDataList.size - 1 downTo 1) {
            val itemView = FloatingCardView(context)
            itemView.setBackgroundResource(mDataList[index].imgRes)
            val tempLp = LayoutParams(mItemCardWidth * 5 / 7, mItemCardHeight)
            tempLp.topMargin = LAYOUT_MARGIN + mItemCardHeight + (index - 1) * mItemCardInterval
            tempLp.addRule(CENTER_HORIZONTAL)
            addView(itemView, tempLp)
            itemView.rotationX = mItemCardRotationX
            itemView.alpha = 0.8f
            mViewList.add(itemView)
        }

        //第一个
        val itemIndex0 = FloatingCardView(context)
        itemIndex0.setBackgroundResource(mDataList[0].imgRes)
        val lp = LayoutParams(mItemCardWidth, mItemCardHeight)
        lp.topMargin = LAYOUT_MARGIN
        lp.bottomMargin = LAYOUT_MARGIN
        lp.addRule(CENTER_HORIZONTAL)
        addView(itemIndex0, lp)
        mViewList.add(itemIndex0)

    }

    class FloatingCardView(context: Context?) : FrameLayout(context!!) {
    }

    class FloatingCardItem(var imgRes: Int) {
        var imgUrl: String = ""
    }
}