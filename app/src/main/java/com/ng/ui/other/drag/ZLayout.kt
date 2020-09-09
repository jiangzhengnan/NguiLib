package com.ng.ui.other.drag

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.contains
import com.ng.ui.R
import kotlinx.android.synthetic.main.activity_drag.view.*
import java.util.*

/**
 * 描述:  可拖动layout
 * @author Jzn
 * @date 2020/9/8
 */
class ZLayout : ConstraintLayout,ZLayoutStretchListener {
    //子layout列表
    private var mChildLayoutList = arrayListOf<ZChildLayout>()

    //当前操作view
    private var mOperationView: ConstraintLayout? = null
    private var mOperationIndex: Int = -1

    //绘制
    private var mIsDrawing = false
    private lateinit var mAnimItemView: ImageView

    //操作符
    private lateinit var mLeftArrow: ImageView
    private lateinit var mRightArrow: ImageView
    private lateinit var mUpArrow: ImageView
    private lateinit var mDownArrow: ImageView
    private lateinit var mArrowList: ArrayList<ImageView>
    private var mArrowResList = arrayListOf(R.drawable.ic_left, R.drawable.ic_up, R.drawable.ic_right, R.drawable.ic_down)

    //操作符区域
    private var mLeftRect: Rect = Rect()
    private var mRightRect: Rect = Rect()
    private var mUpRect: Rect = Rect()
    private var mDownRect: Rect = Rect()
    private var mArrowRectList: ArrayList<Rect> = arrayListOf()

    //父布局id
    private var mRootId = 0

