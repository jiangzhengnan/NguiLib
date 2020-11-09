package com.ng.nguilib.layout

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.ng.nguilib.R
import com.ng.nguilib.utils.MLog
import com.ng.nguilib.utils.ScreenUtils
import com.ng.nguilib.utils.ViewUtils

/**
 * 增加滑动引导按钮的版本
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class ZoomLayout3 constructor(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    //子layout列表
    private var mChildLayoutList: ArrayList<View> = arrayListOf()
    private var mIntervalList: ArrayList<ZoomGuideView> = arrayListOf()

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

    //添加的分割线触摸距离
    private var ADD_TOUCH_DISTANCE = 50

    init {
        ADD_TOUCH_DISTANCE = ViewUtils.dip2px(context, 16f)
        //设置子布局可以溢出父布局
        clipChildren = false
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ZoomLayout)
        mIntervalLineWidth = context.resources.getDimensionPixelOffset(ta.getResourceId(R.styleable.ZoomLayout_IntervalLineWidth, R.dimen.dd03))
        mIntervalLineColor = ta.getColor(R.styleable.ZoomLayout_IntervalLineColor, Color.BLACK)
        ta.recycle()


    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (mChildLayoutList.size != childCount) {
            refreshChildList()
            refreshChildSizeList()
            addSplit()
            //expandTouchDelegate(80)
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mChildLayoutList.clear()
        if (mChildLayoutList.size != childCount) {
            mIntervalList.forEach {
                removeView(it)
            }
            hadAdd = false
            refreshChildList()
            refreshChildSizeList()
            addSplit()
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
    @SuppressLint("ClickableViewAccessibility")
    private fun addIntervalLine(number: Int, child: View) {
        val interValView = ZoomGuideView(context)
        interValView.setBackgroundColor(mIntervalLineColor)
        var lp: LinearLayout.LayoutParams = LayoutParams(measuredWidth, mIntervalLineWidth)
        if (orientation == HORIZONTAL) {
            lp = LayoutParams(mIntervalLineWidth, ViewGroup.LayoutParams.MATCH_PARENT)
        } else if (orientation == VERTICAL) {
            lp = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mIntervalLineWidth)
        }
        lp.weight = 0f
        interValView.layoutParams = lp

        val realIndex = 1 + number * 2

        interValView.setOnMyTouchListener(object : ZoomGuideView.OnMyTouchListener {
            override fun onMyTouchEvent(view: View, motionEvent: MotionEvent): Boolean {
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        requestDisallowInterceptTouchEvent(true)
                        mStartX = motionEvent.x
                        mStartY = motionEvent.y
                        refreshChildSizeList()
                        expandInterval(realIndex, motionEvent.y)
                    }
                    MotionEvent.ACTION_UP -> {
                        refreshChildSizeList()
                        view.performClick()

                        hideInterval(realIndex, motionEvent.y)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        requestDisallowInterceptTouchEvent(true)

                        expandInterval(realIndex, motionEvent.y)
                        mIntervalX = mStartX - motionEvent.x
                        mIntervalY = mStartY - motionEvent.y
                        mStartX = motionEvent.x
                        mStartY = motionEvent.y




                        if (orientation == HORIZONTAL) {
                            if (isChildValueLegal(mRunningXList[realIndex - 1] - mIntervalX.toInt(), realIndex - 1) &&
                                    isChildValueLegal(mRunningXList[realIndex + 1] + mIntervalX.toInt(), realIndex + 1)
                            ) {
                                mRunningXList[realIndex - 1] -= mIntervalX.toInt()
                                mRunningXList[realIndex + 1] += mIntervalX.toInt()
                            }
                        } else if (orientation == VERTICAL) {
                            if (isChildValueLegal(mRunningYList[realIndex - 1] - mIntervalY.toInt(), realIndex - 1) &&
                                    isChildValueLegal(mRunningYList[realIndex + 1] + mIntervalY.toInt(), realIndex + 1)) {
                                mRunningYList[realIndex - 1] -= mIntervalY.toInt()
                                mRunningYList[realIndex + 1] += mIntervalY.toInt()
                            }
                        }
                        resizeChildSize()
                        if (mChildLayoutList.size != 0) {
                            //防止左越界
                            mChildLayoutList[0].x = 0f
                        }
                    }
                }
                return true
            }
        })
        mIntervalList.add(interValView)
        addView(interValView, realIndex, lp)
    }

    private fun resizeChildSize() {
        mChildLayoutList.forEachIndexed { index, child ->
            val childLp: LayoutParams = child.layoutParams as LayoutParams

            if (orientation == HORIZONTAL) {
                childLp.width = mRunningXList[index]
                if (index == mChildLayoutList.size - 1) {
                    //防右越界
                    var temp = measuredWidth - mChildLayoutList[mChildLayoutList.size - 1].x
                    childLp.width = temp.toInt()
                } else {
                    childLp.width = mRunningXList[index]
                }

            } else if (orientation == VERTICAL) {
                childLp.height = mRunningYList[index]
            }
            child.layoutParams = childLp
        }
    }

    @Volatile
    private var mNowChoiceIndex = -1


    //需要增大点击区域的范围
    //暂时只做了横向的～
    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        //Log.d("nangua", "onTouchEvent: " + motionEvent.action + " " + mNowChoiceIndex)
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                if (mNowChoiceIndex != -1) {
                    val intervalView: ZoomGuideView = mChildLayoutList[mNowChoiceIndex] as ZoomGuideView
                    return intervalView.onMyTouchEvent(intervalView, motionEvent)
                }
            }
            MotionEvent.ACTION_UP -> {
                mIntervalList.forEach {
                    it.translationZ = 0.1f
                    it.expend(false)
                }
                mNowChoiceIndex = -1
            }
            MotionEvent.ACTION_MOVE -> {
                if (mNowChoiceIndex != -1) {
                    val intervalView: ZoomGuideView = mChildLayoutList[mNowChoiceIndex] as ZoomGuideView
                    requestDisallowInterceptTouchEvent(true)

                    return intervalView.onMyTouchEvent(intervalView, motionEvent)
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                mIntervalList.forEach {
                    it.translationZ = 0.1f
                    it.expend(false)
                }
                mNowChoiceIndex = -1
            }
        }

        return super.onTouchEvent(motionEvent)
    }


    override fun onInterceptTouchEvent(motionEvent: MotionEvent): Boolean {
        //Log.d("nangua", "onInterceptTouchEvent: " + motionEvent.action + " " + mNowChoiceIndex)
        if (mNowChoiceIndex != -1) {
            return true
        }
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                if (orientation == HORIZONTAL) {
                    val mLeftXList: ArrayList<Int> = arrayListOf()
                    var all = 0
                    for (index: Int in 0 until mRunningXList.size) {
                        all += mRunningXList[index]
                        mLeftXList.add(all)
                    }
                    //MLog.d("范围数组: " + mLeftXList.toString())
                    //Log.d("nangua", "找到列表: " + mRunningXList.toString())
                    for (index: Int in 0 until mLeftXList.size) {
                        var left = 0
                        if (index > 0) {
                            left = mLeftXList[index - 1]
                        }
                        var right = measuredWidth
                        if (index < mLeftXList.size) {
                            right = mLeftXList[index]
                        }
                        if (index % 2 != 0) {
                            left -= ADD_TOUCH_DISTANCE
                            right += ADD_TOUCH_DISTANCE
                        } else {
                            if (index == 0) {
                                right -= ADD_TOUCH_DISTANCE
                            } else if (index == mLeftXList.size - 1) {
                                left += ADD_TOUCH_DISTANCE
                                right -= ADD_TOUCH_DISTANCE
                            } else {
                                left += ADD_TOUCH_DISTANCE
                            }
                        }

                        Log.d("nangua", "坐标"+ motionEvent.x+"当前: " + left + " " + right + "     " + index)

                        if (motionEvent.x > left && motionEvent.x < right) {
                            Log.d("nangua", "选择: " + index)
                            if (mChildLayoutList[index] is ZoomGuideView) {
                                mNowChoiceIndex = index
                                requestDisallowInterceptTouchEvent(true)
                                return true
                            }
                        }
                    }
                    return super.onInterceptTouchEvent(motionEvent)
                }
            }
        }
        return super.onInterceptTouchEvent(motionEvent)
    }


    private fun hideInterval(realIndex: Int, showY: Float) {
        var temp: ZoomGuideView = mChildLayoutList[realIndex] as ZoomGuideView
        temp.translationZ = 0.1f
        temp.setShowY(showY)
        temp.expend(false)
    }

    //展示引导图
    private fun expandInterval(realIndex: Int, showY: Float) {
        var temp: ZoomGuideView = mChildLayoutList[realIndex] as ZoomGuideView
        temp.translationZ = 0.2f
        temp.setShowY(showY)
        temp.expend(true)
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