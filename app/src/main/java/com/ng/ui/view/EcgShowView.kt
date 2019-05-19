package com.ng.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class EcgShowView : View {
    private var mWidth: Float = 0.toFloat()
    private var mHeight: Float = 0.toFloat()
    private var paint: Paint? = null
    private var path: Path? = null
    private var dataStrList: Array<String>? = null
    private var intervalNumHeart: Int = 0
    private var intervalRowHeart: Float = 0.toFloat()
    private var intervalColumnHeart: Float = 0.toFloat()
    private var data: FloatArray? = null
    private var mHeartLinestrokeWidth: Float = 0.toFloat()
    private var row: Int = 0
    private var intervalRow: Float = 0.toFloat()
    private var column: Int = 0
    private var intervalColumn: Float = 0.toFloat()
    private var mGridLinestrokeWidth: Float = 0.toFloat()
    private var mGridstrokeWidthAndHeight: Float = 0.toFloat()

    //心电
    private val MAX_VALUE = 20f //峰值
    private val HEART_LINE_STROKE_WIDTH = 5f

    //网格
    private val GRID_LINE_STROKE_WIDTH = 3f
    private val GRID_WIDTH_AND_HEIGHT = 10f

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        paint = Paint()
        path = Path()
        //setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mWidth = measuredWidth.toFloat()
        mHeight = measuredHeight.toFloat()
        mGridLinestrokeWidth = dip2px(GRID_LINE_STROKE_WIDTH).toFloat()
        mGridstrokeWidthAndHeight = dip2px(GRID_WIDTH_AND_HEIGHT).toFloat()
        column = (mWidth / mGridstrokeWidthAndHeight).toInt();

        intervalColumn = mWidth / column
        row = (mHeight / mGridstrokeWidthAndHeight).toInt()

        intervalRow = mHeight / row

        mHeartLinestrokeWidth = dip2px(HEART_LINE_STROKE_WIDTH).toFloat()
        initData()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制网格
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
        //绘制心电图
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

    fun setData(dataStr: String) {
        dataStrList = dataStr.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        initData()
    }

    private fun initData() {
        try {
            var dataLength = dataStrList!!.size
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
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun px2dip(px: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

    private fun dip2px(dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }


}
