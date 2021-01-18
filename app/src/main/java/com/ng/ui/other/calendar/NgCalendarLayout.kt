package com.ng.ui.other.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.ng.nguilib.utils.ViewUtils
import kotlin.math.abs
import kotlin.random.Random

/**
 * 描述:
 * 财经日历:
 * MonthCalendarView
 * 自定义日历框体
 *
 * 星期栏
 * +
 * 日期网格栏
 *
 * @author Jzn
 * @date 2021/1/16
 */
class NgCalendarLayout(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    //实际宽度
    private var mRealWidth = 0

    //涨跌颜色
    private var mRiseColor: Int? = null
    private var mRiseBgColor: Int? = null
    private var mDownColor: Int? = null
    private var mDownBgColor: Int? = null

    //画笔
    private var mPaint: Paint

    //左右padding
    private var mPadding = ViewUtils.dip2px(getContext(), 12f)

    //星期栏
    private val mWeekStrings = arrayOf("M", "T", "W", "T", "F", "S", "S")
    private var mWeekHeightDp = 32f
    private var mWeekGradient: LinearGradient? = null
    private var mWeekBgRect: Rect? = null
    private val mWeekBgStartColor = Color.parseColor("#F8FAFD")
    private val mWeekBgEndColor = Color.WHITE
    private val mWeekTvColor = Color.BLACK
    private val mWeekTvSize = ViewUtils.dip2px(context, 15f).toFloat()

    //星期栏和日历栏的间距
    private val mInsideWeekDay = ViewUtils.dip2px(context, 12f)

    //日历栏
    private val mDayTvColor = Color.BLACK
    private val mInsideDay = ViewUtils.dip2px(context, 1f)
    private val mDayRow = 5
    private val mDayColumns = 7
    private val mDayHeightDp = ViewUtils.dip2px(context, 58f).toFloat()
    private val mDayStrings = arrayListOf<String>()
    private val mDayShowStrings = arrayListOf<String>()
    private val mDayBgs = arrayListOf<Int>()


    //年 月
    private var mYear: Int? = null
    private var mMonth: Int? = null

    init {
        //test data
        mRiseColor = Color.RED
        mDownColor = Color.GREEN
        mRiseBgColor = Color.parseColor("#FDF1F4")
        mDownBgColor = Color.parseColor("#EEFAF7")
        for (i in 0..35) {
            mDayStrings.add(i.toString())
            mDayShowStrings.add("+" + i * 2)
            if (Random.nextBoolean()) {
                mDayBgs.add(mRiseBgColor!!)
            } else {
                mDayBgs.add(mDownBgColor!!)
            }
        }

        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.FILL

    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mRealWidth = measuredWidth - mPadding * 2

        mWeekGradient = LinearGradient(0f, 0f, measuredWidth.toFloat(), 0f, mWeekBgStartColor, mWeekBgEndColor, Shader.TileMode.CLAMP)
        mWeekBgRect = Rect(0, 0, measuredWidth, ViewUtils.dip2px(context, mWeekHeightDp))
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //单位宽度 暂时根据日历算
        var unitWidth = (mRealWidth - mInsideDay * (mDayColumns - 1)) / mDayColumns

        //绘制星期栏
        mPaint.shader = mWeekGradient
        canvas.drawRect(mWeekBgRect!!, mPaint)
        mPaint.color = mWeekTvColor
        mPaint.shader = null
        mPaint.textSize = mWeekTvSize
        var weekStartX: Float = 0f + mPadding + unitWidth / 2
        val baseLineY: Float = abs(mPaint.ascent() + mPaint.descent()) / 2
        val weekStartY: Float = ViewUtils.dip2px(context, mWeekHeightDp / 2).toFloat() + baseLineY
        for (i in mWeekStrings.indices) {
            // 文字宽
            val textWidth: Float = mPaint.measureText(mWeekStrings[i])
            canvas.drawText(mWeekStrings[i], weekStartX - textWidth / 2, weekStartY, mPaint)
            weekStartX += unitWidth
            if (i != mWeekStrings.size - 1) {
                weekStartX += mInsideDay
            }
        }

        //绘制日历栏
        var nowIndex = 0
        var dayStartX: Float = 0f + mPadding + unitWidth / 2
        val dayBaseLineY: Float = abs(mPaint.ascent() + mPaint.descent()) / 2
        var dayStartY: Float = (ViewUtils.dip2px(context, mWeekHeightDp) + mInsideWeekDay).toFloat()

        for (row in 1..mDayRow) {
            for (column in 1..mDayColumns) {
                //先绘制背景
                val dayBgRect = Rect((dayStartX - unitWidth / 2).toInt(),
                        dayStartY.toInt(),
                        (dayStartX + unitWidth / 2).toInt(),
                        (dayStartY + mDayHeightDp).toInt()
                )
                val dayBgGradient = LinearGradient(0f, dayStartY + mDayHeightDp, 0f, dayStartY, mDayBgs[nowIndex], Color.TRANSPARENT, Shader.TileMode.CLAMP)
                mPaint.shader = dayBgGradient
                canvas.drawRect(dayBgRect, mPaint)


                //再绘制上方显示文字
                mPaint.shader = null
                mPaint.color = mDayTvColor
                val textWidth: Float = mPaint.measureText(mDayStrings[nowIndex])
                canvas.drawText(mDayStrings[nowIndex], dayStartX - textWidth / 2, dayStartY + mDayHeightDp / 4 + dayBaseLineY, mPaint)

                //绘制下方显示文字
                val downTextWidth: Float = mPaint.measureText(mDayShowStrings[nowIndex])
                mPaint.color = if (Random.nextBoolean()) mRiseColor!! else mDownColor!!
                canvas.drawText(mDayShowStrings[nowIndex], dayStartX - downTextWidth / 2, dayStartY + mDayHeightDp / 4 * 3 + dayBaseLineY, mPaint)

                dayStartX += unitWidth
                if (column != mDayColumns) {
                    dayStartX += mInsideDay
                }
                nowIndex++
            }

            dayStartX = (mPadding + mRealWidth / mDayColumns / 2).toFloat()
            dayStartY += mDayHeightDp
        }


    }


}