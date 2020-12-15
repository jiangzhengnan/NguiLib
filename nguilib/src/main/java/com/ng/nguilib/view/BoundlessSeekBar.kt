package com.ng.nguilib.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Vibrator
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.ng.nguilib.utils.MLog
import com.ng.nguilib.utils.ViewUtils
import java.text.DecimalFormat
import java.util.*
import kotlin.math.abs


/**
 * 描述:无边界拖动的progressbar
 * @author Jzn
 * @date 2020/12/11
 */
class BoundlessSeekBar : View {


    private val mPaint by lazy {
        Paint()
    }

    private var mInitPrice = 0f
    private var mCurrencyId: Int = 0
    private var mNowPrice = 0f
    private var mLastPrice = 0f
    private var mMaxPrice = 0f

    //final params
    private var mCenterLineHeight: Float = ViewUtils.dip2px(context, 4f).toFloat()
    private var mLineColor = Color.parseColor("#12B4FF")
    private var mBarR: Float = ViewUtils.dip2px(context, 13f).toFloat()

    //点击切换球的区域高度
    private var mChangeBarHeight: Float = ViewUtils.dip2px(context, 6f).toFloat()
    private var mBarColor = Color.GRAY
    private var mPaddingDistance: Int = ViewUtils.dip2px(context, 50f)

    private var mNowTxtColor = Color.parseColor("#12B4FF")
    private var mNowTxtSize: Float = ViewUtils.dip2px(context, 16f).toFloat()
    private var mOtherTxtColor = Color.parseColor("#888B94")
    private var mOtherTxtSize: Float = ViewUtils.dip2px(context, 11f).toFloat()

    private var mTextR: Float = ViewUtils.dip2px(context, 3f).toFloat()

    //拖动 params
    private var mDragX = 0f
    private var mDragY = 0f
    private var isInside = false

    //刻度尺
    //约定初始 屏幕宽度 = 100
    //private var mScale: Int = ViewUtils.dip2px(context, 100f)
    private var mRuleLength = 50f
    private var mScale = 0f
    private var mLeft = 0f
    private var mRight = 0f
    private var decimalFormat = DecimalFormat("0.00")
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private var isTimerRunning = false

    //三根横线
    private var MAX_WIDTH_THREE_LINE = 3f

    //滑动
    private var mVelocityTracker: VelocityTracker? = null//滑动速度追踪
    private val mMaximumVelocity = Float.MAX_VALUE
    private val mMinimumVelocity = 0
    private val STOP_SPEED = 2f
    private var mMoveLen = 0f
    private var mScrollTimer: Timer? = null
    private var mScrollLastDownX = 0f

    private var mAnimLastValue = 0f

    //旋转角度
    private var mRotate = 0f

