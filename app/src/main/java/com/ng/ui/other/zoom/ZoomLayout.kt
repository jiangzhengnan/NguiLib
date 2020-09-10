package com.ng.ui.other.zoom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.ng.ui.R

/**
 * 描述:
 * @author Jzn
 * @date 2020/9/9
 */
@Suppress("NAME_SHADOWING")
class ZoomLayout @SuppressLint("Recycle") constructor(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
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
    private var mMinZoom = 1
    private var mMaxZoom = 0

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ZoomLayout)
        mIntervalLineWidth = context.resources.getDimensionPixelOffset(ta.getResourceId(R.styleable.ZoomLayout_IntervalLineWidth, R.dimen.dd10))
        mIntervalLineColor = ta.getColor(R.styleable.ZoomLayout_IntervalLineColor, Color.BLACK)
        mMinZoom = context.resources.getDimensionPixelOffset(ta.getResourceId(R.styleable.ZoomLayout_MinZoom, R.dimen.dd01))
        //mMaxZoom = context.resources.getDimensionPixelOffset(ta.getResourceId(R.styleable.ZoomLayout_MaxZoom, R.dimen.dd00))
        if (mMaxZoom == 0) {
            mMaxZoom = Int.MAX_VALUE
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        refreshChildList()
        addSplit()
        refreshChildSizeList()
        //修正分割线宽度
        mIntervalList.forEachIndexed { _, child ->
            val lp: ViewGroup.LayoutParams = child.layoutParams
            if (orientation == HORIZONTAL) {
                lp.height = measuredHeight
            } else if (orientation == VERTICAL) {
                lp.width = measuredWidth
            }
            child.layoutParams = lp
        }
    }

    //刷新子view数组
    private fun refreshChildList() {
        if (mChildLayoutList.size != childCount) {
            mChildLayoutList.clear()
            for (i in 0 until childCount) {
                val childView: View = getChildAt(i)
                mChildLayoutList.add(childView)
            }
        }
    }

    //刷新子view size
    private fun refreshChildSizeList() {
        if (mChildWidthList.size != childCount) {
            mChildWidthList.clear()
            mChildLayoutList.forEachIndexed { _, child ->
                mChildWidthList.add(child.measuredWidth)
            }
            mRunningXList = mChildWidthList
        }
        if (mChildHeightList.size != childCount) {
            mChildHeightList.clear()
            mChildLayoutList.forEachIndexed { _, child ->
                mChildHeightList.add(child.measuredHeight)
            }
            mRunningYList = mChildHeightList
        }
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
    @SuppressLint("ClickableViewAccessibility", "RtlHardcoded")
    private fun addIntervalLine(index: Int, child: View) {
        val interValView = View(context)
        interValView.setBackgroundColor(mIntervalLineColor)
        var lp: ViewGroup.LayoutParams = LayoutParams(measuredWidth, mIntervalLineWidth)
        if (orientation == HORIZONTAL) {
            lp = LayoutParams(mIntervalLineWidth, measuredHeight)
        } else if (orientation == VERTICAL) {
            lp = LayoutParams(measuredWidth, mIntervalLineWidth)
        }
        interValView.layoutParams = lp
        val tarGetLocation = IntArray(2)
        child.getLocationOnScreen(tarGetLocation)
        if (orientation == HORIZONTAL) {
            interValView.x = tarGetLocation[0].toFloat()
        } else if (orientation == VERTICAL) {
            interValView.y = tarGetLocation[1].toFloat()
        }
        val realIndex = 1 + index * 2
        interValView.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    mStartX = motionEvent.x
                    mStartY = motionEvent.y
                }
                MotionEvent.ACTION_UP -> {
                    refreshChildSizeList()
                }
            }
            mIntervalX = mStartX - motionEvent.x
            mIntervalY = mStartY - motionEvent.y
            val logList: ArrayList<Int> = arrayListOf()
            mChildLayoutList.forEachIndexed { _, child ->
                logList.add(child.measuredWidth)
            }
            if (orientation == HORIZONTAL) {
                if (isChildValueLegal(mRunningXList[realIndex - 1] - mIntervalX.toInt()) &&
                        isChildValueLegal(mRunningXList[realIndex + 1] + mIntervalX.toInt())
                ) {
                    mRunningXList[realIndex - 1] -= mIntervalX.toInt()
                    mRunningXList[realIndex + 1] += mIntervalX.toInt()
                }
                // 联动调整左边
                if (!isChildValueLegal(mRunningXList[realIndex - 1] - mIntervalX.toInt())) {
                    gravity = Gravity.LEFT
                    var fixMulti = 0
                    if (realIndex - 2 > 0) {
                        for (index in 0..realIndex - 2) {
                            if (isChildValueLegal(mRunningXList[index] - mIntervalX.toInt())) {
                                mRunningXList[index] -= mIntervalX.toInt()
                                fixMulti++
                            }
                        }
                        mRunningXList[realIndex + 1] += mIntervalX.toInt() * fixMulti
                    }
                }
                //联动调整右边
                if (!isChildValueLegal(mRunningXList[realIndex + 1] + mIntervalX.toInt())) {
                    gravity = Gravity.RIGHT
                    var fixMulti = 0
                    for (index in (realIndex + 2) until childCount) {
                        if (isChildValueLegal(mRunningXList[index] + mIntervalX.toInt())) {
                            mRunningXList[index] += mIntervalX.toInt()
                            fixMulti++
                        }
                    }
                    mRunningXList[realIndex - 1] -= mIntervalX.toInt() * fixMulti
                }
            } else if (orientation == VERTICAL) {
                if (isChildValueLegal(mRunningYList[realIndex - 1] - mIntervalY.toInt()) &&
                        isChildValueLegal(mRunningYList[realIndex + 1] + mIntervalY.toInt())) {
                    mRunningYList[realIndex - 1] -= mIntervalY.toInt()
                    mRunningYList[realIndex + 1] += mIntervalY.toInt()
                }
                // 联动调整上面
                if (!isChildValueLegal(mRunningYList[realIndex - 1] - mIntervalY.toInt())) {
                    gravity = Gravity.TOP
                    var fixMulti = 0
                    if (realIndex - 2 > 0) {
                        for (index in 0..realIndex - 2) {
                            if (isChildValueLegal(mRunningYList[index] - mIntervalY.toInt())) {
                                mRunningYList[index] -= mIntervalY.toInt()
                                fixMulti++
                            }
                        }
                        mRunningYList[realIndex + 1] += mIntervalY.toInt() * fixMulti
                    }
                }

                // 联动调整下面
                if (!isChildValueLegal(mRunningYList[realIndex + 1] + mIntervalY.toInt())) {
                    gravity = Gravity.BOTTOM
                    var fixMulti = 0
                    for (index in (realIndex + 2) until childCount) {
                        if (isChildValueLegal(mRunningYList[index] + mIntervalY.toInt())) {
                            mRunningYList[index] += mIntervalY.toInt()
                            fixMulti++
                        }
                    }
                    mRunningYList[realIndex - 1] -= mIntervalY.toInt() * fixMulti
                }


            }
            mChildLayoutList.forEachIndexed { index, view ->
                val childLp: LayoutParams = view.layoutParams as LayoutParams
                childLp.weight = 0f
                if (orientation == HORIZONTAL) {
                    childLp.width = mRunningXList[index]

                } else if (orientation == VERTICAL) {
                    childLp.height = mRunningYList[index]
                }

                view.layoutParams = childLp
            }

            //防止左越界
            if (mChildLayoutList.size != 0) {
                mChildLayoutList[0].x = 0f
            }
            true
        }
        mIntervalList.add(interValView)
        addView(interValView, realIndex, lp)
    }

    private fun isChildValueLegal(value: Int): Boolean {
        //return value in (mMinZoom + 1) until mMaxZoom
        return value > mMinZoom
    }

}