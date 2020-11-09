package com.ng.nguilib.layout

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.ng.nguilib.R
import kotlin.math.abs

/**
 * 测试覆盖滑动的版本
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class ZoomLayout2 constructor(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
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

    //刷新子view size
    private fun refreshChildSizeList() {
        mRunningXList.clear()
        mChildLayoutList.forEachIndexed { _, child ->
            mRunningXList.add(child.measuredWidth)
        }
        mRunningYList.clear()
        mChildLayoutList.forEachIndexed { _, child ->
            mRunningYList.add(child.measuredHeight)
        }
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

                    if (parent != null) {
                        //防止事件被父布局拦截
                        parent.requestDisallowInterceptTouchEvent(true);
                    }

                    refreshChildSizeList()


                }
                MotionEvent.ACTION_UP -> {
                    view.performClick()
                    refreshChildSizeList()

                }
                MotionEvent.ACTION_MOVE -> {
                    mIntervalX = mStartX - motionEvent.x
                    mIntervalY = mStartY - motionEvent.y
                    // MLog.d("temp:  " + mTempX + " ");
                    judgeLocation(realIndex)

                }
            }
            true
        }

        interValView.translationZ = 0.1f
        mIntervalList.add(interValView)
        addView(interValView, realIndex, lp)
    }

    //修正值
    var fixValue = 0


    private fun judgeLocation(realIndex: Int) {

        if (orientation == HORIZONTAL) {
            if (isChildValueLegal(mRunningXList[realIndex - 1] - mIntervalX.toInt(), realIndex - 1) &&
                    isChildValueLegal(mRunningXList[realIndex + 1] + mIntervalX.toInt(), realIndex + 1)
            ) {
                if (fixValue == 0) {
                    mRunningXList[realIndex - 1] -= mIntervalX.toInt()
                    mRunningXList[realIndex + 1] += mIntervalX.toInt()
                }
            }
        } else if (orientation == VERTICAL) {
            if (isChildValueLegal(mRunningYList[realIndex - 1] - mIntervalY.toInt(), realIndex - 1) &&
                    isChildValueLegal(mRunningYList[realIndex + 1] + mIntervalY.toInt(), realIndex + 1)) {
                mRunningYList[realIndex - 1] -= mIntervalY.toInt()
                mRunningYList[realIndex + 1] += mIntervalY.toInt()
            }
        }


        mChildLayoutList.forEachIndexed { index, child ->
            val childLp: LayoutParams = child.layoutParams as LayoutParams
            childLp.weight = 0f
            if (orientation == HORIZONTAL) {
                var value: Int = abs(mIntervalX.toInt())

                //正左负右
                if (mIntervalX > 0) {
                    when (index) {
                        realIndex - 1 -> {
                            if (!isChildValueLegal(mRunningXList[realIndex - 1] - value, realIndex - 1)) {
                                fixValue += value

                                childLp.marginEnd -= value
                                mRunningXList[realIndex + 1] += value

                                setLeftZ(realIndex)
                            }
                        }
                        realIndex + 1 -> {
                            if (!isChildValueLegal(mRunningXList[realIndex + 1] - value, realIndex + 1)) {
                                fixValue -= value;

                                childLp.marginStart += value
                                mRunningXList[realIndex - 1] -= value
                                if (fixValue < 0) {
                                    //防止间隙
                                    val sunShi = abs(fixValue)
                                    childLp.marginStart -= sunShi
                                    mRunningXList[realIndex - 1] += sunShi
                                    fixValue = 0
                                }
                                serRightZ(realIndex)
                            }
                        }
                        else -> {
                        }
                    }
                } else {
                    //向右边
                    when (index) {
                        realIndex - 1 -> {
                            if (!isChildValueLegal(mRunningXList[realIndex - 1] - value, realIndex - 1)) {
                                fixValue -= value;

                                childLp.marginEnd += value
                                mRunningXList[realIndex + 1] -= value
                                if (fixValue < 0) {
                                    //防止间隙
                                    val sunShi = abs(fixValue)
                                    childLp.marginEnd -= sunShi
                                    mRunningXList[realIndex + 1] += sunShi
                                    fixValue = 0
                                }
                                setLeftZ(realIndex)
                            }
                        }
                        realIndex + 1 -> {
                            if (!isChildValueLegal(mRunningXList[realIndex + 1] - value, realIndex + 1)) {
                                fixValue += value
                                mRunningXList[realIndex - 1] += value
                                childLp.marginStart -= value
                                serRightZ(realIndex)
                            }
                        }
                        else -> {
                        }
                    }
                }

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

    private fun serRightZ(realIndex: Int) {
        mChildLayoutList[realIndex - 1].translationZ = 0.1f
        mChildLayoutList[realIndex + 1].translationZ = 0f

    }

    private fun setLeftZ(realIndex: Int) {
        mChildLayoutList[realIndex - 1].translationZ = 0f
        mChildLayoutList[realIndex + 1].translationZ = 0.1f

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