    private fun resetParams() {
        isInside = false
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private var mInertiaAnimator: ValueAnimator? = null

    init {

        mPaint.apply {
            isAntiAlias = true
            flags = Paint.ANTI_ALIAS_FLAG
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.FILL
        }

        mScrollTimer = Timer()

        //mScrollTask = MyScrollTimerTask(updateHandler)


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mDragX = measuredWidth / 2f
        mDragY = measuredHeight / 2f

        mScale = (measuredWidth / (mRuleLength * 2))




        resetRange()
    }


    fun setLastPriceInit(price: Float, currencyId: Int) {
        mLastPrice = price
        mInitPrice = mLastPrice
        mMaxPrice = Float.MAX_VALUE
        mNowPrice = mInitPrice
        mCurrencyId = currencyId

        //设置左右边界
        resetRange()

    }

    private fun resetRange() {
        if (mMaxPrice < 100f) {
            val distance = mPaddingDistance.toFloat() / mScale

            mDragX = (mNowPrice - mLeft) * mScale
            //        //mNowPrice = mDragX / mScale + mLeft

            mLeft = -distance
            mRight = mLeft + 2 * mRuleLength

        } else {
            //设置左右边界
            mLeft = mInitPrice - mRuleLength
            mRight = mInitPrice + mRuleLength
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null)
            return

        //先绘制横线
        mPaint.apply {
            strokeWidth = mCenterLineHeight
            color = mLineColor
        }
        canvas.drawLine(0f, mDragY, mDragX, mDragY, mPaint)
        mPaint.color = mOtherTxtColor
        canvas.drawLine(mDragX, mDragY, measuredWidth.toFloat(), mDragY, mPaint)

        //绘制last price 与 刻度尺
        mPaint.apply {
            textSize = mOtherTxtSize
            color = mOtherTxtColor
        }
        drawOtherTxt(canvas)

        //绘制中间可拖动bar
        mPaint.apply {
            strokeWidth = mBarR
            color = mBarColor
        }
        canvas.drawCircle(mDragX, mDragY, mBarR, mPaint)

        //绘制可拖动bar头上的文字
        mPaint.apply {
            textSize = mNowTxtSize
            color = mNowTxtColor
        }
        //float num=(float)Math.round(totalPrice*100)/100;
        //mNowPrice = mDragX / mScale + mLeft
        val nowPriceStr = "$${decimalFormat.format(mNowPrice)}"
        canvas.drawText(nowPriceStr, mDragX - mPaint.measureText(nowPriceStr) / 2,
                measuredHeight / 2 - mBarR - (mPaint.descent() - mPaint.ascent()) - 5, mPaint)


        //画三根线
        mPaint.shader = null
        mPaint.color = Color.WHITE
        val interval = mBarR / 3f
        mPaint.strokeWidth = MAX_WIDTH_THREE_LINE
        val mShowY = mDragY
        canvas.translate(mDragX, mShowY)
        canvas.rotate(mRotate)
        canvas.drawLine(-interval, -interval,
                -interval, interval, mPaint)
        canvas.drawLine(0f, -interval,
                0f, interval, mPaint)
        canvas.drawLine(interval, -interval,
                interval, interval, mPaint)

//        canvas.drawLine(mDragX - interval, mShowY - interval,
//                mDragX - interval, mShowY + interval, mPaint)
//        canvas.drawLine(mDragX, mShowY - interval,
//                mDragX, mShowY + interval, mPaint)
//        canvas.drawLine(mDragX + interval, mShowY - interval,
//                mDragX + interval, mShowY + interval, mPaint)


    }

    //设置最新价
    fun setLastPrice(price: Float) {
        mLastPrice = price;
        postInvalidate()
    }

    private fun drawOtherTxt(canvas: Canvas) {
        //首先绘制
        //绘制last price (如果在范围内)
        if (mLastPrice in mLeft..mRight) {
            val mLastPriceX = (mLastPrice - mLeft) * mScale
            val lastPriceStr = "Last price:" + mLastPrice
            drawOtherTxt(canvas, mLastPriceX, lastPriceStr, measuredHeight / 2 + (mPaint.descent() - mPaint.ascent()) + mBarR + 5)
        }

        for (index in mLeft.toInt()..mRight.toInt()) {
            if (index >= 0 && index % 25 == 0) {
                val mTmpX = (index - mLeft) * mScale
                val lastPriceStr = index.toString()
                drawOtherTxt(canvas, mTmpX, lastPriceStr, measuredHeight / 2 + (mPaint.descent() - mPaint.ascent()) * 2 + mBarR * 2)
            }
        }
    }

    private fun drawOtherTxt(canvas: Canvas, startX: Float, str: String, y: Float) {
        mPaint.color = if (startX < mDragX) {
            mNowTxtColor
        } else {
            mOtherTxtColor
        }
        canvas.drawCircle(startX, mDragY, mTextR, mPaint)
        mPaint.color = mOtherTxtColor
        canvas.drawText(str, startX - mPaint.measureText(str) / 2, y, mPaint)

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(motionEvent: MotionEvent?): Boolean {
        if (motionEvent == null)
            return false

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(motionEvent)

        parent.requestDisallowInterceptTouchEvent(true)


        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                mAnimLastValue = 0f
                mMoveLen = 0f

                //两种情况均需要强行停止滚动
                if (timer != null) {
                    timer!!.cancel()
                    timer = null
                }

                if (isInsideBar(motionEvent.x, motionEvent.y)) {
                    isInside = true
                } else if (
                        (motionEvent.x > mPaddingDistance && motionEvent.x < (measuredWidth - mPaddingDistance))
                        &&
                        (abs(mDragY - motionEvent.y) < mChangeBarHeight)) {
                    //支持点击切换位置
                    mDragX = motionEvent.x
                    isInside = true
                    refreshParams()
                    postInvalidate()
                } else {
                    mScrollLastDownX = motionEvent.x
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if (isInside) {
                    when {
                        motionEvent.x < mPaddingDistance -> {
                            //向左扩展
                            startLeftExtend()
                        }
                        motionEvent.x > (measuredWidth - mPaddingDistance) -> {
                            //向右
                            startRightExtend()
                        }
                        else -> {
                            stopExtend()
                            if ((motionEvent.x / mScale + mLeft) > 0
                            ) {
                                mDragX = motionEvent.x
                                refreshParams()
                            }

                        }
                    }
                } else {
                    //滑动
                    mMoveLen += (motionEvent.x - mScrollLastDownX)

                    val temp = -(motionEvent.x - mScrollLastDownX)

                    mScrollLastDownX = motionEvent.x


                    MLog.d("滑动: $temp")

                    startRun(temp / 10)

                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (isInside) {
                    resetParams()
                    stopExtend()
                } else {

                    // 计算当前速度， 1000表示每秒像素数等
                    mVelocityTracker!!.computeCurrentVelocity(1000, mMaximumVelocity)


                    // 获取横向速度
                    val velocityX = mVelocityTracker!!.xVelocity.toInt()

                    if (abs(velocityX) < 1000) {
                        resetParams()
                        mMoveLen = 0f
                    } else {
                        if (mVelocityTracker != null) {
                            mVelocityTracker!!.recycle()
                            mVelocityTracker = null
                            mAnimLastValue = 0f
                        }


                        // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
                        if (abs(mMoveLen) < 0.0001) {
                            mMoveLen = 0f
                            return true
                        }

                        mMoveLen /= 2

                        val duration = abs(velocityX) / 3.toLong()
                        val circleNum = abs(velocityX) / 1000 / 2 + 1

                        var needFinish = true
                        mInertiaAnimator = ValueAnimator.ofFloat(0f, mMoveLen)
                        mInertiaAnimator!!.duration = duration
                        mInertiaAnimator!!.interpolator = DecelerateInterpolator(2f)
                        mInertiaAnimator!!.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
                            //旋转
                            val tempPercent = animation.animatedFraction
                            mRotate = if (mMoveLen > 0) {
                                -180 * circleNum * tempPercent
                            } else {
                                180 * circleNum * tempPercent
                            }

                            if (needFinish) {
                                val temp = (animation.animatedValue as Float) - mAnimLastValue
                                mAnimLastValue = animation.animatedValue as Float

                                mMoveLen -= temp
                                needFinish = startRun(-temp)
                            } else {
                                postInvalidate()
                            }

                        })
                        mInertiaAnimator!!.start()
                    }
                }
            }
        }
        return super.onTouchEvent(motionEvent)
    }

