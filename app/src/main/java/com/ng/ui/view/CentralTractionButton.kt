package com.ng.ui.view

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.OvershootInterpolator
import android.widget.CompoundButton
import android.widget.RadioButton
import com.ng.ngcommon.util.LogUtils
import com.ng.ui.R

/**
 * Created by GG on 2017/11/2.
 */
class CentralTractionButton : RadioButton {
    //四个图片的id
    private var normalexternalbackground: Int = 0
    private var normalinsidebackground: Int = 0
    private var selectedinsidebackground: Int = 0
    private var selectedexternalbackground: Int = 0
    //文字
    private var textdimension: Float = 0f
    private var text: String = ""
    //绘制图形的画笔
    private var bmPaint: Paint? = null
    //图形偏移距离
    private var offsetDistanceLimit: Float = 0.toFloat()

    //组件宽高
    private var mWidth: Float = 0.toFloat()
    private var mHeight: Float = 0.toFloat()
    //中心点坐标,相较于屏幕
    private var centerX: Float = 0.toFloat()
    private var centerY: Float = 0.toFloat()
    //中心点坐标,相较于组件内
    private var centerx: Float = 0.toFloat()
    private var centery: Float = 0.toFloat()


    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ctattrs)
        text = ta.getString(R.styleable.ctattrs_text)
        textdimension = ta.getDimension(R.styleable.ctattrs_textdimension, 1f)
        normalexternalbackground = ta.getResourceId(R.styleable.ctattrs_normalexternalbackground, 0)
        normalinsidebackground = ta.getResourceId(R.styleable.ctattrs_normalinsidebackground, 0)
        selectedinsidebackground = ta.getResourceId(R.styleable.ctattrs_selectedinsidebackground, 0)
        selectedexternalbackground = ta.getResourceId(R.styleable.ctattrs_selectedexternalbackground, 0)

        //打印所有的属性
        val count = attrs.attributeCount
        for (i in 0..count - 1) {
            val attrName = attrs.getAttributeName(i)
            val attrVal = attrs.getAttributeValue(i)
            LogUtils.d("attrName = $attrName , attrVal = $attrVal")
        }
        ta.recycle()
        init()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mWidth = measuredWidth.toFloat()
        mHeight = measuredHeight.toFloat()
        LogUtils.d("onLayout: $mWidth $mHeight")
        //可供位移的距离
        offsetDistanceLimit = mWidth / 6
        centerY = ((getBottom() + getTop()) / 2).toFloat()
        centerX = ((getRight() + getLeft()) / 2).toFloat()
        centerx = mWidth / 2
        centery = mHeight / 2
        LogUtils.d("中心点坐标: $centerX $centerY")
        init()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    //轨迹圆外径的半径mR = ob
    var mR: Float = 0.toFloat()
    //背景图图形的半径 = 长宽(这里类似于直径)/2 = ob/2
    var mr: Float = 0.toFloat()

    private fun init() {
        initPaint()
        LogUtils.d("-----init-----")
        //得到组件宽高中的较小值,再/2得到ob的距离
        if (mHeight > mWidth) mR = mHeight / 2 else mR = mWidth / 2
        LogUtils.d("ob的距离:" + mR)
        mr = mR / 2

        // 背景图绘制区域
        mExternalDestRect = Rect((centerx - mr).toInt(), (centery - mr).toInt(),
                (centerx + mr).toInt(),
                (centery + mr).toInt())
        //初始化: 75 75 225 225

        // 中心图绘制区域
        mInsideDestRect = Rect((centerx - mr).toInt(), (centery - mr).toInt(),
                (centerx + mr).toInt(),
                (centery + mr).toInt())


        // 内外的图形
        externalBD = resources.getDrawable(normalexternalbackground) as BitmapDrawable
        mExternalSrcRect = Rect(0, 0, externalBD!!.intrinsicWidth, externalBD!!.intrinsicHeight)

        insidelBD = resources.getDrawable(normalinsidebackground) as BitmapDrawable
        mInsideSrcRect = Rect(0, 0, insidelBD!!.intrinsicWidth, insidelBD!!.intrinsicHeight)

        setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                externalBD = resources.getDrawable(selectedexternalbackground) as BitmapDrawable
                insidelBD = resources.getDrawable(selectedinsidebackground) as BitmapDrawable

                val pvhX = PropertyValuesHolder.ofFloat("scaleX", 0.1f,
                        1f)
                val pvhY = PropertyValuesHolder.ofFloat("scaleY", 0.1f,
                          1f)
                val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, pvhX, pvhY)
                objectAnimator.duration = 500
                val overshootInterpolator = OvershootInterpolator(1.2f)
                objectAnimator.interpolator = overshootInterpolator
                objectAnimator.start()
                postInvalidate()
            } else {
                externalBD = resources.getDrawable(normalexternalbackground) as BitmapDrawable
                insidelBD = resources.getDrawable(normalinsidebackground) as BitmapDrawable
                postInvalidate()
            }
        }
    }

    //初始化画笔
    private fun initPaint() {
        //绘制图形的画笔
        bmPaint = Paint()
        bmPaint!!.isAntiAlias = true//抗锯齿功能
        bmPaint!!.style = Paint.Style.FILL//设置填充样式   Style.FILL/Style.FILL_AND_STROKE/Style.STROKE
    }

    internal var mExternalSrcRect: Rect? = null
    internal var mExternalDestRect: Rect? = null
    internal var mInsideSrcRect: Rect? = null
    internal var mInsideDestRect: Rect? = null


    var externalBD: BitmapDrawable? = null
    var insidelBD: BitmapDrawable? = null
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //暂时画个边框表示范围
        val bianKuanPaint = Paint()
        bianKuanPaint.isAntiAlias = true
        bianKuanPaint.strokeWidth = 2f
        bianKuanPaint.style = Paint.Style.STROKE
        bianKuanPaint.color = resources.getColor(R.color.black)
        canvas.drawRect(0f, 0f, this.width.toFloat(), this.height.toFloat(), bianKuanPaint)


        //绘制默认状态下背景图
        val externalBM = externalBD!!.bitmap
        canvas.drawBitmap(externalBM, mExternalSrcRect, mExternalDestRect, bmPaint)


        //绘制默认状态下中心图
        val insidelBM = insidelBD!!.bitmap
        canvas.drawBitmap(insidelBM, mInsideSrcRect, mInsideDestRect, bmPaint)
    }


    override fun setOnCheckedChangeListener(listener: CompoundButton.OnCheckedChangeListener) {
        super.setOnCheckedChangeListener(listener)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {

        //相较于视图的XY
        var mx1 = event.x
        var my1 = event.y
        var mx2 = event.x
        var my2 = event.y  //需要减掉标题栏高度
        LogUtils.d("---onTouchEvent---")
        LogUtils.d(" 点击坐标：$mx1 $my1")
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                LogUtils.d("ACTION_DOWN")
                //TODO 弹动一下的动画效果
                postInvalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                LogUtils.d("ACTION_MOVE:" + scrollX + " " + scrollY)

                //判断点击位置距离中心的距离
                var distanceToCenter = getDistanceTwoPoint(mx1, my1, centerx, centery)

                var mExternalOffesetLimit = mr / 4

                var mInsideOffesetLimit = mr / 2


                //如果区域在轨迹圆内则移动
                if (distanceToCenter > mExternalOffesetLimit) {
                    //如果点击位置在组件外，则获取点击位置和中心点连线上的一点(该点满足矩形在组件内)为中心作图
                    // oc/oa = od/ob
                    var od = mx1 - centerx
                    var ob = getDistanceTwoPoint(centerx, centery, mx1, my1)
                    var oc = od / ob * mExternalOffesetLimit
                    // ca/oa = db/ob
                    var db = centery - my1
                    var ac = db / ob * mExternalOffesetLimit
                    //得到ac和oc判断得出a点的位置
                    mx1 = centerx + oc
                    my1 = centery - ac

                    od = mx2 - centerx
                    ob = getDistanceTwoPoint(centerx, centery, mx2, my2)
                    oc = od / ob * mInsideOffesetLimit
                    // ca/oa = db/ob
                    db = centery - my2
                    ac = db / ob * mInsideOffesetLimit
                    //得到ac和oc判断得出a点的位置
                    mx2 = centerx + oc
                    my2 = centery - ac
                } else {
                    //获得与中点的距离，*2,如图3

                    var ab = my2 - centery
                    var bo = mx2 - centerx
                    LogUtils.d("ab:" + ab + "  bo:" + bo)
                    mx2 = centerx + 2f * bo
                    my2 = centery + 2f * ab
                    distanceToCenter = getDistanceTwoPoint(mx1, my1, centerx, centery)
                    if (distanceToCenter > mExternalOffesetLimit) {
                        return super.onTouchEvent(event)
                    }
                }


                var left: Int = (mx1 - mr).toInt()
                var right: Int = (mx1 + mr).toInt()
                var top: Int = (my1 - mr).toInt()
                var bottom: Int = (my1 + mr).toInt()
                //更新背景图绘制区域
                mExternalDestRect = Rect(left, top, right, bottom)

                left = (mx2 - mr).toInt()
                right = (mx2 + mr).toInt()
                top = (my2 - mr).toInt()
                bottom = (my2 + mr).toInt()
                //更新中心图绘制区域
                mInsideDestRect = Rect(left, top, right, bottom)
                postInvalidate()
            }
            MotionEvent.ACTION_UP -> {
                LogUtils.d("ACTION_UP")

                //复原背景图绘制区域
                mExternalDestRect = Rect((centerx - mr).toInt(), (centery - mr).toInt(),
                        (centerx + mr).toInt(),
                        (centery + mr).toInt())
                //复原中心图绘制区域
                mInsideDestRect = Rect((centerx - mr).toInt(), (centery - mr).toInt(),
                        (centerx + mr).toInt(),
                        (centery + mr).toInt())
                postInvalidate()
            }
        }

        LogUtils.d("---end---")
        return super.onTouchEvent(event)
    }


    //得到两点之间的距离
    fun getDistanceTwoPoint(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return Math.sqrt((Math.pow((x1 - x2).toDouble(), 2.toDouble()) +
                Math.pow((y1 - y2).toDouble(), 2.toDouble()))).toFloat()
    }

}
