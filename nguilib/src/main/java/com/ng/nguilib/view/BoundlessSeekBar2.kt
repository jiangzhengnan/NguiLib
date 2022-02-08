package com.ng.nguilib.view

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import com.ng.nguilib.layout.zoom.thread.ThreadPoolUtil
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
class BoundlessSeekBar2 : View {

    private val TEMP_INDEX = 4

    private val mPaint by lazy {
        Paint()
    }

    private var isUp = false

    private var mInitPrice = 0f // 初始价
    private var mCurrencyId: Int = 0
    private var mNowPrice = 0f  //选中价
    private var mLastPrice = 0f //最新价

    //极限范围
    private val mMinPrice = 0.5f
    private var mMaxPrice = 0f

    //浮动范围
    private var mLeft = 0f
    private var mRight = 0f

    private var mLeftAndRightDis = 0f


    //final params
    private var mCenterLineHeight: Float = ViewUtils.dip2px(context, 4f).toFloat()
    private var mLineColor = Color.parseColor("#12B4FF")
    private var mBarR: Float = ViewUtils.dip2px(context, 13f).toFloat()

    //点击切换球的区域高度
    private var mChangeBarHeight: Float = ViewUtils.dip2px(context, 6f).toFloat()

    //todo WHITE
    private var mBarColor = Color.TRANSPARENT
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
    private var mRuleTemp = 25f
    private var mLeftRightScrollTemp = 0.8f

    private var mScale = 0f
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
    private var mScrollTask: BoundlessSeekBar2.MyScrollTimerTask? = null
    private var mScrollLastDownX = 0f


    class MyScrollTimerTask(var handler: Handler) : TimerTask() {
        override fun run() {
            handler.sendMessage(handler.obtainMessage())
        }
    }

    private fun resetParams() {
        isInside = false
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        //todo
//        mNowTxtColor = ThemeHelper.getAttrColor(context, R.attr.nc401)
//        mOtherTxtColor = ThemeHelper.getAttrColor(context, R.attr.nc302)
        mLineColor = mOtherTxtColor
        mPaint.apply {
            isAntiAlias = true
            flags = Paint.ANTI_ALIAS_FLAG
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.FILL
            //todo
            //typeface = WebullTextUtils.getBoldFontTypeface(context)
        }

        mScrollTimer = Timer()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mDragX = measuredWidth / 2f
        mDragY = measuredHeight / 2f

        resetRange()
    }


    fun setLastPriceInit(price: Float, currencyId: Int, isUp: Boolean) {
        this.isUp = isUp
        mInitPrice = price
        mMaxPrice = mInitPrice * 2
        mRuleLength = mInitPrice / 2f

        //第一次最新价等于初始价
        mLastPrice = mInitPrice

        val lastPriceInitRate = if (isUp) {
            1.1f
        } else {
            0.9f
        }
        mNowPrice = price * lastPriceInitRate

        mRuleTemp = mInitPrice / TEMP_INDEX
        if (mRuleTemp <= 0.01f) {
            mRuleTemp = 0.01f
        }
        mLeftRightScrollTemp = 0.8f * (mInitPrice / 50f)//滚动步长

        mCurrencyId = currencyId
        //设置左右边界
        resetRange()
        postInvalidate()
    }

    private fun resetRange() {
        //缩放比率
        mScale = (measuredWidth / (mRuleLength * 2))
        //左右轴长
        mLeftAndRightDis = mPaddingDistance / mScale
        //设置左右边界
        mLeft = mInitPrice - mRuleLength
        mRight = mInitPrice + mRuleLength
        //设置当前位置
        mDragX = (mNowPrice - mLeft) * mScale
    }

