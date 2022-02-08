package com.ng.nguilib.view

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.AttributeSet
import android.view.MotionEvent
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
class BoundlessSeekBar3 : View {

    private val TEMP_INDEX = 4

    private val MIN_VALUE = 0.5f

    private val mPaint by lazy {
        Paint()
    }

    private var mInitPrice = 0f
    private var mCurrencyId: Int = 0
    private var mNowPrice = 0f
    private var mLastPrice = 0f
    private var mMaxPrice = 0f      //最大价格


    //final params
    private var mCenterLineHeight: Float = ViewUtils.dip2px(context, 4f).toFloat()
    private var mLineColor = Color.parseColor("#12B4FF")
    private var mBarR: Float = ViewUtils.dip2px(context, 13f).toFloat()

    //点击切换球的区域高度
    private var mChangeBarHeight: Float = ViewUtils.dip2px(context, 6f).toFloat()
    private var mBarColor = Color.WHITE

    private var mPaddingDistance: Int = 0
    private var mTouchDistance: Int = 0


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

    private var mLeft = 0f
    private var mRight = 0f

    //三根横线
    private var MAX_WIDTH_THREE_LINE = 3f

    //滑动
    private var mMoveLen = 0f
    private var mScrollLastDownX = 0f


    private fun resetParams() {
        isInside = false
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
//    todo     mNowTxtColor = ThemeHelper.getAttrColor(context, R.attr.nc401)
//        mOtherTxtColor = ThemeHelper.getAttrColor(context, R.attr.nc302)

        mTouchDistance = (mBarR / 2).toInt()
        mPaint.apply {
            isAntiAlias = true
            flags = Paint.ANTI_ALIAS_FLAG
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.FILL
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mDragX = measuredWidth / 2f
        mDragY = measuredHeight / 2f

        resetRange()
    }


    //初始化
    fun setLastPriceInit(price: Float, currencyId: Int) {
        mLastPrice = price
        mInitPrice = price
        mNowPrice = price
        mRuleLength = (price) / 2f
        mRuleTemp = mLastPrice / TEMP_INDEX
        if (mRuleTemp <= 0.01f) {
            mRuleTemp = 0.01f
        }
        mLeftRightScrollTemp = 0.8f * (mLastPrice / 50f)
        mMaxPrice = price * 2 + MIN_VALUE * 2

        mCurrencyId = currencyId
        //设置左右边界
        resetRange()
        postInvalidate()

    }

    private fun resetRange() {
        mScale = (measuredWidth.toFloat() / (mRuleLength * 2))
        //设置左右边界
        mLeft = mInitPrice - mRuleLength
        mRight = mInitPrice + mRuleLength
        MLog.d("左右边界: " + mLeft + "  " + mRight + "  ")
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
        //绘制bar的边
        mPaint.apply {
            strokeWidth = ViewUtils.dip2px(context, 1f).toFloat()
            style = Paint.Style.STROKE

            //todo delete
            color = Color.BLACK
            //todo color = ThemeHelper.getAttrColor(context,R.attr.nc103)
        }
        canvas.drawCircle(mDragX, mDragY, mBarR, mPaint)

        //画三根线
        mPaint.style = Paint.Style.FILL
        mPaint.shader = null
        //todo mPaint.color = ThemeHelper.getAttrColor(context, R.attr.nc302)
        val interval = mBarR / 3f
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

        //val nowPriceStr = "$${decimalFormat.format(mNowPrice)}"
        val nowPriceStr =
                //todo FMNumberUtils.formatNumber(FMNumberUtils.formatNumberTo2Point(mNowPrice), mCurrencyId)
                "" + mNowPrice + "$"
        canvas.drawText(nowPriceStr, mDragX - mPaint.measureText(nowPriceStr) / 2,
                measuredHeight / 2 - mBarR - (mPaint.descent() - mPaint.ascent()) - 5, mPaint)

    }


    //设置最新价
    fun setLastPrice(price: Float) {
        mLastPrice = price
        postInvalidate()
    }

    //绘制点
    private fun drawOtherTxt(canvas: Canvas) {
        //首先绘制
        //绘制last price (如果在范围内)
        if (mLastPrice in mLeft..mRight) {
            val mLastPriceX = (mLastPrice - mLeft) * mScale

//                todo    context.getString(R.string.Option_Simple_Trade_1045) + ":" +
//                    FMNumberUtils.formatNumber(FMNumberUtils.formatNumberTo2Point(mLastPrice), mCurrencyId)
            val lastPriceY = measuredHeight / 2 + (mPaint.descent() - mPaint.ascent()) + mBarR + 5
            drawOtherTxt(-1, canvas, mLastPriceX, "LastPrice:", lastPriceY, mTextR * 2, true, mNowTxtColor)
            drawOtherTxt(-1, canvas, mLastPriceX, "" + mLastPrice + "$", lastPriceY + ViewUtils.dip2px(context, 13f), mTextR * 2, true, mNowTxtColor)

        }

        var nowAll = MIN_VALUE
        for (index in 0..TEMP_INDEX * 2) {
            var mTmpX = (nowAll - mLeft) * mScale

            MLog.d("画: " + nowAll + "   " + mLeft + " " + mTmpX + "   " + measuredWidth)

            val lastPriceStr =
                    "" + nowAll + "$"
            //todo val lastPriceStr = FMNumberUtils.formatNumber(FMNumberUtils.formatNumberTo2Point(nowAll), mCurrencyId)
            drawOtherTxt(index, canvas, mTmpX, lastPriceStr, measuredHeight / 2 + (mPaint.descent() - mPaint.ascent()) + mBarR + 5, mTextR,
                    (abs(nowAll - mLastPrice) >= mRuleTemp / 2), mOtherTxtColor)
            nowAll += mRuleTemp
        }

    }


    private fun drawOtherTxt(index: Int, canvas: Canvas, startX: Float, str: String, y: Float, r: Float, hadText: Boolean, textColor: Int) {
        if (!hadText) {
            return
        }
        mPaint.color = if (startX < mDragX) {
            mNowTxtColor
        } else {
            mOtherTxtColor
        }
        canvas.drawCircle(startX, mDragY, r, mPaint)
        mPaint.color = textColor
        //if (hadText) {

        var x = startX - mPaint.measureText(str) / 2
        if (index == 0) {
            x = startX
        } else if (index == TEMP_INDEX * 2) {
            x = startX - mPaint.measureText(str)
        }
        canvas.drawText(str, x, y, mPaint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(motionEvent: MotionEvent?): Boolean {
        if (motionEvent == null)
            return false

        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                //两种情况均需要强行停止滚动
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
                        motionEvent.x < mTouchDistance -> {
                            //向左扩展
                            startLeftExtend()
                        }
                        motionEvent.x > (measuredWidth - mTouchDistance) -> {
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


                    startRun(temp / mScale)

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

    private fun refreshParams() {
        mNowPrice = mDragX / mScale + mLeft

        ThreadPoolUtil.runOnUiThread({
            try {

                if (abs(mNowPrice - mLastPrice) <= mRuleLength / 50) {
                    vibrator(context)
                } else if (abs(mRuleTemp - abs(mNowPrice % mRuleTemp)) <= mRuleLength / 50 && abs(mNowPrice - mLastPrice) >= mRuleTemp * 2 / 3) {
                    vibrator(context)
                }

                invalidate()
            } catch (e: Exception) {
            }
        }, 0)


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

                startRun(-mLeftRightScrollTemp)
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

                startRun(mLeftRightScrollTemp)
            }
        }
        timer!!.schedule(timerTask, 0, 1000L / 60L)
    }

    private fun startRun(temp: Float) {
        //  mScale = (measuredWidth / (mRuleLength * 2))
        if (mLeft + temp >= MIN_VALUE
                && mRight + temp <= mMaxPrice - MIN_VALUE
        ) {
            mLeft += temp
            mRight += temp

            refreshParams()
        }
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