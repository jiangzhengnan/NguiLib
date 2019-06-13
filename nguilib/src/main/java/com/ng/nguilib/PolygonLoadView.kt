package com.ng.nguilib

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.animation.RotateAnimation

class PolygonLoadView : View {
    //common
    private var paintLine: Paint? = null
    private var paintPoint: Paint? = null

    private var roundRF: RectF? = null
    private val mGridLinestrokeWidth = 30f

    private var SHOW_MODEL = 0
    val SHOW_MODEL_ROUND = 0x00
    val SHOW_MODEL_TRIANGLE = 0x01
    val SHOW_MODEL_SQUARE = 0x02

    val TIME_CIRCLE: Long = 2200

    private var animatorSet: AnimatorSet? = null
    private var mSideLenght: Float = 0.toFloat()
    private var mHalfSH: Float = 0.toFloat()
    private var thickness: Float = 0.toFloat()

    //round
    private var pointX: Float = 0.toFloat()
    private var pointY: Float = 0.toFloat()
    private var startAngle: Float = 0.toFloat()
    private val swipeAngle = 270f
    //triangle

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

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
        mHalfSH = mSideLenght / 2
        thickness = mGridLinestrokeWidth / 2
        when (SHOW_MODEL) {
            SHOW_MODEL_ROUND -> initRound()
            SHOW_MODEL_TRIANGLE -> initTriangle()
            SHOW_MODEL_SQUARE -> initSquare()
        }

    }

    private fun initSquare() {
        startAnimSquare()
    }

    private fun startAnimSquare() {

    }

    private fun initTriangle() {
        //paint
        paintLine!!.style = Paint.Style.STROKE
        paintLine!!.color = Color.parseColor("#2D283C")
        paintLine!!.strokeWidth = mGridLinestrokeWidth
        paintLine!!.isAntiAlias = true
        paintLine!!.strokeCap = Paint.Cap.ROUND
        roundRF = RectF(0 + mGridLinestrokeWidth / 2,
                0 + mGridLinestrokeWidth / 2,
                mSideLenght - mGridLinestrokeWidth / 2,
                mSideLenght - mGridLinestrokeWidth / 2)
        paintPoint!!.isAntiAlias = true
        paintPoint!!.color = Color.parseColor("#4A22EA")
        paintPoint!!.style = Paint.Style.STROKE
        paintPoint!!.strokeWidth = mGridLinestrokeWidth
        paintPoint!!.strokeCap = Paint.Cap.ROUND


        //point
        pointX = mHalfSH / 2 + thickness
        pointY = mHalfSH
        // startAnimTriangle
        /**
         * x ->  mHalfSH/2 - mHalfSH - mHalfSH*3/2 - mHalfSH/2
         * y ->  width/2   -   0  -  width/2 - width/2
         * startAngle ->        315f 225f 135f 45f -45f
         */
        startAnimByStep(3, object : OnAnimationUpdatePLView {
            override fun onUpdate(step: Int, fraction: Float) {
                when (step) {
                    1 -> {
                        pointX = mHalfSH / 2 + thickness + fraction * (mHalfSH / 2 - thickness)
                        pointY = mHalfSH + fraction * (mHalfSH - thickness)
                    }
                    2 -> {
                        pointX = mHalfSH + fraction * (mHalfSH / 2)
                        pointY = mHalfSH + fraction * (mHalfSH - thickness)
                    }
                    3 -> {
                        pointX = mHalfSH * 3 / 2 - thickness - fraction * (mHalfSH - 2 * thickness)
                        pointY = mSideLenght - thickness - fraction * (mHalfSH - thickness)
                    }
                }
            }
        })
    }


    private fun initRound() {
        //paint
        paintLine!!.style = Paint.Style.STROKE
        paintLine!!.color = Color.parseColor("#2D283C")
        paintLine!!.strokeWidth = mGridLinestrokeWidth
        paintLine!!.isAntiAlias = true
        paintLine!!.strokeCap = Paint.Cap.ROUND
        roundRF = RectF(0 + mGridLinestrokeWidth / 2,
                0 + mGridLinestrokeWidth / 2,
                mSideLenght - mGridLinestrokeWidth / 2,
                mSideLenght - mGridLinestrokeWidth / 2)
        paintPoint!!.isAntiAlias = true
        paintPoint!!.color = Color.parseColor("#4A22EA")
        paintPoint!!.style = Paint.Style.STROKE
        paintPoint!!.strokeWidth = mGridLinestrokeWidth
        paintPoint!!.strokeCap = Paint.Cap.ROUND

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
                        pointY = mSideLenght - thickness - fraction * (mHalfSH - thickness)
                        startAngle = 135f - fraction * 90
                    }
                    2 -> {
                        pointX = mSideLenght - fraction * (mHalfSH - thickness) - thickness
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
            pointAnimatorTemp.duration = TIME_CIRCLE / 4
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
        mSideLenght = (if (measuredWidth > measuredHeight) measuredHeight else measuredWidth).toFloat()
        //宽必须等于高
        LogUtils.d("宽： $mSideLenght  高：  $mSideLenght")
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
        canvas.drawPoint(pointX, pointY, paintPoint!!)
    }

    private fun drawTriangle(canvas: Canvas) {
        canvas.drawPoint(pointX, pointY, paintPoint!!)

    }

    private fun drawRound(canvas: Canvas) {
        canvas.drawArc(roundRF!!, startAngle, swipeAngle, false, paintLine!!)
        canvas.drawPoint(pointX, pointY, paintPoint!!)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    interface OnAnimationUpdatePLView {
        fun onUpdate(step: Int, fraction: Float)
    }

}