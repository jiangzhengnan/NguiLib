package com.ng.ui.other.circularpro

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi
import kotlin.math.min

/**
 *
 * 中心白圈
 *
 * 外侧蓝圈
 *
 */
class CircularAnimProgressView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
  private var line = 0
  private var centerX = 0f
  private var centerY = 0f
  private var centerR = 0f
  private var outR = 0f
  private var outCircleSpace = dip2px(23f)
  private var outRWidth = dip2px(20f)
  private var title = ""
  private var showStr = ""

  private var lineEffect = DashPathEffect(floatArrayOf(1f, 15f), 0f)
  private var outRect: RectF = RectF()
  private var mPaint: Paint = Paint()

  private var nowAnimDegree = -100f
  private var degreeAnim: ValueAnimator

  private var c1Line = 60f
  private var c2Line = 60f
  private var c3Line = 180f

  init {
    mPaint.apply {
      isAntiAlias = true
      flags = Paint.ANTI_ALIAS_FLAG
      strokeCap = Paint.Cap.ROUND
      style = Paint.Style.STROKE
    }
    degreeAnim = ValueAnimator.ofFloat(-100f, 260f)
    degreeAnim.duration = 300 * 1000
    degreeAnim.interpolator = LinearInterpolator()
    degreeAnim.repeatCount = -1
    degreeAnim.addUpdateListener { animation ->
      nowAnimDegree = animation.animatedValue as Float
      postLineAnim()
      invalidate()
    }
  }

  var lastTime = 0L
  var state = 1
  private fun postLineAnim() {
    val nowTime = System.currentTimeMillis()
    if (nowTime - lastTime < (1000 / 60)) {
      return
    }
    lastTime = nowTime
    val temp = 0.05f
    //最小20，最大80
    if (state == 1) {
      //c1+ c2- c3
      if (c1Line + 1 < 120f && c2Line - 1 > 20f) {
        c1Line += temp
        c2Line -= temp
        c3Line = 300 - c1Line - c2Line
      } else {
        state++
      }
    } else if (state == 2) {
      //c2+ c3- c1
      if (c2Line + 1 < 120f && c3Line - 1 > 20f) {
        c2Line += temp
        c3Line -= temp
        c1Line = 300 - c2Line - c3Line
      } else {
        state++
      }
    } else if (state == 3) {
      //c3+ c1- c2
      if (c3Line + 1 < 180f && c1Line - 1 > 20f) {
        c3Line += temp
        c1Line -= temp
        c2Line = 300 - c1Line - c3Line
      } else {
        state++
      }
    }
    if (state > 3) {
      state = 1
    }
  }

  fun setData(s1: String, s2: String) {
    title = s1
    showStr = s2
    invalidate()
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    //中心白圈
    mPaint.apply {
      color = Color.WHITE
      style = Paint.Style.FILL
    }
    mPaint.setShadowLayer(
      dip2px(50f), 0f, 0f,
      Color.parseColor("#22FFA500")
    )
    canvas.drawCircle(centerX, centerY, centerR, mPaint)
    mPaint.setShadowLayer(0f, 0f, 0f, Color.TRANSPARENT)
    // 中心点点小圈边
    mPaint.apply {
      color = Color.parseColor("#FFA500")
      pathEffect = lineEffect
      style = Paint.Style.STROKE
      strokeWidth = dip2px(1f).toFloat()
    }
    canvas.drawCircle(centerX, centerY, centerR - dip2px(10f), mPaint)
    //文案
    mPaint.apply {
      color = Color.parseColor("#FFA500")
      textSize = dip2px(20f)
      pathEffect = null
      style = Paint.Style.FILL
    }
    canvas.drawText(
      title,
      centerX - mPaint.measureText(title) / 2,
      centerY - dip2px(20f), mPaint
    )
    canvas.drawText(
      showStr,
      centerX - mPaint.measureText(showStr) / 2,
      centerY + dip2px(20f), mPaint
    )
    //外部弧
    canvas.save()
    mPaint.apply {
      color = Color.parseColor("#F5DEB3")
      strokeWidth = outRWidth
      strokeCap = Paint.Cap.ROUND
      style = Paint.Style.STROKE
    }
    outRect.apply {
      left = 0f + outRWidth
      top = 0f + outRWidth
      right = line.toFloat() - outRWidth
      bottom = line.toFloat() - outRWidth
    }
    canvas.save()
    canvas.rotate(nowAnimDegree, centerX, centerY)
    Log.d("nangua", "nowde:" + nowAnimDegree)

    //弧1
    var tempDegree = nowAnimDegree
    canvas.drawArc(
      outRect, tempDegree, c1Line, false, mPaint
    )
    tempDegree += c1Line
    tempDegree += 20f
    //弧2
    mPaint.color = Color.parseColor("#F4A460")
    canvas.drawArc(
      outRect, tempDegree, c2Line, false, mPaint
    )
    tempDegree += c2Line
    tempDegree += 20f
    //弧3
    mPaint.color = Color.parseColor("#FFA500")
    canvas.drawArc(
      outRect, tempDegree, c3Line, false, mPaint
    )
    canvas.restore()

  }


  override fun onVisibilityChanged(changedView: View, visibility: Int) {
    super.onVisibilityChanged(changedView, visibility)
    if (visibility == VISIBLE) {
      degreeAnim.resume()
    } else {
      degreeAnim.pause()
    }

  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    if (!degreeAnim.isStarted) {
      degreeAnim.start()
    }
  }


  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    degreeAnim.cancel()

  }

  @SuppressLint("DrawAllocation")
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    line = min(measuredWidth, measuredHeight)
    centerX = (line / 2).toFloat()
    centerY = (line / 2).toFloat()
    //中心圈小大小为
    outR = (line / 2 - outRWidth / 2).toFloat()
    centerR = (line / 2 - outCircleSpace - outRWidth)
    invalidate()
  }

  private fun dip2px(dpValue: Float): Float {
    val scale = resources?.displayMetrics?.density
    return (dpValue * scale!! + 0.5f)
  }
}