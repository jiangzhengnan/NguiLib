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
import android.view.animation.AccelerateInterpolator
import com.ng.nguilib.utils.LogUtils


class PolygonLoadView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    //common
    private lateinit var paintLine: Paint
    private lateinit var paintPoint: Paint
    private   var roundRF: RectF? = null
    private val mGridLinestrokeWidth = 30f
    private var SHOW_MODEL = 0
    val SHOW_MODEL_ROUND = 0x00
    val SHOW_MODEL_TRIANGLE = 0x01
    val SHOW_MODEL_SQUARE = 0x02
    val TIME_CIRCLE: Long = 3000
    private var animatorSet: AnimatorSet? = null
    private var mSideLength: Float = 0.toFloat()
    private var mHalfSH: Float = 0.toFloat()
    private var thickness: Float = 0.toFloat()

    //round
    private var pointX: Float = 0.toFloat()
    private var pointY: Float = 0.toFloat()
    private var startAngle: Float = 0.toFloat()
    private val swipeAngle = 270f
    //triangle square
    private lateinit var path: Path
    private var mHalfHeifht: Float = 0.toFloat()
    private var startLineX: Float = 0.toFloat()
    private var startLineY: Float = 0.toFloat()
    private var endLineX: Float = 0.toFloat()
    private var endLineY: Float = 0.toFloat()


    fun setModel(model: Int) {
        if (SHOW_MODEL == SHOW_MODEL_ROUND || SHOW_MODEL == SHOW_MODEL_TRIANGLE || SHOW_MODEL == SHOW_MODEL_SQUARE) {
            this.SHOW_MODEL = model
            init()
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
        paintLine = Paint()
        paintPoint = Paint()
        animatorSet = AnimatorSet()
        mHalfSH = mSideLength / 2
        thickness = mGridLinestrokeWidth / 2
        when (SHOW_MODEL) {
            SHOW_MODEL_ROUND -> initRound()
            SHOW_MODEL_TRIANGLE -> initTriangle()
            SHOW_MODEL_SQUARE -> initSquare()
        }

    }

    private fun initSquare() {
        //paint
        paintLine.style = Paint.Style.STROKE
        paintLine.color = Color.parseColor("#2D283C")
        paintLine.strokeWidth = mGridLinestrokeWidth
        paintLine.isAntiAlias = true
        paintLine.strokeCap = Paint.Cap.ROUND
        paintLine.strokeJoin = Paint.Join.ROUND
        roundRF = RectF(0 + mGridLinestrokeWidth / 2,
                0 + mGridLinestrokeWidth / 2,
                mSideLength - mGridLinestrokeWidth / 2,
                mSideLength - mGridLinestrokeWidth / 2)
        paintPoint.isAntiAlias = true
        paintPoint.color = Color.parseColor("#4A22EA")
        paintPoint.style = Paint.Style.STROKE
        paintPoint.strokeWidth = mGridLinestrokeWidth
        paintPoint.strokeCap = Paint.Cap.ROUND
        //point
        pointX = mHalfSH
        pointY = 2 * mHalfSH - thickness
        //line
        path = Path()
        startLineX = thickness
        startLineY = mHalfSH * 2 - thickness
        endLineX = mHalfSH * 2 - thickness
        endLineY = mHalfSH * 2 - thickness
        path.moveTo(startLineX, startLineY)
        path.lineTo(thickness, thickness)
        path.lineTo(mHalfSH * 2 - thickness, thickness)
        path.lineTo(endLineX, endLineY)
        //startAnimSquare
        startAnimByStep(4, object : OnAnimationUpdatePLView {
            override fun onUpdate(step: Int, fraction: Float) {
                path.reset()
                when (step) {
                    1 -> {
                        pointX = mHalfSH + fraction * (mHalfSH - thickness)
                        pointY = mSideLength - thickness - fraction * (mHalfSH - thickness)

                        startLineX = thickness + fraction * (2 * mHalfSH - 2 * thickness)
                        startLineY = mHalfSH * 2 - thickness
                        endLineX = mHalfSH * 2 - thickness
                        endLineY = mHalfSH * 2 - thickness - fraction * (2 * mHalfSH - 2 * thickness)
                        path.moveTo(startLineX, startLineY)
                        path.lineTo(thickness, mHalfSH * 2 - thickness)
                        path.lineTo(thickness, thickness)
                        path.lineTo(mHalfSH * 2 - thickness, thickness)
                        path.lineTo(endLineX, endLineY)
                    }
                    2 -> {
                        pointX = mSideLength - fraction * (mHalfSH - thickness) - thickness
                        pointY = mHalfSH - fraction * (mHalfSH - thickness)

                        startLineX = 2 * mHalfSH - thickness
                        startLineY = mHalfSH * 2 - thickness - fraction * (2 * mHalfSH - 2 * thickness)
                        endLineX = mHalfSH * 2 - thickness - fraction * (2 * mHalfSH - 2 * thickness)
                        endLineY = thickness
                        path.moveTo(startLineX, startLineY)
                        path.lineTo(mHalfSH * 2 - thickness, mHalfSH * 2 - thickness)
                        path.lineTo(thickness, mHalfSH * 2 - thickness)
                        path.lineTo(thickness, thickness)
                        path.lineTo(endLineX, endLineY)
                    }
                    3 -> {
                        pointX = mHalfSH - fraction * (mHalfSH - thickness)
                        pointY = thickness + fraction * (mHalfSH - thickness)

                        //start 右上往左 end 左上往下
                        startLineX = 2 * mHalfSH - thickness - fraction * (2 * mHalfSH - 2 * thickness)
                        startLineY = thickness
                        endLineX = thickness
                        endLineY = thickness + fraction * (2 * mHalfSH - 2 * thickness)
                        path.moveTo(startLineX, startLineY)
                        path.lineTo(mHalfSH * 2 - thickness, thickness)
                        path.lineTo(mHalfSH * 2 - thickness, mHalfSH * 2 - thickness)
                        path.lineTo(thickness, mHalfSH * 2 - thickness)
                        path.lineTo(endLineX, endLineY)
                    }
                    4 -> {
                        pointX = thickness + fraction * (mHalfSH - thickness)
                        pointY = mHalfSH + fraction * (mHalfSH - thickness)

                        startLineX = thickness
                        startLineY = thickness + fraction * (2 * mHalfSH - 2 * thickness)
                        endLineX = thickness + fraction * (2 * mHalfSH - 2 * thickness)
                        endLineY = 2 * mHalfSH - thickness
                        path.moveTo(startLineX, startLineY)
                        path.lineTo(thickness, thickness)
                        path.lineTo(mHalfSH * 2 - thickness, thickness)
                        path.lineTo(mHalfSH * 2 - thickness, mHalfSH * 2 - thickness)
                        path.lineTo(endLineX, endLineY)

                    }
                }
            }
        })
    }


    private fun initTriangle() {
        //paint
        paintLine.style = Paint.Style.STROKE
        paintLine.color = Color.parseColor("#2D283C")
        paintLine.strokeWidth = mGridLinestrokeWidth
        paintLine.isAntiAlias = true
        paintLine.strokeCap = Paint.Cap.ROUND
        paintLine.strokeJoin = Paint.Join.ROUND
        roundRF = RectF(0 + mGridLinestrokeWidth / 2,
                0 + mGridLinestrokeWidth / 2,
                mSideLength - mGridLinestrokeWidth / 2,
                mSideLength - mGridLinestrokeWidth / 2)
        paintPoint.isAntiAlias = true
        paintPoint.color = Color.parseColor("#4A22EA")
        paintPoint.style = Paint.Style.STROKE
        paintPoint.strokeWidth = mGridLinestrokeWidth
        paintPoint.strokeCap = Paint.Cap.ROUND
        //point
        pointX = mHalfSH
        pointY = 2 * mHalfSH - thickness
        mHalfHeifht = (mHalfSH * 0.87).toFloat()
        //line
        path = Path()
        startLineX = thickness
        startLineY = mHalfSH * 2 - thickness
        endLineX = mHalfSH * 2 - thickness
        endLineY = mHalfSH * 2 - thickness
        path.moveTo(startLineX, startLineY)
        path.lineTo(mHalfSH, thickness)
        path.lineTo(endLineX, endLineY)
        // startAnimTriangle
        startAnimByStep(3, object : OnAnimationUpdatePLView {
            override fun onUpdate(step: Int, fraction: Float) {
                path.reset()
                when (step) {
                    1 -> {
                        pointX = mHalfSH + fraction * (mHalfSH / 2 - thickness)
                        pointY = 2 * mHalfSH - thickness - fraction * (mHalfSH - thickness)
                        startLineX = thickness + fraction * (2 * mHalfSH - 2 * thickness)
                        startLineY = mHalfSH * 2 - thickness
                        endLineX = mHalfSH * 2 - thickness - fraction * (mHalfSH - thickness)
                        endLineY = mHalfSH * 2 - thickness - fraction * (2 * mHalfSH - 2 * thickness)
                        path.moveTo(startLineX, startLineY)
                        path.lineTo(thickness, mHalfSH * 2 - thickness)
                        path.lineTo(mHalfSH, thickness)
                        path.lineTo(endLineX, endLineY)
                    }
                    2 -> {
                        pointX = mHalfSH * 3 / 2 - thickness - fraction * (mHalfSH - 2 * thickness)
                        pointY = mHalfSH
                        startLineX = 2 * mHalfSH - thickness - fraction * (mHalfSH - thickness)
                        startLineY = mHalfSH * 2 - thickness - fraction * (2 * mHalfSH - 2 * thickness)
                        endLineX = mHalfSH - fraction * (mHalfSH - thickness)
                        endLineY = thickness + fraction * (2 * mHalfSH - 2 * thickness)
                        path.moveTo(startLineX, startLineY)
                        path.lineTo(mHalfSH * 2 - thickness, mHalfSH * 2 - thickness)
                        path.lineTo(thickness, mHalfSH * 2 - thickness)
                        path.lineTo(endLineX, endLineY)

                    }
                    3 -> {
                        pointX = mHalfSH / 2 + thickness + fraction * (mHalfSH / 2 - thickness)
                        pointY = mHalfSH + fraction * (mHalfSH - thickness)
                        startLineX = mHalfSH - fraction * (mHalfSH - thickness)
                        startLineY = thickness + fraction * (2 * mHalfSH - 2 * thickness)
                        endLineX = thickness + fraction * (mHalfSH * 2 - 2 * thickness)
                        endLineY = mHalfSH * 2 - thickness
                        path.moveTo(startLineX, startLineY)
                        path.lineTo(mHalfSH, thickness)
                        path.lineTo(mHalfSH * 2 - thickness, mHalfSH * 2 - thickness)
                        path.lineTo(endLineX, endLineY)

                    }
                }
            }
        })
    }

    private fun initRound() {
        //paint
        paintLine.style = Paint.Style.STROKE
        paintLine.color = Color.parseColor("#2D283C")
        paintLine.strokeWidth = mGridLinestrokeWidth
        paintLine.isAntiAlias = true
        paintLine.strokeCap = Paint.Cap.ROUND
        roundRF = RectF(0 + mGridLinestrokeWidth / 2,
                0 + mGridLinestrokeWidth / 2,
                mSideLength - mGridLinestrokeWidth / 2,
                mSideLength - mGridLinestrokeWidth / 2)
        paintPoint.isAntiAlias = true
        paintPoint.color = Color.parseColor("#4A22EA")
        paintPoint.style = Paint.Style.STROKE
        paintPoint.strokeWidth = mGridLinestrokeWidth
        paintPoint.strokeCap = Paint.Cap.ROUND
        //point
        pointX = mHalfSH
        pointY = mHalfSH * 2 - thickness
        startAngle = 225f
        // startAnimRound()
        startAnimByStep(4, object : OnAnimationUpdatePLView {
            override fun onUpdate(step: Int, fraction: Float) {
                when (step) {
                    1 -> {
                        pointX = mHalfSH + fraction * (mHalfSH - thickness)
                        pointY = mSideLength - thickness - fraction * (mHalfSH - thickness)
                        startAngle = 135f - fraction * 90
                    }
                    2 -> {
                        pointX = mSideLength - fraction * (mHalfSH - thickness) - thickness
                        pointY = mHalfSH - fraction * (mHalfSH - thickness)
                        startAngle = if (startAngle > 0) {
                            45 - fraction * 90
                        } else {
                            405 - fraction * 90
                        }
                    }
                    3 -> {
                        pointX = mHalfSH - fraction * (mHalfSH - thickness)
                        pointY = thickness + fraction * (mHalfSH - thickness)
                        startAngle = 315f - fraction * 90
                    }
                    4 -> {
                        pointX = thickness + fraction * (mHalfSH - thickness)
                        pointY = mHalfSH + fraction * (mHalfSH - thickness)
                        startAngle = 225f - fraction * 90
                    }
                }
            }
        })
    }

    private fun startAnimByStep(step: Int, listener: OnAnimationUpdatePLView) {
        val interpolator = AccelerateInterpolator(1f)
        val pointAnimList = mutableListOf<Animator>()
        for (index in 1..step) {
            val pointAnimatorTemp = ValueAnimator.ofFloat(0f, 100f)
            pointAnimatorTemp.duration = this.TIME_CIRCLE / 4
            pointAnimatorTemp.interpolator = interpolator
            pointAnimatorTemp.startDelay = 30//制造停顿感
            pointAnimatorTemp.addUpdateListener { animation ->
                val temp = animation.animatedFraction
                listener.onUpdate(index, temp)
                invalidate()
            }
            pointAnimList.add(pointAnimatorTemp)
        }
        animatorSet!!.playSequentially(pointAnimList)
        animatorSet!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                animatorSet!!.start()
            }
        })
        animatorSet!!.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mSideLength = (if (measuredWidth > measuredHeight) measuredHeight else measuredWidth).toFloat()
        //宽必须等于高
        LogUtils.d("宽： $mSideLength  高：  $mSideLength")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (SHOW_MODEL) {
            SHOW_MODEL_ROUND -> drawRound(canvas)
            SHOW_MODEL_TRIANGLE -> drawTriangle(canvas)
            SHOW_MODEL_SQUARE -> drawSquare(canvas)
            else -> {
            }
        }
    }

    private fun drawSquare(canvas: Canvas) {
        canvas.drawPath(path, paintLine)
        canvas.drawPoint(pointX, pointY, paintPoint)
    }

    private fun drawTriangle(canvas: Canvas) {
        canvas.drawPath(path, paintLine)
        canvas.drawPoint(pointX, pointY, paintPoint)
    }

    private fun drawRound(canvas: Canvas) {
        if (roundRF==null) {
            return
        }
        canvas.drawArc(roundRF, startAngle, swipeAngle, false, paintLine)
        canvas.drawPoint(pointX, pointY, paintPoint)
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
            if (visibility == View.VISIBLE) {
                startAnimation()
            } else if (visibility == View.GONE || visibility == View.INVISIBLE) {
                stopAnimation()
            }
        }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.VISIBLE) {
            startAnimation()
        } else {
            stopAnimation()
        }
    }

    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect) {
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
        LogUtils.d("stopAnimation")
        if (animatorSet != null) {
            animatorSet?.cancel()
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun startAnimation() {
        LogUtils.d("startAnimation")
        if (animatorSet != null && !animatorSet!!.isStarted && !animatorSet!!.isRunning) {
            animatorSet!!.start()
        }
    }
}