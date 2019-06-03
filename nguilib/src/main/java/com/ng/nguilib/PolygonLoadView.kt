package com.ng.nguilib

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.animation.RotateAnimation

class PolygonLoadView : View {
    private lateinit var paint: Paint
    private lateinit var round: RectF
    private var mGridLinestrokeWidth: Float = 30f
    private lateinit var rotateAnimation: RotateAnimation

    var SHOW_MODEL = 0
    val SHOW_MODEL_ROUND = 0x00
    val SHOW_MODEL_TRIANGLE = 0x01
    val SHOW_MODEL_SQUARE = 0x02


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        SHOW_MODEL = SHOW_MODEL_ROUND
        LogUtils.d("init")
        paint = Paint()
        //开始动画
        startAnim()
    }

    private fun startAnim() {
        rotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.interpolator = OvershootInterpolator(5f)
        rotateAnimation.duration = 1500
        rotateAnimation.repeatCount = ValueAnimator.INFINITE
        this.animation = rotateAnimation
        rotateAnimation.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        when (SHOW_MODEL) {
            SHOW_MODEL_ROUND -> drawRound(canvas!!)
        }


    }

    private fun drawRound(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.color = Color.parseColor("#2D283C")
        paint.strokeWidth = mGridLinestrokeWidth
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        val x = ((width - height / 2) / 2).toFloat()
        val y = (height / 4).toFloat()
        round = RectF(x, y,
                width - x, height - y)
        canvas.drawArc(round,
                315f,
                270f,
                false,
                paint
        )
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


}