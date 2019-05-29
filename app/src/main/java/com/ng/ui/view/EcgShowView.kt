package com.ng.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.ng.ui.LogUtils
import com.ng.ui.R
import java.util.*

class EcgShowView : View {
    //显示模式，分为两种 1.全部显示 2.动态滑动显示  3.动态更新显示
    private var SHOW_MODEL = 0
    val SHOW_MODEL_ALL = 0x00
    val SHOW_MODEL_DYNAMIC_SCROLL = 0x01
    val SHOW_MODEL_DYNAMIC_REFRESH = 0x02

    //基本参数
    private var mWidth: Float = 0.toFloat()
    private var mHeight: Float = 0.toFloat()
    private var paint: Paint? = null
    private var path: Path? = null
    private var dataStrList: Array<String>? = null

    //滑动
    private var scrollIndex = 0
    internal var timer: Timer? = null
    internal var timerTask: TimerTask? = null
    private val INTERVAL_SCROLL_REFRESH = 80f //滑动刷新都有使用的参数

    //刷新
    private var refreshList: MutableList<Float>? = null
    private var showIndex: Int = 0

    //心电
    private val MAX_VALUE = 20f //峰值
    private var intervalNumHeart: Int = 0
    private var intervalRowHeart: Float = 0.toFloat()
    private var intervalColumnHeart: Float = 0.toFloat()
    private val HEART_LINE_STROKE_WIDTH = 5f
    private var data: FloatArray? = null
    private var mHeartLinestrokeWidth: Float = 0.toFloat()

    //网格
    private val GRID_LINE_STROKE_WIDTH = 3f
    private val GRID_WIDTH_AND_HEIGHT = 10f
    private var row: Int = 0
    private var intervalRow: Float = 0.toFloat()
    private var column: Int = 0
    private var intervalColumn: Float = 0.toFloat()
    private var mGridLinestrokeWidth: Float = 0.toFloat()
    private var mGridstrokeWidthAndHeight: Float = 0.toFloat()


    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        paint = Paint()
        path = Path()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mWidth = measuredWidth.toFloat()
        mHeight = measuredHeight.toFloat()
        mGridLinestrokeWidth = dip2px(GRID_LINE_STROKE_WIDTH).toFloat()
        mGridstrokeWidthAndHeight = dip2px(GRID_WIDTH_AND_HEIGHT).toFloat()

        column = (mWidth / mGridstrokeWidthAndHeight).toInt()
        intervalColumn = mWidth / column
        row = (mHeight / mGridstrokeWidthAndHeight).toInt()
        intervalRow = mHeight / row

        mHeartLinestrokeWidth = dip2px(HEART_LINE_STROKE_WIDTH).toFloat()
        initData()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制网格
        //TODO 动态的话只绘制一次网格？

        drawGrid(canvas)

        //绘制心电图
        when (SHOW_MODEL) {
            SHOW_MODEL_ALL -> drawHeartAll(canvas)
            SHOW_MODEL_DYNAMIC_SCROLL -> drawHeartScroll(canvas)
            SHOW_MODEL_DYNAMIC_REFRESH -> drawHeartRefresh(canvas)
        }

    }

    //接受心电数据的方法
    fun showLine(point: Float) {
        LogUtils.d("showLine:$point $SHOW_MODEL$intervalNumHeart")
        if (refreshList == null) {
            refreshList = ArrayList()
            data = FloatArray(intervalNumHeart)
        }
        refreshList!!.add(point)
        LogUtils.d(Thread.currentThread().name + "0:   " + data!![0])

        LogUtils.d("1:   " + refreshList!!.toString())
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

        val nowIndex = if (refreshList == null) 0 else refreshList!!.size //当前长度

        if (nowIndex == 0) {
            return
        }

        if (nowIndex < intervalNumHeart) {
            showIndex = nowIndex - 1
        } else {
            showIndex = (nowIndex - 1) % intervalNumHeart
        }


        for (i in 0 until intervalNumHeart) {
            if (i > refreshList!!.size - 1) {
                break
            }
            if (nowIndex <= intervalNumHeart) {

                data!![i] = refreshList!![i]
            } else {
                val times = (nowIndex - 1) / intervalNumHeart

                val temp = times * intervalNumHeart + i

                if (temp < nowIndex) {
                    data?.set(i, refreshList!![temp])
                }
            }
        }

        logdata()

        //绘制出data
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
        LogUtils.d("data: $str")

    }

    // 绘制心电滚动视图
    private fun drawHeartScroll(canvas: Canvas) {
        if (data == null || data!!.size == 0) {
            return
        }
        paint!!.reset()
        path!!.reset()
        paint!!.style = Paint.Style.STROKE
        paint!!.color = Color.parseColor("#31CE32")
        paint!!.strokeWidth = mGridLinestrokeWidth
        paint!!.isAntiAlias = true
        path!!.moveTo(0f, mHeight / 2)

        var scrollStartIndex = 0
        var scrollEndIndex = 0

        scrollEndIndex = scrollIndex

        scrollStartIndex = scrollEndIndex - intervalNumHeart
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
            LogUtils.d("drawHeartScroll $nowX $nowY")
        }

        canvas.drawPath(path!!, paint!!)
        postInvalidate()
    }

    //绘制全部心电图
    private fun drawHeartAll(canvas: Canvas) {
        if (data == null || data!!.size == 0) {
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

    //绘制网格
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
        //当前模式
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
                intervalRowHeart = mWidth / dip2px(INTERVAL_SCROLL_REFRESH)
                intervalNumHeart = (mWidth / intervalRowHeart).toInt()
                intervalColumnHeart = mHeight / (MAX_VALUE * 2)
                startScrollTimer()
            }
            SHOW_MODEL_DYNAMIC_REFRESH -> {


                intervalRowHeart = mWidth / dip2px(INTERVAL_SCROLL_REFRESH)
                intervalNumHeart = (mWidth / intervalRowHeart).toInt()
                intervalColumnHeart = mHeight / (MAX_VALUE * 2)
                LogUtils.d("what the fk ? $mHeight")

                LogUtils.d("what the fk ? " + MAX_VALUE * 2)

                LogUtils.d("what the fk ? $intervalColumnHeart")
            }
        }
        LogUtils.d(Thread.currentThread().name + "initDataEnd : " + intervalColumnHeart)

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
        timer!!.schedule(timerTask, 0, 50)
    }


    private fun dip2px(dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }
}