package com.ng.nguilib

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.ng.nguilib.utils.DensityUtil
import com.ng.nguilib.utils.MLog

/**
 * @ProjectName: NguiLib
 * @Package: com.ng.nguilib
 * @Description:https://dribbble.com/shots/6201452-Arrow-micro-interaction
 * @Author: Eden
 * @CreateDate: 2019/6/24 16:54
 */
class ArrowInteractionView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    //common
    private lateinit var paintLine: Paint

    private lateinit var paintCircle: Paint

    private lateinit var paintBg: Paint

    private var mCircleWidth: Float = 0.0f

    private var SHOW_MODEL = 0
    val SHOW_MODEL_LEFT = 0x00
    val SHOW_MODEL_RIGHT = 0x01

    private var ANIM_STEP = 0x01
    private val ANIM_STEP_1 = 0x01
    private val ANIM_STEP_2 = 0x02
    private val ANIM_STEP_3 = 0x03
    private val ANIM_STEP_4 = 0x04
    private val animSteps = arrayListOf(ANIM_STEP_1, ANIM_STEP_2, ANIM_STEP_3, ANIM_STEP_4)


    val TIME_STEP: Long = 400
    private var animatorSet: AnimatorSet? = null
    private var mSideLength: Float = 0.toFloat()
    private var mRadius: Float = 0.toFloat()


    //triangle
    private var pointX: Float = 0.toFloat()
    private var pointY: Float = 0.toFloat()
    private var mTriangleSideLength: Float = 0.toFloat()
    private lateinit var path: Path

    //circle
    private var roundRF: RectF? = null
    private var startAngle1: Float = 0.toFloat()
    private var swipeAngle1: Float = 0.toFloat()
    private var startAngle2: Float = 0.toFloat()
    private var swipeAngle2: Float = 0.toFloat()

    private var hadInit: Boolean = false
    private var isPlaying: Boolean = false

    fun setModel(model: Int) {
        if (SHOW_MODEL == SHOW_MODEL_LEFT || SHOW_MODEL == SHOW_MODEL_RIGHT) {
            this.SHOW_MODEL = model
            postInvalidate()
        } else {
            try {
                throw Exception("error model")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun init() {
        hadInit = true
        MLog.d("init")
        paintLine = Paint()
        paintCircle = Paint()
        paintBg = Paint()
        this.animatorSet = AnimatorSet()
        mRadius = mSideLength / 2
        mCircleWidth = DensityUtil.dip2pxFloat(context, 3f)
        roundRF = RectF(0 + mCircleWidth / 2,
                0 + mCircleWidth / 2,
                mSideLength - mCircleWidth / 2,
                mSideLength - mCircleWidth / 2)
        //paint
        paintLine.style = Paint.Style.STROKE
        paintLine.color = Color.parseColor("#ffffff")
        paintLine.strokeWidth = mCircleWidth
        paintLine.isAntiAlias = true
        paintLine.strokeCap = Paint.Cap.ROUND
        paintLine.strokeJoin = Paint.Join.ROUND
        //triangle
        pointX = mRadius
        pointY = mRadius
        mTriangleSideLength = mRadius / 6
        path = Path()

        postInvalidate()
    }

    fun startAnim() {
        if (isPlaying) return
        isPlaying = true
        when (SHOW_MODEL) {
            SHOW_MODEL_LEFT -> initLeft()
            SHOW_MODEL_RIGHT -> initRight()
        }
    }

    private fun initLeft() {
        //startAnimSquare
        startAnimByStep(4, object : OnAnimationUpdatePLView {
            override fun onUpdate(step: Int, fraction: Float) {
                ANIM_STEP = animSteps[step - 1]
                path.reset()
                when (step) {
                    1 -> {
                        pointX = mRadius * (1 - fraction)
                        mTriangleSideLength = mRadius / 6 * (1 - fraction)
                    }
                    2 -> {
                        startAngle1 = 180f
                        swipeAngle1 = 180f * fraction
                        startAngle2 = 180f
                        swipeAngle2 = -180f * fraction
                    }
                    3 -> {
                        startAngle1 = 180f + 180 * fraction
                        swipeAngle1 = 180f * (1 - fraction)
                        startAngle2 = 180f - 180 * fraction
                        swipeAngle2 = -180f * (1 - fraction)
                    }
                    4 -> {
                        pointX = mSideLength - mRadius * fraction
                        mTriangleSideLength = mRadius / 6 * fraction
                    }
                }
            }
        })
    }


    private fun initRight() {
        startAngle1 = 0f
        startAngle2 = 0f
        //startAnimSquare
        startAnimByStep(4, object : OnAnimationUpdatePLView {
            override fun onUpdate(step: Int, fraction: Float) {
                ANIM_STEP = animSteps[step - 1]
                path.reset()
                when (step) {
                    1 -> {
                        pointX = mRadius + mRadius * fraction
                        mTriangleSideLength = mRadius / 6 * (1 - fraction)
                    }
                    2 -> {
                        startAngle1 = 0f
                        swipeAngle1 = 180f * fraction
                        startAngle2 = 0f
                        swipeAngle2 = -180f * fraction
                    }
                    3 -> {
                        startAngle1 = 0f + 180 * fraction
                        swipeAngle1 = 180f * (1 - fraction)
                        startAngle2 = 0f - 180 * fraction
                        swipeAngle2 = -180f * (1 - fraction)
                    }
                    4 -> {
                        pointX = mRadius * fraction
                        mTriangleSideLength = mRadius / 6 * fraction
                    }
                }
            }
        })
    }


    private fun startAnimByStep(step: Int, listener: OnAnimationUpdatePLView) {
        val interpolator = DecelerateInterpolator(1f)
        val pointAnimList = mutableListOf<Animator>()
        for (index in 1..step) {
            val pointAnimatorTemp = ValueAnimator.ofFloat(0f, 100f)
            pointAnimatorTemp.duration = this.TIME_STEP
            pointAnimatorTemp.interpolator = interpolator
            pointAnimatorTemp.startDelay = 30
            pointAnimatorTemp.addUpdateListener { animation ->
                val temp = animation.animatedFraction
                listener.onUpdate(index, temp)
                invalidate()
            }
            pointAnimList.add(pointAnimatorTemp)
        }
        animatorSet!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                isPlaying = false
            }
        })
        animatorSet!!.playSequentially(pointAnimList)
        animatorSet!!.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mSideLength = (if (measuredWidth > measuredHeight) measuredHeight else measuredWidth).toFloat()
        //宽必须等于高
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!hadInit) {
            init()
        }

        drawCircle(canvas)
        when (SHOW_MODEL) {
            SHOW_MODEL_LEFT -> drawLeft(canvas)
            SHOW_MODEL_RIGHT -> drawRight(canvas)
            else -> {
            }
        }
    }

    private fun drawCircle(canvas: Canvas) {
        //2452FEpaintBg
        paintBg.isAntiAlias = true
        paintBg.color = Color.parseColor("#2452FE")
        paintBg.style = Paint.Style.FILL
        paintBg.strokeWidth = mRadius
        canvas.drawCircle(mRadius, mRadius, mRadius, paintBg)

        paintCircle.isAntiAlias = true
        paintCircle.color = Color.parseColor("#66ffffff")
        paintCircle.style = Paint.Style.STROKE
        paintCircle.strokeWidth = mCircleWidth
        paintCircle.strokeCap = Paint.Cap.ROUND
        canvas.drawCircle(mRadius, mRadius, mRadius - mCircleWidth / 2, paintCircle)


    }

    private fun drawLeft(canvas: Canvas) {
        when (ANIM_STEP) {
            ANIM_STEP_1 -> {
                path.moveTo(pointX + mTriangleSideLength, pointY - mTriangleSideLength * 1.4f)
                path.lineTo(pointX - mTriangleSideLength, pointY)
                path.lineTo(pointX + mTriangleSideLength, pointY + mTriangleSideLength * 1.4f)
                canvas.drawPath(path, paintLine)
            }
            ANIM_STEP_2 -> {
                roundRF?.let { canvas.drawArc(it, startAngle1, swipeAngle1, false, paintLine) }
                roundRF?.let { canvas.drawArc(it, startAngle2, swipeAngle2, false, paintLine) }
            }
            ANIM_STEP_3 -> {
                roundRF?.let { canvas.drawArc(it, startAngle1, swipeAngle1, false, paintLine) }
                roundRF?.let { canvas.drawArc(it, startAngle2, swipeAngle2, false, paintLine) }
            }
            ANIM_STEP_4 -> {
                path.moveTo(pointX + mTriangleSideLength, pointY - mTriangleSideLength * 1.4f)
                path.lineTo(pointX - mTriangleSideLength, pointY)
                path.lineTo(pointX + mTriangleSideLength, pointY + mTriangleSideLength * 1.4f)
                canvas.drawPath(path, paintLine)
            }
        }
    }

    private fun drawRight(canvas: Canvas) {
        when (ANIM_STEP) {
            ANIM_STEP_1 -> {
                path.moveTo(pointX - mTriangleSideLength, pointY - mTriangleSideLength * 1.4f)
                path.lineTo(pointX + mTriangleSideLength, pointY)
                path.lineTo(pointX - mTriangleSideLength, pointY + mTriangleSideLength * 1.4f)
                canvas.drawPath(path, paintLine)
            }
            ANIM_STEP_2 -> {
                roundRF?.let { canvas.drawArc(it, startAngle1, swipeAngle1, false, paintLine) }
                roundRF?.let { canvas.drawArc(it, startAngle2, swipeAngle2, false, paintLine) }
            }
            ANIM_STEP_3 -> {
                roundRF?.let { canvas.drawArc(it, startAngle1, swipeAngle1, false, paintLine) }
                roundRF?.let { canvas.drawArc(it, startAngle2, swipeAngle2, false, paintLine) }
            }
            ANIM_STEP_4 -> {
                path.moveTo(pointX - mTriangleSideLength, pointY - mTriangleSideLength * 1.4f)
                path.lineTo(pointX + mTriangleSideLength, pointY)
                path.lineTo(pointX - mTriangleSideLength, pointY + mTriangleSideLength * 1.4f)
                canvas.drawPath(path, paintLine)
            }
        }
    }

    interface OnAnimationUpdatePLView {
        fun onUpdate(step: Int, fraction: Float)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
    }

    override fun setVisibility(visibility: Int) {
        val currentVisibility = getVisibility()
        super.setVisibility(visibility)
        if (visibility != currentVisibility) {
            if (visibility == VISIBLE) {
                startAnimation()
            } else if (visibility == GONE || visibility == INVISIBLE) {
                stopAnimation()
            }
        }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == VISIBLE) {
            startAnimation()
        } else {
            stopAnimation()
        }
    }

    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
        if (gainFocus) {
            startAnimation()
        } else {
            stopAnimation()
        }
    }

    //应该绑定activity生命周期
    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun stopAnimation() {
        if (animatorSet != null) {
            animatorSet?.cancel()
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun startAnimation() {
        if (animatorSet != null && !animatorSet!!.isStarted && !animatorSet!!.isRunning) {
            animatorSet!!.start()
        }
    }

}