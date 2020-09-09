package com.ng.ui.other.zoom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.contains
import com.ng.nguilib.utils.DensityUtil
import com.ng.nguilib.utils.MLog

/**
 * 描述:
 * @author Jzn
 * @date 2020/9/9
 */
class ZoomLayout(context: Context?, attrs: AttributeSet?) : LinearLayout(context!!, attrs) {
    //子layout列表
    private var mChildLayoutList: ArrayList<View> = arrayListOf()

    //分割线宽度
    private var mIntervalLineWidth = 1


    init {
        initView()
    }


    //起始点位置
    private var mStartX = 0f
    private var mStartY = 0f

    //位移
    private var mIntervalX = 0f
    private var mIntervalY = 0f

    private var hadAdd = false


    //保存每个子view的宽度
    private var mChildWidthList: ArrayList<Int> = arrayListOf()

    //变化中的子view宽度
    private var mRunningList: ArrayList<Int> = arrayListOf()


    private fun initView() {
        //todo 这里要属性获取分割线宽度
        mIntervalLineWidth = DensityUtil.dip2px(context, 10f)

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val childCount = childCount
        mChildLayoutList.clear()
        for (i in 0 until childCount) {
            val childView: View = getChildAt(i)
            mChildLayoutList.add(childView)

            //childView.measure(50, heightMeasureSpec)
        }

//        mChildLayoutList.forEachIndexed { index, child ->
//            MLog.d("child: " + index + "  id:" + child.id)
//        }


        addSplit()

        if (mChildWidthList.size != childCount) {
            refreshChildWidthList()
        }
    }

    private fun refreshChildWidthList() {
        mChildWidthList.clear()
        mChildLayoutList.forEachIndexed { index, child ->
            mChildWidthList.add(child.measuredWidth)
        }
        mRunningList = mChildWidthList
        MLog.d("每个子view的宽度: $mChildWidthList")

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
    }

    //在子view中设置操作分割线
    private fun addSplit() {
        if (hadAdd) {
            return
        }

        //设置整体padding
        setPadding(mIntervalLineWidth, mIntervalLineWidth, mIntervalLineWidth, mIntervalLineWidth)
        //todo 这里要做横竖向判定
        //添加子view间距离
        //在子view的间距中添加操作view
        mChildLayoutList.forEachIndexed { index, child ->
            if (index < mChildLayoutList.size - 1) {
                addIntervalLine(index, child)
            }
        }
        hadAdd = true
    }


    private fun addIntervalLine(index: Int, child: View) {
        var interValView = View(context)
        interValView.setBackgroundColor(Color.parseColor("#11000000"))
        MLog.d("宽度: $index $mIntervalLineWidth $measuredHeight")
        var lp: ViewGroup.LayoutParams = LayoutParams(mIntervalLineWidth, measuredHeight)
        interValView.layoutParams = lp
        val tarGetLocation = IntArray(2)
        child.getLocationOnScreen(tarGetLocation)
        interValView.x = tarGetLocation[0].toFloat()

        interValView.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(p0: View, motionEvent: MotionEvent): Boolean {
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        mStartX = motionEvent.x
                        mStartY = motionEvent.y
                    }
                    MotionEvent.ACTION_UP -> {
                        refreshChildWidthList()
                    }
                    MotionEvent.ACTION_MOVE -> {

                    }
                }

                mIntervalX = mStartX - motionEvent.x
                mIntervalY = mStartY - motionEvent.y

                val realIndex = 1 + index * 2
                // 左边+ 右边-
                var logList: ArrayList<Int> = arrayListOf()
                mChildLayoutList.forEachIndexed { index, child ->
                    logList.add(child.measuredWidth)
                }
                MLog.d("mIntervalX:" + mIntervalX + " realIndex:" + realIndex)
                MLog.d("@@@@@@@@@@@@@@@@@@   init 宽度: $mChildWidthList")
                MLog.d("真实 宽度: $logList")


//                mRunningList[realIndex - 1] = (mChildWidthList[realIndex - 1] - mIntervalX).toInt()
//                mRunningList[realIndex + 1] = (mChildWidthList[realIndex + 1] + mIntervalX).toInt()
                mRunningList[realIndex - 1] -= mIntervalX.toInt()
                mRunningList[realIndex + 1] += mIntervalX.toInt()


                MLog.d("动态修改后的 宽度: $mRunningList")
                //[275, 28, 275, 28, 418]

                mChildLayoutList.forEachIndexed { index, view ->
                    val lp: LinearLayout.LayoutParams = view.layoutParams as LayoutParams
                    lp.weight = 0f
                    lp.width = mRunningList[index]
                    view.layoutParams = lp
                }
                return true
            }

        })

        addView(interValView, 1 + index * 2, lp)
    }

    private fun setViewWidth(view: View, width: Int) {
        val lp: ViewGroup.LayoutParams = view.layoutParams
        lp.width = width
        view.layoutParams = lp
    }

    private fun addRightMargin(view: View, right: Int) {
        val lp: LayoutParams = view.layoutParams as LayoutParams
        lp.setMargins(0, 0, right, 0)
        view.layoutParams = lp
    }

    //移除view
    private fun hideView(view: View) {
        if (contains(view)) {
            removeView(view)
        }
    }


}