    //设置最新价
    fun setLastPrice(price: Float) {
        mLastPrice = price
        postInvalidate()
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
        //这里判断
        var startLineX = 0f
        var endLineX = measuredWidth.toFloat()
        if (mLeft < mMinPrice) {
            startLineX = abs(mLeft - 0.5f) * mScale
        }
        if (mRight > mMaxPrice) {
            endLineX = measuredWidth - abs(mMaxPrice - mRight) * mScale
        }
        canvas.drawLine(startLineX, mDragY, endLineX, mDragY, mPaint)
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
        //绘制bar的边
        mPaint.apply {
            strokeWidth = ViewUtils.dip2px(context, 1f).toFloat()
            style = Paint.Style.STROKE
            ////todo
            color = Color.BLACK
            //color = ThemeHelper.getAttrColor(context, R.attr.nc103)
        }
        canvas.drawCircle(mDragX, mDragY, mBarR, mPaint)
        //画三根线
        mPaint.style = Paint.Style.FILL
        mPaint.shader = null

        //todo
        //mPaint.color = ThemeHelper.getAlphaColor(0.12f, Color.BLACK)
        val interval = mBarR / 3.5f
        mPaint.strokeWidth = MAX_WIDTH_THREE_LINE
        val mShowY = mDragY
        canvas.drawLine(mDragX - interval, mShowY - interval,
                mDragX - interval, mShowY + interval, mPaint)
        canvas.drawLine(mDragX, mShowY - interval,
                mDragX, mShowY + interval, mPaint)
        canvas.drawLine(mDragX + interval, mShowY - interval,
                mDragX + interval, mShowY + interval, mPaint)
        //绘制可拖动bar头上的文字
        mPaint.apply {
            textSize = mNowTxtSize
            color = mNowTxtColor
        }
        //todo
        //val nowPriceStr = FMNumberUtils.formatNumber(FMNumberUtils.formatNumberTo2Point(mNowPrice), mCurrencyId)
        val nowPriceStr = "$${decimalFormat.format(mNowPrice)}"
        canvas.drawText(nowPriceStr, mDragX - mPaint.measureText(nowPriceStr) / 2,
                measuredHeight / 2 - mBarR - (mPaint.descent() - mPaint.ascent()) - 5, mPaint)


    }


    //绘制点
    private fun drawOtherTxt(canvas: Canvas) {
        //首先绘制
        //绘制last price (如果在范围内)
        mPaint.color = mNowTxtColor
        if (mLastPrice in mLeft..mRight) {
            val mLastPriceX = (mLastPrice - mLeft) * mScale

            val lastPriceY = measuredHeight / 2 + (mPaint.descent() - mPaint.ascent()) + mBarR + 5
            //todo
//            drawOtherTxt(-1, canvas, mLastPriceX, context.getString(R.string.Option_Simple_Trade_1045), lastPriceY, mTextR * 2, true, mNowTxtColor)
//            drawOtherTxt(-1, canvas, mLastPriceX, FMNumberUtils.formatNumber(FMNumberUtils.formatNumberTo2Point(mLastPrice), mCurrencyId), lastPriceY + ViewUtils.dip2px(context, 13f), mTextR * 1.5f, true, mNowTxtColor)
            drawOtherTxt(-1, canvas, mLastPriceX, "最新价", lastPriceY, mTextR * 2, true, mNowTxtColor)
            drawOtherTxt(-1, canvas, mLastPriceX, "$${decimalFormat.format(mLastPrice)}", lastPriceY + ViewUtils.dip2px(context, 13f), mTextR * 1.5f, true, mNowTxtColor)

        }
        mPaint.color = mOtherTxtColor
        var nowAll = mMinPrice
        for (index in 0..TEMP_INDEX * 2) {
            var mTmpX = (nowAll - mLeft) * mScale
            //todo
            //val lastPriceStr = FMNumberUtils.formatNumber(FMNumberUtils.formatNumberTo2Point(nowAll), mCurrencyId)
            val lastPriceStr = "$${decimalFormat.format(nowAll)}"
            drawOtherTxt(index, canvas, mTmpX, lastPriceStr, measuredHeight / 2 + (mPaint.descent() - mPaint.ascent()) +
                    mBarR + 5, mTextR,
                    (abs(nowAll - mLastPrice) >= mRuleTemp / 2), mOtherTxtColor)

            if (index == 0) {
                nowAll += (mRuleTemp - mMinPrice)
            } else {
                nowAll += mRuleTemp
            }
        }

    }

