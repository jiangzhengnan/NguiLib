package com.ng.nguilib.layout

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.ng.nguilib.R
import com.ng.nguilib.layout.thread.ThreadPoolUtil
import com.ng.nguilib.utils.ViewUtils
import kotlin.math.abs


/**
 * 基于LinearLayout
 * 增加滑动引导按钮的版本
 * 增加了长按触发功能
 */
@SuppressLint("CustomViewStyleable")
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (mChildLayoutList.size != childCount) {
            refreshChildList()
            refreshChildSizeList()
            addSplit()
            //expandTouchDelegate(80)
        } else {
            refreshChildSizeList()
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
            if (childView.visibility == VISIBLE)
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
            mChildLayoutList.forEachIndexed { index, _ ->
                if (index < mChildLayoutList.size - 1) {
                    addIntervalLine(index)
                }
            }
            hadAdd = true
        }
    }

    var startTime = 0L
    var mNowTouchX = -1f
    var mNowTouchY = -1f
    var mRealIndex = -1
    var hadPost = false
    var isInside = true
    var isMoving = false

    private fun isInside(x: Float, y: Float): Boolean {
        return abs(mNowTouchX - x) < 10 && abs(mNowTouchY - y) < 10
    }

    //增加垂直分割线
    @SuppressLint("ClickableViewAccessibility")
    private fun addIntervalLine(number: Int) {
        val interValView = ZoomGuideView(context)
        interValView.setBackgroundColor(mIntervalLineColor)
        var lp = LayoutParams(measuredWidth, mIntervalLineWidth)
        if (orientation == HORIZONTAL) {
            lp = LayoutParams(mIntervalLineWidth, ViewGroup.LayoutParams.MATCH_PARENT)
        } else if (orientation == VERTICAL) {
            lp = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mIntervalLineWidth)
        }
        interValView.layoutParams = lp
        val realIndex = 1 + number * 2
        interValView.setOnMyTouchListener(object : ZoomGuideView.OnMyTouchListener {
            override fun onMyTouchEvent(event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        requestDisallowInterceptTouchEvent(true)
                        mStartX = event.x
                        mStartY = event.y
                        refreshChildSizeList()
                        //expandInterval(realIndex, event.y)

                        mNowTouchX = event.x
                        mNowTouchY = event.y
                        mRealIndex = realIndex


                        if (!hadPost) {
                            hadPost = true
                            startTime = System.currentTimeMillis()


                            ThreadPoolUtil.runOnUiThread({
                                if (isInside) {
                                    isMoving = true
                                    expandInterval(mRealIndex, mNowTouchY)
                                }
                            }, 400)
                        }
                    }
                    MotionEvent.ACTION_UP -> {

                        startTime = Long.MAX_VALUE
                        refreshChildSizeList()
                        hideInterval(realIndex, event.y)

                        resetTouchView()
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        mNowTouchY = event.y
                        resetTouchView()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (isInside(event.x, event.y)) {
                            mNowTouchX = event.x
                            mNowTouchY = event.y
                            isInside = true
                        } else {
                            isInside = false
                        }

                        if (isMoving) {
                            mNowTouchX = event.x
                            mNowTouchY = event.y
                        }

                        mRealIndex = realIndex


                        if (System.currentTimeMillis() - startTime < 400 || !isMoving) {
                            return true
                        }

                        requestDisallowInterceptTouchEvent(true)
                        expandInterval(realIndex, event.y)
                        mIntervalX = mStartX - event.x
                        mIntervalY = mStartY - event.y
                        mStartX = event.x
                        mStartY = event.y
                        if (orientation == HORIZONTAL) {
                            val xDistance = mIntervalX.toInt()
                            //横屏需要考虑 有时候被动进入最小宽度值的情况
                            if (xDistance > 0) {
                                if (isChildValueLegal(mRunningXList[realIndex - 1] - mIntervalX.toInt(), realIndex - 1)) {
                                    mRunningXList[realIndex - 1] -= mIntervalX.toInt()
                                    if ((mRunningXList[realIndex + 1] + mIntervalX.toInt()) < measuredWidth) {
                                        mRunningXList[realIndex + 1] += mIntervalX.toInt()
                                    }
                                }
                            } else {
                                if (isChildValueLegal(mRunningXList[realIndex + 1] + mIntervalX.toInt(), realIndex + 1)) {
                                    mRunningXList[realIndex + 1] += mIntervalX.toInt()

                                    if ((mRunningXList[realIndex - 1] - mIntervalX.toInt()) < measuredWidth) {
                                        mRunningXList[realIndex - 1] -= mIntervalX.toInt()
                                    }
                                }
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

            if (orientation == HORIZONTAL && (index < mRunningXList.size)) {
                childLp.width = mRunningXList[index]
            } else if (orientation == VERTICAL && (index < mRunningYList.size)) {
                childLp.height = mRunningYList[index]
            }
            child.layoutParams = childLp
        }
    }

    @Volatile
    private var mNowChoiceIndex = -1

    interface OnZoomListener {
        fun setState(isZoomming: Boolean)
    }

    private var isZoomState = false

    fun getZoomState(): Boolean {
        return isZoomState
    }

    private var mCallBack: OnZoomListener? = null

    fun addListener(callback: OnZoomListener) {
        mCallBack = callback
    }


    //需要增大点击区域的范围
    //暂时只做了横向的～
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                if (mNowChoiceIndex != -1) {
                    if (mCallBack != null) {
                        mCallBack!!.setState(true)
                    }
                    val intervalView: ZoomGuideView = mChildLayoutList[mNowChoiceIndex] as ZoomGuideView
                    return intervalView.onTouchEvent(motionEvent)
                }
            }
            MotionEvent.ACTION_UP -> {
                resetTouchView()


                isZoomState = false
                mIntervalList.forEach {
                    it.translationZ = 0.1f
                    it.expend(false)
                }
                if (mCallBack != null) {
                    mCallBack!!.setState(false)
                }
                mNowChoiceIndex = -1
            }
            MotionEvent.ACTION_MOVE -> {
                if (mNowChoiceIndex != -1) {
                    val intervalView: ZoomGuideView = mChildLayoutList[mNowChoiceIndex] as ZoomGuideView
                    requestDisallowInterceptTouchEvent(true)
                    return intervalView.onTouchEvent(motionEvent)
                }
            }
            MotionEvent.ACTION_CANCEL -> {

                mIntervalList.forEach {
                    it.translationZ = 0.1f
                    it.expend(false)
                }
                mNowChoiceIndex = -1
                if (mCallBack != null) {
                    mCallBack!!.setState(false)
                }

                //重置长按相关
                resetTouchView()

            }
        }

        return super.onTouchEvent(motionEvent)
    }

    fun resetTouchView() {
        //重置长按相关
        isMoving = false
        hadPost = false
        isInside = true
        //隐藏滑动条
        startTime = Long.MAX_VALUE
        refreshChildSizeList()
        hideInterval(mRealIndex, mNowTouchY)
        mRealIndex = -1
        mNowTouchY = -1f
    }


    override fun onInterceptTouchEvent(motionEvent: MotionEvent): Boolean {
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
                        if (motionEvent.x > left && motionEvent.x < right) {
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
        if (realIndex < mChildHeightList.size) {
            val temp: ZoomGuideView = mChildLayoutList[realIndex] as ZoomGuideView
            temp.translationZ = 0.1f
            temp.setShowY(showY)
            temp.expend(false)
        }
    }

    //展示引导图
    private fun expandInterval(realIndex: Int, showY: Float) {
        if (realIndex < mChildHeightList.size && realIndex >= 0) {
            val temp: ZoomGuideView = mChildLayoutList[realIndex] as ZoomGuideView
            temp.translationZ = 0.2f
            temp.setShowY(showY)
            temp.expend(true)
        }
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