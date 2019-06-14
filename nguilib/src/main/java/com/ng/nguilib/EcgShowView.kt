package com.ng.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.ng.nguilib.LogUtils
import com.ng.nguilib.utils.DensityUtil.dip2px
import java.util.*

class EcgShowView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var SHOW_MODEL = 0
    val SHOW_MODEL_ALL = 0x00
    val SHOW_MODEL_DYNAMIC_SCROLL = 0x01
    val SHOW_MODEL_DYNAMIC_REFRESH = 0x02

    private var mWidth: Float = 0.toFloat()
    private var mHeight: Float = 0.toFloat()
    private var paint: Paint? = null
    private var path: Path? = null
    private var dataStrList: Array<String>? = null

    private var scrollIndex = 0
    private lateinit var timer: Timer
    private lateinit var timerTask: TimerTask
    private val INTERVAL_SCROLL_REFRESH = 80f

    private var refreshList: MutableList<Float>? = null
    private var showIndex: Int = 0

    private val MAX_VALUE = 20f
    private var intervalNumHeart: Int = 0
    private var intervalRowHeart: Float = 0.toFloat()
    private var intervalColumnHeart: Float = 0.toFloat()
    private val HEART_LINE_STROKE_WIDTH = 2.5f
    private var data: FloatArray? = null
    private var mHeartLinestrokeWidth: Float = 0.toFloat()

    private val GRID_LINE_STROKE_WIDTH = 1.5f
    private val GRID_WIDTH_AND_HEIGHT = 5f
    private var row: Int = 0
    private var intervalRow: Float = 0.toFloat()
    private var column: Int = 0
    private var intervalColumn: Float = 0.toFloat()
    private var mGridLinestrokeWidth: Float = 0.toFloat()
    private var mGridstrokeWidthAndHeight: Float = 0.toFloat()


    init {
        init()
    }

    private fun init() {
        paint = Paint()
        path = Path()
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mWidth = measuredWidth.toFloat()
        mHeight = measuredHeight.toFloat()
        mGridLinestrokeWidth = dip2px(context, GRID_LINE_STROKE_WIDTH).toFloat()
        mGridstrokeWidthAndHeight = dip2px(context, GRID_WIDTH_AND_HEIGHT).toFloat()

        column = (mWidth / mGridstrokeWidthAndHeight).toInt()
        intervalColumn = mWidth / column
        row = (mHeight / mGridstrokeWidthAndHeight).toInt()
        intervalRow = mHeight / row

        mHeartLinestrokeWidth = dip2px(context, HEART_LINE_STROKE_WIDTH).toFloat()
        initData()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawGrid(canvas)

        when (SHOW_MODEL) {
            SHOW_MODEL_ALL -> drawHeartAll(canvas)
            SHOW_MODEL_DYNAMIC_SCROLL -> drawHeartScroll(canvas)
            SHOW_MODEL_DYNAMIC_REFRESH -> drawHeartRefresh(canvas)
        }

    }

    fun showLine(point: Float) {
        if (refreshList == null) {
            refreshList = ArrayList()
            data = FloatArray(intervalNumHeart)
        }
        refreshList!!.add(point)

        postInvalidate()
    }


    private fun drawHeartRefresh(canvas: Canvas) {
        paint!!.reset()
        path!!.reset()
        paint!!.style = Paint.Style.STROKE
        paint!!.color = Color.parseColor("#31CE32")
        paint!!.strokeWidth = mGridLinestrokeWidth
        paint!!.isAntiAlias = true
        path!!.moveTo(0f, mHeight / 2)

        val nowIndex = if (refreshList == null) 0 else refreshList!!.size

        if (nowIndex == 0) {
            return
        }

        showIndex = if (nowIndex < intervalNumHeart) {
            nowIndex - 1
        } else {
            (nowIndex - 1) % intervalNumHeart
        }


        for (i in 0 until intervalNumHeart) {
            if (i > refreshList!!.size - 1) {
                break
            }
            if (nowIndex <= intervalNumHeart) {
                this.data!![i] = refreshList!![i]
            } else {
                val times = (nowIndex - 1) / intervalNumHeart

                val temp = times * intervalNumHeart + i

                if (temp < nowIndex) {
                    this.data!![i] = refreshList!![temp]
                }
            }
        }

        logdata()

        var nowX: Float
        var nowY: Float
        for (i in data!!.indices) {
            nowX = i * intervalRowHeart
            var dataValue = data!![i]
            if (dataValue > 0) {
                if (dataValue > MAX_VALUE * 0.8f) {
                    dataValue = MAX_VALUE * 0.8f
                }
            } else {
                if (dataValue < -MAX_VALUE * 0.8f) {
                    dataValue = -(MAX_VALUE * 0.8f)
                }
            }
            nowY = mHeight / 2 - dataValue * intervalColumnHeart

            if (i - 1 == showIndex) {
                path!!.moveTo(nowX, nowY)

            } else {
                path!!.lineTo(nowX, nowY)
            }

        }

        canvas.drawPath(path!!, paint!!)
    }

    private fun logdata() {
        var str = ""
        for (temp in data!!) {
            str += "$temp,"

        }

    }

    private fun drawHeartScroll(canvas: Canvas) {
        if (data == null || data!!.isEmpty()) {
            return
        }
        paint!!.reset()
        path!!.reset()
        paint!!.style = Paint.Style.STROKE
        paint!!.color = Color.parseColor("#31CE32")
        paint!!.strokeWidth = mGridLinestrokeWidth
        paint!!.isAntiAlias = true
        path!!.moveTo(0f, mHeight / 2)

        val scrollEndIndex = scrollIndex
        var scrollStartIndex = scrollEndIndex - intervalNumHeart
        if (scrollStartIndex < 0) {
            scrollStartIndex = 0
        }

        var nowX: Float
        var nowY: Float
        for (i in scrollStartIndex until scrollEndIndex) {
            nowX = (i - scrollStartIndex) * intervalRowHeart

            var dataValue = data!![i]
            if (dataValue > 0) {
                if (dataValue > MAX_VALUE * 0.8f) {
                    dataValue = MAX_VALUE * 0.8f
                }
            } else {
                if (dataValue < -MAX_VALUE * 0.8f) {
                    dataValue = -(MAX_VALUE * 0.8f)
                }
            }
            nowY = mHeight / 2 - dataValue * intervalColumnHeart
            path!!.lineTo(nowX, nowY)
        }

        canvas.drawPath(path!!, paint!!)
        postInvalidate()

    }

    private fun drawHeartAll(canvas: Canvas) {
        if (data == null || data!!.isEmpty()) {
            return
        }
        paint!!.reset()
        path!!.reset()
        paint!!.style = Paint.Style.STROKE
        paint!!.color = Color.parseColor("#31CE32")
        paint!!.strokeWidth = mGridLinestrokeWidth
        paint!!.isAntiAlias = true
        path!!.moveTo(0f, mHeight / 2)
        var nowX: Float
        var nowY: Float
        for (i in data!!.indices) {
            nowX = i * intervalRowHeart
            var dataValue = data!![i]
            if (dataValue > 0) {
                if (dataValue > MAX_VALUE * 0.8f) {
                    dataValue = MAX_VALUE * 0.8f
                }
            } else {
                if (dataValue < -MAX_VALUE * 0.8f) {
                    dataValue = -(MAX_VALUE * 0.8f)
                }
            }
            nowY = mHeight / 2 - dataValue * intervalColumnHeart
            path!!.lineTo(nowX, nowY)
        }

        canvas.drawPath(path!!, paint!!)

    }

    private fun drawGrid(canvas: Canvas) {
        paint!!.style = Paint.Style.STROKE
        paint!!.color = Color.parseColor("#D8D8D8")
        paint!!.strokeWidth = mGridLinestrokeWidth
        paint!!.isAntiAlias = true
        for (i in 0..column) {
            val iTempC = i * intervalColumn
            path!!.moveTo(iTempC, 0f)
            path!!.lineTo(iTempC, mHeight)
        }
        for (i in 0..row) {
            path!!.moveTo(0f, i * intervalRow)
            path!!.lineTo(mWidth, i * intervalRow)
        }
        canvas.drawPath(path!!, paint!!)
    }

    fun setData(dataStr: String?, model: Int) {
        if (dataStr != null)
            dataStrList = dataStr.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        this.SHOW_MODEL = model
        initData()
    }


    private fun initData() {
        LogUtils.d(Thread.currentThread().name + "initData: " + SHOW_MODEL)
        var dataLength: Int
        when (SHOW_MODEL) {
            SHOW_MODEL_ALL -> {
                dataLength = dataStrList!!.size

                if (dataLength > mWidth) {
                    dataLength = mWidth.toInt()
                }
                data = FloatArray(dataLength)
                for (i in 0 until dataLength) {
                    data!![i] = java.lang.Float.parseFloat(dataStrList!![i])
                }
                intervalNumHeart = data!!.size
                intervalRowHeart = mWidth / intervalNumHeart
                intervalColumnHeart = mHeight / (MAX_VALUE * 2)
            }
            SHOW_MODEL_DYNAMIC_SCROLL -> {
                dataLength = dataStrList!!.size

                data = FloatArray(dataLength)
                for (i in 0 until dataLength) {
                    data!![i] = java.lang.Float.parseFloat(dataStrList!![i])
                }
                intervalRowHeart = mWidth / dip2px(context, INTERVAL_SCROLL_REFRESH)
                intervalNumHeart = (mWidth / intervalRowHeart).toInt()
                intervalColumnHeart = mHeight / (MAX_VALUE * 2)
                startScrollTimer()
            }
            SHOW_MODEL_DYNAMIC_REFRESH -> {


                intervalRowHeart = mWidth / dip2px(context, INTERVAL_SCROLL_REFRESH)
                intervalNumHeart = (mWidth / intervalRowHeart).toInt()
                intervalColumnHeart = mHeight / (MAX_VALUE * 2)
            }
        }

    }

    private fun startScrollTimer() {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                if (scrollIndex < data!!.size) {
                    scrollIndex++
                } else {
                    scrollIndex = 0
                }
            }
        }
        timer.schedule(timerTask, 0, 50)
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        timer.cancel()
    }

}
