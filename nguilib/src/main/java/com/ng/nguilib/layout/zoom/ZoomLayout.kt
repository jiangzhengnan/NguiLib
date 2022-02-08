package com.ng.nguilib.layout.zoom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.ng.nguilib.R

/**
 * 描述: 普通联动滑动版本
 * @author Jzn
 * @date 2020/9/9
 */
class ZoomLayout constructor(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    //子layout列表
    private var mChildLayoutList: ArrayList<View> = arrayListOf()
    private var mIntervalList: ArrayList<View> = arrayListOf()

    //起始点位置
    private var mStartX = 0f
    private var mStartY = 0f

    //位移
    private var mIntervalX = 0f
    private var mIntervalY = 0f

    //分割线是否添加过
    private var hadAdd = false

    //保存每个子view的宽度
    private var mChildWidthList: ArrayList<Int> = arrayListOf()
    private var mChildHeightList: ArrayList<Int> = arrayListOf()

    //变化中的子view宽度
    private var mRunningXList: ArrayList<Int> = arrayListOf()
    private var mRunningYList: ArrayList<Int> = arrayListOf()


    //params
    private var mIntervalLineWidth = 1
    private var mIntervalLineColor = 1

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ZoomLayout)
        mIntervalLineWidth = context.resources.getDimensionPixelOffset(ta.getResourceId(R.styleable.ZoomLayout_IntervalLineWidth, R.dimen.dd10))
        mIntervalLineColor = ta.getColor(R.styleable.ZoomLayout_IntervalLineColor, Color.BLACK)
        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (mChildLayoutList.size != childCount) {
            refreshChildList()
            refreshChildSizeList()
            addSplit()
        }

        val maxSize = if (measuredHeight > measuredWidth) measuredHeight else measuredWidth
        //修正分割线宽度
        mIntervalList.forEachIndexed { _, child ->
            val lp: ViewGroup.LayoutParams = child.layoutParams
            if (orientation == HORIZONTAL) {
                lp.height = maxSize
            } else if (orientation == VERTICAL) {
                lp.width = maxSize
            }
            child.layoutParams = lp
        }
    }

    //刷新子view数组
    private fun refreshChildList() {
        mChildLayoutList.clear()
        for (i in 0 until childCount) {
            val childView: View = getChildAt(i)
            mChildLayoutList.add(childView)
        }
    }

    //刷新子view size
    private fun refreshChildSizeList() {
        mChildWidthList.clear()
        mChildLayoutList.forEachIndexed { _, child ->
            mChildWidthList.add(child.measuredWidth)
        }
        mRunningXList = mChildWidthList
        mChildHeightList.clear()
        mChildLayoutList.forEachIndexed { _, child ->
            mChildHeightList.add(child.measuredHeight)
        }
        mRunningYList = mChildHeightList
    }

    //在子view中设置操作分割线
    private fun addSplit() {
        if (mChildLayoutList.size == childCount && !hadAdd) {
            //在子view的间距中添加操作view
            mChildLayoutList.forEachIndexed { index, child ->
                if (index < mChildLayoutList.size - 1) {
                    addIntervalLine(index, child)
                }
            }
            hadAdd = true
        }
    }

    //增加垂直分割线
    private fun addIntervalLine(number: Int, child: View) {
        val interValView = View(context)
        interValView.setBackgroundColor(mIntervalLineColor)
        var lp: ViewGroup.LayoutParams = LayoutParams(measuredWidth, mIntervalLineWidth)
        if (orientation == HORIZONTAL) {
            lp = LayoutParams(mIntervalLineWidth, ViewGroup.LayoutParams.MATCH_PARENT)
        } else if (orientation == VERTICAL) {
            lp = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mIntervalLineWidth)
        }
        interValView.layoutParams = lp

        val realIndex = 1 + number * 2

        interValView.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    mStartX = motionEvent.x
                    mStartY = motionEvent.y

                    refreshChildSizeList()

                    if (parent != null) {
                        //防止事件被父布局拦截
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                MotionEvent.ACTION_UP -> {
                    refreshChildSizeList()
                    view.performClick()
                }
                MotionEvent.ACTION_MOVE -> {
                    mIntervalX = mStartX - motionEvent.x
                    mIntervalY = mStartY - motionEvent.y
                    if (orientation == HORIZONTAL) {
                        if (isChildValueLegal(mRunningXList[realIndex - 1] - mIntervalX.toInt(), realIndex - 1) &&
                                isChildValueLegal(mRunningXList[realIndex + 1] + mIntervalX.toInt(), realIndex + 1)
                        ) {
                            mRunningXList[realIndex - 1] -= mIntervalX.toInt()
                            mRunningXList[realIndex + 1] += mIntervalX.toInt()
                        }
                        // 联动调整左边
                        if (!isChildValueLegal(mRunningXList[realIndex - 1] - mIntervalX.toInt(), realIndex - 1)) {
                            gravity = Gravity.START
                            var fixMulti = 0
                            if (realIndex - 2 > 0) {
                                for (index in 0..realIndex - 2) {
                                    //这里要判断是否是分割线
                                    if (index % 2 == 0 && isChildValueLegal(mRunningXList[index] - mIntervalX.toInt(), index)) {
                                        mRunningXList[index] -= mIntervalX.toInt()
                                        fixMulti++
                                    }
                                }
                                mRunningXList[realIndex + 1] += mIntervalX.toInt() * fixMulti
                            }
                        }
                        //联动调整右边
                        if (!isChildValueLegal(mRunningXList[realIndex + 1] + mIntervalX.toInt(), realIndex + 1)) {
                            gravity = Gravity.END
                            var fixMulti = 0
                            for (index in (realIndex + 2) until childCount) {
                                if (index % 2 == 0 && isChildValueLegal(mRunningXList[index] + mIntervalX.toInt(), index)) {
                                    mRunningXList[index] += mIntervalX.toInt()
                                    fixMulti++
                                }
                            }
                            mRunningXList[realIndex - 1] -= mIntervalX.toInt() * fixMulti
                        }
                    } else if (orientation == VERTICAL) {
                        if (isChildValueLegal(mRunningYList[realIndex - 1] - mIntervalY.toInt(), realIndex - 1) &&
                                isChildValueLegal(mRunningYList[realIndex + 1] + mIntervalY.toInt(), realIndex + 1)) {
                            mRunningYList[realIndex - 1] -= mIntervalY.toInt()
                            mRunningYList[realIndex + 1] += mIntervalY.toInt()
                        }
                        // 联动调整上面
                        if (!isChildValueLegal(mRunningYList[realIndex - 1] - mIntervalY.toInt(), realIndex + 1)) {
                            gravity = Gravity.TOP
                            var fixMulti = 0
                            if (realIndex - 2 > 0) {
                                for (index in 0..realIndex - 2) {
                                    if (index % 2 == 0 && isChildValueLegal(mRunningYList[index] - mIntervalY.toInt(), index)) {
                                        mRunningYList[index] -= mIntervalY.toInt()
                                        fixMulti++
                                    }
                                }
                                mRunningYList[realIndex + 1] += mIntervalY.toInt() * fixMulti
                            }
                        }
                        // 联动调整下面
                        if (!isChildValueLegal(mRunningYList[realIndex + 1] + mIntervalY.toInt(), realIndex + 1)) {
                            gravity = Gravity.BOTTOM
                            var fixMulti = 0
                            for (index in (realIndex + 2) until childCount) {
                                if (index % 2 == 0 && isChildValueLegal(mRunningYList[index] + mIntervalY.toInt(), index)) {
                                    mRunningYList[index] += mIntervalY.toInt()
                                    fixMulti++
                                }
                            }
                            mRunningYList[realIndex - 1] -= mIntervalY.toInt() * fixMulti
                        }
                    }
                    mChildLayoutList.forEachIndexed { index, child ->
                        val childLp: LayoutParams = child.layoutParams as LayoutParams
                        //childLp.weight = 0f
                        if (orientation == HORIZONTAL) {
                            childLp.width = mRunningXList[index]

                        } else if (orientation == VERTICAL) {
                            childLp.height = mRunningYList[index]
                        }

                        child.layoutParams = childLp
                    }
                    //防止左越界
                    if (mChildLayoutList.size != 0) {
                        mChildLayoutList[0].x = 0f
                    }
                }
            }
            true
        }
        mIntervalList.add(interValView)
        addView(interValView, realIndex, lp)
    }

    private fun isChildValueLegal(value: Int, index: Int): Boolean {
        val minZoom = if (orientation == HORIZONTAL) {
            mChildLayoutList[index].minimumWidth
        } else {
            mChildLayoutList[index].minimumHeight
        }
        return value > minZoom
    }

}