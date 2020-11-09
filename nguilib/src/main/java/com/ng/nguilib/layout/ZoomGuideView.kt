package com.ng.nguilib.layout

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.Path.FillType
import android.view.MotionEvent
import android.view.View
import com.ng.nguilib.utils.ViewUtils


/**
 * 描述:
 * @author Jzn
 * @date 2020/11/6
 */
class ZoomGuideView(context: Context?) : View(context) {

    private var mPaint = Paint()
    private var isExpend = false

    private var mColor = Color.parseColor("#47B1FB")

    //上下的线
    private var WIDTH_LINE = 10
    private var MAX_LENGTH_LINE = 300
    private var mLengthLine = 0f

    //中心圆圈
    private var MAX_CIRCLE_R = 50
    private var mCircleR = 0f

    //三根横线
    private var MAX_WIDTH_THREE_LINE = 3f

    //左右箭头　
    private var MAX_LENGTH_TRIANGLE = 200
    private var mLengthTriangle = 0f

    //三角形变长
    private var TRIANGLE_SIDE = 60

    //三角形柱长
    private var TRIANGLE_LENGTH = 100


    private var mAnimator: ValueAnimator

    private var mShowY = 0f;

    init {
        WIDTH_LINE = ViewUtils.dip2px(context,5f)
        MAX_LENGTH_LINE = ViewUtils.dip2px(context,100f)
        MAX_CIRCLE_R = ViewUtils.dip2px(context,20f)
        MAX_LENGTH_TRIANGLE = ViewUtils.dip2px(context,60f)
        TRIANGLE_SIDE = ViewUtils.dip2px(context,25f)
        TRIANGLE_LENGTH = ViewUtils.dip2px(context,40f)

        mPaint = Paint();
        mPaint.color = mColor
        mPaint.strokeWidth = 100f
        mPaint.isAntiAlias = true
        mPaint.flags = Paint.ANTI_ALIAS_FLAG
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.style = Paint.Style.FILL

        mAnimator = ValueAnimator.ofInt(0, 100)
        mAnimator.duration = 250
        //mAnimator.interpolator = OvershootInterpolator()
    }


    fun setShowY(showY: Float) {
        mShowY = showY
    }

    fun expend(expend: Boolean) {
        if (!isExpend && expend) {
            //展开
            isExpend = true
            openAnim()
        } else if (isExpend && !expend) {
            //收起
            endAnim()
        }
        invalidate()
    }

    public interface OnMyTouchListener {
        fun onMyTouchEvent(view: View, event: MotionEvent):Boolean
    }

    var onCallBack: OnMyTouchListener? = null

    public fun setOnMyTouchListener(temp: OnMyTouchListener) {
        this.onCallBack = temp
    }


    fun onMyTouchEvent(view: View,event: MotionEvent):Boolean {
        return onCallBack!!.onMyTouchEvent(view,event)
    }

    private fun endAnim() {
        mAnimator.addUpdateListener(AnimatorUpdateListener { animation ->
            mCircleR = MAX_CIRCLE_R * (1 - animation.animatedFraction)
            mLengthLine = MAX_LENGTH_LINE * (1 - animation.animatedFraction)
            mLengthTriangle = MAX_LENGTH_TRIANGLE * (1 - animation.animatedFraction)

            invalidate()
        })
        mAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                isExpend = false
            }
        })
        mAnimator.start()
    }

    private fun openAnim() {
        mAnimator.addUpdateListener(AnimatorUpdateListener { animation ->
            mCircleR = MAX_CIRCLE_R * animation.animatedFraction
            mLengthLine = MAX_LENGTH_LINE * animation.animatedFraction
            mLengthTriangle = MAX_LENGTH_TRIANGLE * animation.animatedFraction

            invalidate()
        })
        mAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                isExpend = true
            }
        })
        mAnimator.start()
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isExpend) {
            val centerX = measuredWidth / 2f
            val centerY = measuredHeight / 2f

            //画圈
            mPaint.shader = null
            mPaint.color = mColor
            mPaint.strokeWidth = 100f
            canvas.drawCircle(centerX, mShowY, mCircleR, mPaint)
            //画上下的线
            val mGradient = RadialGradient(
                    centerX, mShowY, MAX_LENGTH_LINE.toFloat(),
                    mColor,
                    Color.TRANSPARENT,
                    Shader.TileMode.CLAMP)
            mPaint.shader = mGradient
            canvas.drawRect(centerX - WIDTH_LINE, mShowY,
                    centerX + WIDTH_LINE, mShowY - mLengthLine, mPaint
            )
            canvas.drawRect(centerX - WIDTH_LINE, mShowY + mLengthLine,
                    centerX + WIDTH_LINE, mShowY, mPaint
            )
            //画三根线
            mPaint.shader = null
            mPaint.color = Color.WHITE
            val interval = mCircleR / 2
            mPaint.strokeWidth = MAX_WIDTH_THREE_LINE
            canvas.drawLine(centerX - interval, mShowY - interval,
                    centerX - interval, mShowY + interval, mPaint)
            canvas.drawLine(centerX, mShowY - interval,
                    centerX, mShowY + interval, mPaint)
            canvas.drawLine(centerX + interval, mShowY - interval,
                    centerX + interval, mShowY + interval, mPaint)
            //画左右两个三角形
            val mTriangleGradient = RadialGradient(
                    centerX, mShowY, MAX_LENGTH_TRIANGLE.toFloat() * 2,
                    intArrayOf(Color.TRANSPARENT, Color.TRANSPARENT,  mColor),
                    null,
                    Shader.TileMode.CLAMP)
            mPaint.shader = mTriangleGradient
            var startX = centerX - mLengthTriangle
            val path = Path()
            path.fillType = FillType.WINDING
            path.moveTo(startX, mShowY)
            path.lineTo(startX, mShowY + interval / 2)
            path.lineTo(startX - TRIANGLE_SIDE, mShowY + interval / 2)
            path.lineTo(startX - TRIANGLE_SIDE, mShowY + interval)
            path.lineTo(startX - TRIANGLE_LENGTH, mShowY)
            path.lineTo(startX - TRIANGLE_SIDE, mShowY - interval)
            path.lineTo(startX - TRIANGLE_SIDE, mShowY - interval / 2)
            path.lineTo(startX, mShowY - interval / 2)
            path.lineTo(startX, mShowY)
            path.close()
            canvas.drawPath(path, mPaint)
            startX = centerX + mLengthTriangle
            path.fillType = FillType.WINDING
            path.moveTo(startX, mShowY)
            path.lineTo(startX, mShowY + interval / 2)
            path.lineTo(startX + TRIANGLE_SIDE, mShowY + interval / 2)
            path.lineTo(startX + TRIANGLE_SIDE, mShowY + interval)
            path.lineTo(startX + TRIANGLE_LENGTH, mShowY)
            path.lineTo(startX + TRIANGLE_SIDE, mShowY - interval)
            path.lineTo(startX + TRIANGLE_SIDE, mShowY - interval / 2)
            path.lineTo(startX, mShowY - interval / 2)
            path.lineTo(startX, mShowY)
            path.close()
            canvas.drawPath(path, mPaint)
        }
    }


}