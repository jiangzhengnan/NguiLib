package com.ng.nguilib.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.animation.DecelerateInterpolator
import android.animation.AnimatorSet
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.animation.AccelerateInterpolator
import android.graphics.RectF
import android.util.Log
import com.ng.nguilib.utils.DensityUtil


/**
 * 描述:
 * @author Jzn
 * @date 2019-12-14
 */
class PointLoadingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val STATE_RUNNING = 0x1
    private val STATE_STOP = 0x2
    private var mAnimState = 0x2

    private var mHeight: Float = 0.toFloat()
    private var mPointHeight: FloatArray? = null
    private var mWidth: Float = 0.toFloat()
    private var mPointSpace: Float = 0.toFloat()

    //time
    private val TIME_ALL_INTERVAL: Long = 300
    private val TIME_POINT_INTERVAL: Long = 200
    private val TIME_DOWN_ANIM: Long = 300
    private val TIME_UP_ANIM: Long = 600

    //R of point
    private var mPointDiameter: Float = 0f
    private val POINT_DIAMETER = 12f


    //default point size
    private val POINT_SIZE = 3


    //tools
    private var mPointPaint: Paint = Paint()
    private var mBgPaint: Paint = Paint()
    private var mDownAnimSet: AnimatorSet? = null
    private var mUpAnimSet: AnimatorSet? = null

    //color
    private val DEFAULT_BG_COLOR = Color.WHITE
    private val DEFAULT_POINT_COLOR = Color.BLACK


    fun setColor(bgColor: Int, pointColor: Int) {
        mBgPaint.color = bgColor
        mPointPaint.color = pointColor

    }

    init {
        init(getContext())
    }


    private fun init(context: Context) {
        mAnimState = STATE_STOP
        mPointDiameter = DensityUtil.dip2px(context, POINT_DIAMETER).toFloat()
        mPointHeight = FloatArray(POINT_SIZE)
        initPaint()
    }

    private fun initPaint() {
        mPointPaint = Paint()
        mPointPaint.color = DEFAULT_POINT_COLOR
        mPointPaint.strokeWidth = mPointDiameter
        mPointPaint.isAntiAlias = true
        mPointPaint.strokeCap = Paint.Cap.ROUND
        mPointPaint.style = Paint.Style.FILL
        mBgPaint.isAntiAlias = true
        mBgPaint.style = Paint.Style.FILL
        mBgPaint.color = DEFAULT_BG_COLOR
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBg(canvas)
        drawPoints(canvas)
    }

    private fun drawBg(canvas: Canvas) {
        val bgRectF = RectF(0f, 0f, mWidth, mHeight)
        canvas.drawRoundRect(bgRectF, mHeight / 2, mHeight / 2, this.mBgPaint)
    }

    private fun drawPoints(canvas: Canvas) {
        for (i in 0 until POINT_SIZE) {
            canvas.drawPoint((i + 1) * mPointSpace, mPointHeight!![i], mPointPaint)
        }
    }


    private fun initAnim() {
        logd("initAnim")
        //down
        if (mDownAnimSet == null) {
            mDownAnimSet = AnimatorSet()
            val mDownAnimList = ArrayList<ValueAnimator>()
            for (i in 0 until POINT_SIZE) {
                val tempDownAmt = ValueAnimator.ofFloat(0f, 100f)
                tempDownAmt.duration = TIME_DOWN_ANIM
                tempDownAmt.interpolator = AccelerateInterpolator()
                tempDownAmt.startDelay = i * TIME_POINT_INTERVAL
                tempDownAmt.addUpdateListener { animation ->
                    val temp = animation.animatedFraction
                    mPointHeight?.set(i, mHeight / 2 + (mHeight / 2 + mPointDiameter) * temp)
                    postInvalidate()
                }
                mDownAnimList.add(tempDownAmt)
            }
            mDownAnimSet!!.playTogether(mDownAnimList as Collection<Animator>?)
            mDownAnimSet!!.startDelay = TIME_ALL_INTERVAL
            mDownAnimSet!!.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    if (mAnimState != STATE_STOP)
                        mUpAnimSet!!.start()
                }
            })
        }
        //up
        if (mUpAnimSet == null) {
            mUpAnimSet = AnimatorSet()
            val mUpAnimList = ArrayList<ValueAnimator>()
            for (i in 0 until POINT_SIZE) {
                val tempUpAmt = ValueAnimator.ofFloat(0f, 100f)
                tempUpAmt.duration = TIME_UP_ANIM
                tempUpAmt.interpolator = DecelerateInterpolator()
                tempUpAmt.startDelay = i * TIME_POINT_INTERVAL
                tempUpAmt.addUpdateListener { animation ->
                    val temp = animation.animatedFraction
                    mPointHeight?.set(i, mHeight + mPointDiameter - (mHeight / 2 + mPointDiameter) * temp)
                    postInvalidate()
                }
                mUpAnimList.add(tempUpAmt)
            }
            mUpAnimSet!!.playTogether(mUpAnimList as Collection<Animator>?)
            mUpAnimSet!!.startDelay = TIME_ALL_INTERVAL
            mUpAnimSet!!.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    if (mAnimState != STATE_STOP)
                        mDownAnimSet!!.start()
                }
            })
        }
    }


    fun startLoadingAnim() {
        initAnim();
        if (mDownAnimSet != null && mAnimState == STATE_STOP) {
            mAnimState = STATE_RUNNING
            mDownAnimSet!!.start()
        }
    }

    fun stopLoadingAnim() {
        mAnimState = STATE_STOP
        if (mDownAnimSet != null) {
            mDownAnimSet!!.cancel()
            mDownAnimSet!!.end()
        }
        if (mUpAnimSet != null) {
            mUpAnimSet!!.cancel()
            mUpAnimSet!!.end()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopLoadingAnim()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        logd("visibility: $visibility")
        if (visibility == VISIBLE) {
            startLoadingAnim()
        } else {
            stopLoadingAnim()
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mHeight = measuredHeight.toFloat()
        mWidth = measuredWidth.toFloat()
        mPointSpace = mWidth / (POINT_SIZE + 1)
        for (i in 0 until POINT_SIZE) {
            mPointHeight?.set(i, mHeight / 2)
        }
        initAnim()
        logd("$mHeight   $mWidth   $mPointSpace")
    }

    fun logd(txt: String) {
        Log.d("nangua", txt)
    }
}