    private fun refreshParams() {
        mNowPrice = mDragX / mScale + mLeft
        if (abs(mNowPrice.toInt() % 25) <= 1 || abs(mNowPrice.toInt() - mLastPrice.toInt()) <= 1) {
            vibrator(context)
        }
        invalidate()
    }

    private fun stopExtend() {
        if (!isTimerRunning) {
            return
        }
        if (timerTask != null)
            timerTask!!.cancel()
        if (timer != null) {
            timer!!.cancel()
            timer!!.purge()
            timer = null
        }
        isTimerRunning = false
    }

    private fun startLeftExtend() {
        if (isTimerRunning) {
            return
        }
        isTimerRunning = true
        timer = object : Timer() {}
        timerTask = object : TimerTask() {
            override fun run() {

                startRun(-0.8f)
            }
        }
        timer!!.schedule(timerTask, 0, 1000L / 60L)
    }

    private fun startRightExtend() {
        if (isTimerRunning) {
            return
        }
        isTimerRunning = true
        timer = object : Timer(isTimerRunning) {}
        timerTask = object : TimerTask() {
            override fun run() {

                startRun(0.8f)
            }
        }
        timer!!.schedule(timerTask, 0, 1000L / 60L)
    }

    private fun startRun(temp: Float): Boolean {
        //  mScale = (measuredWidth / (mRuleLength * 2))
        val distance = mPaddingDistance / mScale
        return if (mLeft + temp > -distance
                && mRight + temp < mMaxPrice + distance
        ) {
            mLeft += temp
            mRight += temp
            refreshParams()
            true
        } else {
            false
        }
    }


    private fun isInsideBar(x: Float, y: Float): Boolean {
        return abs(mDragX - x) < mBarR && abs(mDragY - y) < mBarR
    }

    private fun vibrator(context: Context) {
        try {
            val vib = context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
            if (vib.hasVibrator()) {
                vib.vibrate(30)
            }
        } catch (e: Exception) {
        }
    }

    fun getNowPrice(): String {
        return mNowPrice.toString()
    }

    class MyScrollTimerTask(var handler: Handler) : TimerTask() {
        override fun run() {
            handler.sendMessage(handler.obtainMessage())
        }
    }

}