    private fun drawOtherTxt(index: Int, canvas: Canvas, startX: Float, str: String, y: Float, r: Float, hadText: Boolean, textColor: Int) {
        if (!hadText) {
            return
        }
        canvas.drawCircle(startX, mDragY, r, mPaint)
        mPaint.color = textColor
        //if (hadText) {

        var x = startX - mPaint.measureText(str) / 2
        canvas.drawText(str, x, y, mPaint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(motionEvent: MotionEvent?): Boolean {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true)
        }

        if (motionEvent == null)
            return false

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(motionEvent)

        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                //两种情况均需要强行停止滚动
                if (mScrollTask != null) {
                    mScrollTask!!.cancel()
                    mScrollTask = null
                }
                if (isInsideBar(motionEvent.x, motionEvent.y)) {
                    isInside = true
                } else if (
                        (motionEvent.x > mPaddingDistance && motionEvent.x < (measuredWidth - mPaddingDistance))
                        &&
                        (abs(mDragY - motionEvent.y) < mChangeBarHeight)) {
                    //支持点击切换位置
                    isInside = true
                    refreshParams(motionEvent.x)
                } else {
                    mScrollLastDownX = motionEvent.x
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {

                if (isInside) {
                    when {
                        motionEvent.x < mPaddingDistance -> {
                            MLog.d("向左扩展 " + mNowPrice+" " + mDragX)
                            //向左扩展
                            startLeftExtend()
                        }
                        motionEvent.x > (measuredWidth - mPaddingDistance) -> {
                            MLog.d("向右扩展")
                            //向右
                            startRightExtend()
                        }
                        else -> {
                            MLog.d("拖动")
                            stopExtend()
                            if ((motionEvent.x / mScale + mLeft) > 0
                            ) {
                                refreshParams(motionEvent.x)
                            }
                        }
                    }
                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (isInside) {
                    resetParams()
                    stopExtend()
                }
            }
        }
        return super.onTouchEvent(motionEvent)
    }

    private var isMoveToLeft = false

    private fun refreshParams(motionEventX: Float) {

        var tempNowPrice = motionEventX / mScale + mLeft
        if (isUp) {
            //上涨
            //1.当前选中价格比最新价低，此时只能往右划动
            if (tempNowPrice <= mLastPrice && mDragX > motionEventX) {
                mDragX = (mLastPrice - mLeft) * mScale
                mNowPrice = mLastPrice
                postInvalidate()
                return
            }
        } else {
            //下跌
            //1.当前选中价格比最新价高，此时只能往左划动
            if (tempNowPrice >= mLastPrice && mDragX < motionEventX) {
                mDragX = (mLastPrice - mLeft) * mScale
                mNowPrice = mLastPrice
                postInvalidate()


                return
            }
        }


        //边界控制
        if (tempNowPrice < mMinPrice) {
            tempNowPrice = mMinPrice
        } else if (tempNowPrice > mMaxPrice) {
            tempNowPrice = mMaxPrice
        }
        mNowPrice = tempNowPrice
        mDragX = (mNowPrice - mLeft) * mScale




        judgeVibrator()
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
        postInvalidate()
    }

    private fun startLeftExtend() {
        if (isTimerRunning) {
            return
        }
        isTimerRunning = true
        timer = object : Timer() {}
        timerTask = object : TimerTask() {
            override fun run() {
                val temp = -mLeftRightScrollTemp
                startRun(temp)
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
                val temp = mLeftRightScrollTemp
                startRun(temp)
            }
        }
        timer!!.schedule(timerTask, 0, 1000L / 60L)
    }

    private fun startRun(temp: Float) {
        if (!isTimerRunning){
            return
        }
        var tempValue = temp
        val mLeftLimit = mLeft + tempValue + mLeftAndRightDis
        val mRightLimit = mRight + tempValue - mLeftAndRightDis


        if (isUp) {
            //上涨
            when {
                tempValue > 0 && mRightLimit > mMaxPrice -> {
                    mDragX = (mMaxPrice - mLeft) * mScale
                    mNowPrice = mMaxPrice
                    stopExtend()
                    vibrator(context)
                    return
                }
                tempValue < 0 && mLeftLimit < mLastPrice -> {
                    mDragX = (mLastPrice - mLeft) * mScale
                    mNowPrice = mLastPrice
                    stopExtend()
                    vibrator(context)
                    return
                }
            }

        } else {
            //下跌
            when {
                tempValue < 0 && mLeftLimit <= mMinPrice -> {
                    mDragX = mPaddingDistance.toFloat()
                    mNowPrice = mMinPrice
                    stopExtend()
                    vibrator(context)

                    return
                }
                tempValue > 0 && mRightLimit >= mLastPrice -> {
                    mDragX = (mLastPrice - mLeft) * mScale
                    mNowPrice = mLastPrice
                    stopExtend()
                    vibrator(context)
                    MLog.d("stop 222")

                    return
                }
            }
        }
        mLeft += tempValue
        mRight += tempValue

        mNowPrice = mDragX / mScale + mLeft


        judgeVibrator()
        invalidate()

    }

    private fun judgeVibrator() {
        ThreadPoolUtil.runOnUiThread({
            try {
                if (abs(mNowPrice - mLastPrice) <= mRuleLength / 50) {
                    vibrator(context)
                } else if (abs(mRuleTemp - abs(mNowPrice % mRuleTemp)) <= mRuleLength / 50 && abs(mNowPrice - mLastPrice) >= mRuleTemp / 2) {
                    vibrator(context)
                }
            } catch (e: Exception) {
            }
        }, 0)

    }


    private fun isInsideBar(x: Float, y: Float): Boolean {
        return abs(mDragX - x) < mBarR && abs(mDragY - y) < mBarR
    }

    private fun vibrator(context: Context) {
        val vib = context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        if (vib != null && vib.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createOneShot(1, 255))
            } else {
                vib.vibrate(20)
            }
        }
    }

    fun getNowPrice(): String {
        return mNowPrice.toString()
    }
}