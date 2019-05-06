package com.ng.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.ng.ui.R

class WaveView : View {

    private var color: Int
    private var paint: Paint
    private var path: Path
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var cX: Float = 0f
    private var cY: Float = 0f
    private var xOffset = 0f

    private var progress = 50
    private val clipPath: Path
    private var left: Float = 0f
    private var top: Float = 0f
    private var right: Float = 0f
    private var bottom: Float = 0f

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val array = context?.obtainStyledAttributes(attrs, R.styleable.WaveView)
        this.color = array!!.getColor(R.styleable.WaveView_WaveView_color, Color.BLUE)
        paint = Paint()
        path = Path()
        clipPath = Path();
        array.recycle()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val dx = mWidth / 4f
        val dy = mHeight / 8f
        val yOffset = (100 - progress) / 100f * height
        val p1 = floatArrayOf(xOffset, yOffset)
        val p2 = floatArrayOf(dx, -dy)
        val p3 = floatArrayOf(2 * dx, 0f)
        val p4 = floatArrayOf(dx, dy)
        val p5 = floatArrayOf(2 * dx, 0f)
        val p6 = floatArrayOf(dx, -dy)
        val p7 = floatArrayOf(2 * dx, 0f)
        val p8 = floatArrayOf(dx, dy)
        val p9 = floatArrayOf(2 * dx, 0f)

        paint.style = Paint.Style.FILL

        path.reset()
        path.moveTo(p1[0], p1[1])
        path.rQuadTo(p2[0], p2[1], p3[0], p3[1])
        path.rQuadTo(p4[0], p4[1], p5[0], p5[1])
        path.rQuadTo(p6[0], p6[1], p7[0], p7[1])
        path.rQuadTo(p8[0], p8[1], p9[0], p9[1])
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(0f, height.toFloat())
        path.close()

        clipPath.reset()
        left = 0f
        top = 0f
        right = mWidth.toFloat()
        bottom = height.toFloat()
        clipPath.addArc(left, top, right, bottom, 0f, 360f)

        canvas!!.clipPath(clipPath)
        canvas!!.drawPath(path, paint)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measuredWidth
        mHeight = measuredHeight
        cX = (mWidth / 2).toFloat()
        cY = (mHeight / 2).toFloat()

    }
}