    //

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        initAll()
    }

    private fun initAll() {
        mRootId = id
        mAnimItemView = ImageView(context)
        mAnimItemView.scaleType = ImageView.ScaleType.FIT_XY
        mLeftArrow = ImageView(context)
        mRightArrow = ImageView(context)
        mUpArrow = ImageView(context)
        mDownArrow = ImageView(context)
        mLeftArrow.setImageResource(mArrowResList[0])
        mUpArrow.setImageResource(mArrowResList[1])
        mRightArrow.setImageResource(mArrowResList[2])
        mDownArrow.setImageResource(mArrowResList[3])
        mArrowList = arrayListOf(mLeftArrow, mUpArrow, mRightArrow, mDownArrow)
        mArrowRectList = arrayListOf(mLeftRect, mUpRect, mRightRect, mDownRect)
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        var mArrowWidth = mLeftArrow.width
        var mArrowHeight = mLeftArrow.height
        //确定操作符位置
        mLeftArrow.x = 0f
        mLeftArrow.y = height / 2.toFloat() - mArrowHeight / 2
        mRightArrow.x = width.toFloat() - mArrowWidth
        mRightArrow.y = height / 2.toFloat() - mArrowHeight / 2
        mUpArrow.x = width / 2.toFloat() - mArrowWidth / 2
        mUpArrow.y = 0f
        mDownArrow.x = width / 2.toFloat() - mArrowWidth / 2
        mDownArrow.y = height.toFloat() - mArrowHeight
        //确定操作符区域
        //扩大一点区域，方便选中
        mArrowWidth += 100
        mArrowHeight += 100
        mLeftRect = Rect(0, height / 2 - mArrowHeight / 2, mArrowWidth, height / 2 + mArrowHeight / 2)
        mRightRect = Rect(width - mArrowWidth, height / 2 - mArrowHeight / 2, width, height / 2 + mArrowHeight / 2)
        mUpRect = Rect(width / 2 - mArrowWidth / 2, 0, width / 2 + mArrowWidth / 2, mArrowHeight)
        mDownRect = Rect(width / 2 - mArrowWidth / 2, height - mArrowHeight, width / 2 + mArrowWidth / 2, height)
        mArrowRectList = arrayListOf(mLeftRect, mUpRect, mRightRect, mDownRect)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val childCount = childCount
        mChildLayoutList.clear()
        for (i in 0 until childCount) {
            val childView: View = getChildAt(i)
            if (childView is ZChildLayout) {
                mChildLayoutList.add(childView)
            }
            childView.measure(widthMeasureSpec, heightMeasureSpec)
        }



        mChildLayoutList.forEachIndexed { index, child ->
            child.setCallBack(index, this)
        }
    }

    //拖动的位置
    private var mLiftX = 0f
    private var mLiftY = 0f

    //起始点位置
    private var mStartX = 0f
    private var mStartY = 0f

    //位移
    private var mIntervalX = 0f
    private var mIntervalY = 0f

    override fun onStartLift(motionEvent: MotionEvent) {
        mStartX = motionEvent.x
        mStartY = motionEvent.y

        mChildLayoutList.forEachIndexed { index, child ->
        }
    }


    override fun onLift(index: Int, view: View, motionEvent: MotionEvent) {
        //MLog.d("$index $motionEvent")
        mOperationView = view as ConstraintLayout
        mLiftX = motionEvent.rawX
        mLiftY = motionEvent.rawY - ViewUtils.getStatusBarHeight(context)
        mIntervalX = mStartX - motionEvent.x
        mIntervalY = mStartY - motionEvent.y

        val bitmap = ViewUtils.getBitmapFromView(view)
        view.visibility = View.INVISIBLE
        val tarGetLocation = IntArray(2)
        view.getLocationOnScreen(tarGetLocation)
        if (!mIsDrawing) {
            mAnimItemView.setImageBitmap(bitmap)
        }
        mAnimItemView.x = tarGetLocation[0].toFloat() - mIntervalX
        mAnimItemView.y = tarGetLocation[1].toFloat() - ViewUtils.getStatusBarHeight(context) - mIntervalY
        //显示悬浮框
        showView(mAnimItemView)
        mIsDrawing = true

        //显示操作视图
        showOptionView()

    }

    //在父布局的四个角显示操作符按钮
    private fun showOptionView() {
        mArrowList.forEach {
            showView(it)
        }
        mArrowRectList.forEachIndexed { index, rect ->
            if (rect.contains(mLiftX.toInt(), mLiftY.toInt())) {
                mArrowList[index].setImageResource(R.drawable.ic_change)
                mOperationIndex = index
            } else {
                mArrowList[index].setImageResource(mArrowResList[index])
            }
        }
    }

    //重新排序
    //先试验单一的左右关系
    private fun onOption(index: Int) {
        var constraintSet: ConstraintSet = ConstraintSet()
        constraintSet.clone(root_layout)

        //左 上 右 下
        when (index) {
            0 -> {
                //原来的左右改为到一起
                //找到原来左边约束父布局的view
//                mChildLayoutList.forEachIndexed { index, child ->
//                    var lp: LayoutParams = child.layoutParams as LayoutParams
//                    var leftToLeftId = lp.leftToLeft
//
//                    MLog.d("index: " + index)
//
//                    MLog.d(" left to left : " + lp.leftToLeft)
//                    MLog.d(" left to right : " + lp.leftToRight)
//
//                    MLog.d(" right to left : " + lp.rightToLeft)
//                    MLog.d(" right to right : " + lp.rightToRight)
//
//                    if (leftToLeftId.equals(0)) {
//                        MLog.d("当前操作的:" + mOperationView!!.id)
//                        MLog.d(" 找到的 : " + child.id)
//
//                    }
//
//                    //left to parent
//                    constraintSet.connect(mOperationView!!.id,ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,20)
//                    constraintSet.connect(mOperationView!!.id,ConstraintSet.RIGHT,child.id,ConstraintSet.LEFT,20)
//                    constraintSet.applyTo(root_layout)
//                    return
//
//                }
                var left = mChildLayoutList[0]
                var center = mChildLayoutList[1]
                var right = mChildLayoutList[2]
                constraintSet.connect(center.id,ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,0)
                constraintSet.connect(center.id,ConstraintSet.RIGHT,left.id,ConstraintSet.LEFT,0)

                constraintSet.connect(left.id,ConstraintSet.LEFT,center.id,ConstraintSet.RIGHT,0)
                constraintSet.connect(left.id,ConstraintSet.RIGHT,right.id,ConstraintSet.LEFT,0)

                constraintSet.connect(right.id,ConstraintSet.LEFT,left.id,ConstraintSet.RIGHT,0)
                constraintSet.connect(right.id,ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,0)

                constraintSet.applyTo(root_layout)
            }
            1 -> {
            }
            2 -> {
            }
            3 -> {
            }
        }
        //完成以后重置mOperationIndex
        mOperationIndex = -1
    }
    //在父布局的四个角显示操作符按钮
    private fun hideOptionView() {
        mArrowList.forEach {
            hideView(it)
        }
    }

    override fun onFinishLift() {
        mIsDrawing = false
        //判断此时的状态
        hideView(mAnimItemView)
        hideOptionView()
        if (mOperationView != null) {
            mOperationView!!.visibility = View.VISIBLE
        }
        if (mOperationIndex != -1) {
            onOption(mOperationIndex)
        }
    }

    //移除view
    private fun hideView(view: View) {
        if ( contains(view)) {
            removeView(view)
        }
    }


    //显示view
    private fun showView(view: View) {
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        hideView(view)
        addView(view, lp)
    }


}