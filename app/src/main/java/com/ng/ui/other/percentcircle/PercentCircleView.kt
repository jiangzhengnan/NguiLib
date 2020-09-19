package com.ng.ui.other.percentcircle

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.ng.nguilib.utils.MLog
import com.ng.ui.R
import kotlin.math.abs
import kotlin.math.min

/**
 * 自定义值动画百分比圆饼饼
 * value: 0 - 100
 * @author Jzn
 * @date 2020/9/19
 */
class PercentCircleView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var mLength = 0f
    private var mCenterX = 0f
    private var mCenterY = 0f
    private var mRectFProgressArc: RectF = RectF()
    private var mValue = 0
    private var mNowSweepAngle = 0f

    //attrs
    private var mTxtSize = 10f
    private var mTxtColor = Color.BLACK
    private var mProBgColor = Color.GRAY
    private var mProStartColor = Color.RED
    private var mProEndColor = Color.BLUE
    private var mProWidth = 20f
    private val DURATION: Long = 600

    //others
    private var mPaint: Paint = Paint()
    private var mCircleAnimator: ValueAnimator? = null

    fun initValue(value: Int) {
        mValue = value
        mNowSweepAngle = value / 100f * 360f
        postInvalidate()
    }

    fun setValue(value: Int) {
        mCircleAnimator = ValueAnimator.ofInt(0, value)
        mCircleAnimator!!.duration = DURATION
        mCircleAnimator!!.interpolator = LinearInterpolator()
        mCircleAnimator!!.addUpdateListener(AnimatorUpdateListener { animation ->
            val temp = animation.animatedValue as Int
            mNowSweepAngle = temp / 100f * 360f
            mValue = temp
            MLog.d(" " + mNowSweepAngle + " " + mValue)
            postInvalidate()
        })
        mCircleAnimator!!.start()
    }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PercentCircleView)
        mTxtSize = ta.getDimension(R.styleable.PercentCircleView_txtSize, 10f)
        mTxtColor = ta.getColor(R.styleable.PercentCircleView_txtColor, Color.BLACK)
        mProBgColor = ta.getColor(R.styleable.PercentCircleView_proBgColor, Color.GRAY)
        mProStartColor = ta.getColor(R.styleable.PercentCircleView_proStartColor, Color.RED)
        mProEndColor = ta.getColor(R.styleable.PercentCircleView_proEndColor, Color.BLUE)
        mProWidth = ta.getDimension(R.styleable.PercentCircleView_proWidth, 10f)

        ta.recycle()
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = mProWidth
        mPaint.flags = Paint.ANTI_ALIAS_FLAG
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //draw bg
        mPaint.style = Paint.Style.STROKE
        mPaint.color = mProBgColor
        canvas.drawCircle(mCenterX, mCenterY, mLength / 2 - mProWidth / 2, mPaint)
        //draw value
        val temp: Float = mProWidth / (3.14159f * mLength * 2f) * 360f + 1f
        mPaint.shader = generateSweepGradient(-90f - temp, mNowSweepAngle, mProStartColor, mProEndColor)
        canvas.drawArc(mRectFProgressArc, -90f, mNowSweepAngle, false, mPaint)
        //draw text
        mPaint.style = Paint.Style.FILL
        mPaint.shader = null
        mPaint.color = mTxtColor
        mPaint.textSize = mTxtSize
        val str = "$mValue%"

        // 文字宽
        val textWidth: Float = mPaint.measureText(str)
        // 文字baseline在y轴方向的位置
        val baseLineY: Float = abs(mPaint.ascent() + mPaint.descent()) / 2
        canvas.drawText(str, mCenterX - textWidth / 2, mCenterY + baseLineY, mPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mLength = min(measuredHeight, measuredWidth).toFloat()
        mCenterX = measuredWidth / 2f
        mCenterY = measuredHeight / 2f
        mRectFProgressArc.set(mProWidth / 2, mProWidth / 2, measuredWidth - mProWidth / 2, measuredWidth - mProWidth / 2)
        setMeasuredDimension(mLength.toInt(), mLength.toInt())
    }

    private fun generateSweepGradient(angle: Float, sweepAngle: Float, startColor: Int, endColor: Int): SweepGradient? {
        val sweepGradient = SweepGradient(mCenterX, mCenterY, intArrayOf(startColor, endColor), floatArrayOf(0f, sweepAngle / 360))
        val matrix = Matrix()
        matrix.setRotate(angle, mCenterX, mCenterY)
        sweepGradient.setLocalMatrix(matrix)
        return sweepGradient
    }

}