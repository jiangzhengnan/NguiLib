package com.ng.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import java.util.*

/**
 * 描述:
 * @author Jzn
 * @date 2020-01-09
 */
class SoundView : View {
    private var mPaint: Paint? = null
    private var mHeight: Float = 0.toFloat()
    private var mWidth: Float = 0.toFloat()
    private var mPath: Path? = null
    //周期
    private val T = 2f
    //总偏移量
    private var mAllOffsetX = 0
    //每一条直线的偏移量
    private var mEachOffsetX = dp2px(1f)
    //垂直方向间距
    private val mSpaceY = dp2px(2f).toFloat()
    // 振幅
    private var mAmplitude = dp2px(10f)
    //间距
    private var mSpaceX = dp2px(5f)
    //小ball半径
    private var mBallRadius = dp2px(1f).toFloat()
    //上颜色
    private val mTopColor = Color.parseColor("#0A84FF")
    private val mBottomColor = Color.parseColor("#1BE5FF")
    //两个峰值
    private var mPeakValueIndex1 = 1
    private var mPeakValueIndex2 = 1
    //中心点坐标
    private var mCenterX: Float = 0.toFloat()
    private var mCenterY: Float = 0.toFloat()
    private var isWaveAnimRunning = false
    private var mWaveAnimator: ValueAnimator? = null
    //sin和cos的切换
    private var mFunctionChangeTag = true
    //透明度遮罩
    private var mRadialGradient: RadialGradient? = null
    //间距倍数
    private var mXSpaceMultiple = 1f
    //音量
    private var mSourceVolume = 0f

    private var mVolume = 0f
    private var mMaxVolume = 1f

    fun setRealVolume(volume: Int) {
        if (volume < 0 || volume > 100) {
            return
        }
        mSourceVolume = volume.toFloat()
        mMaxVolume = volume.toFloat()
        postInvalidate()
    }

    //添加切换的估值器
    fun setVolume(volume: Int) {
        if (volume < 0 || volume > 100) {
            return
        }
        mSourceVolume = volume.toFloat()
        //mMaxVolume = VOLUME_INIT + mSourceVolume;

        val disparity = Math.abs(mSourceVolume - mMaxVolume).toInt()
        if (disparity > VOLUE_THRESHOLD) {
            this.mMaxVolume = if (mMaxVolume < mSourceVolume) mMaxVolume + 5 else mMaxVolume - 5
            if (mMaxVolume > 100) {
                mMaxVolume = 100f
            } else if (mMaxVolume < VOLUME_INIT) {
                mMaxVolume = VOLUME_INIT.toFloat()
            }
            //音量越大间距越小
            mXSpaceMultiple = (100f - mMaxVolume) / 100f / 2f
            postInvalidate()
        }
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        //关闭硬件加速，否则遮罩会有问题
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        //初始值
        mMaxVolume = VOLUME_INIT.toFloat()
        mPaint = Paint()
        mPaint!!.isAntiAlias = true//消除锯齿
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.strokeWidth = dp2px(2f).toFloat()
        mPath = Path()
        //anim
        mWaveAnimator = ValueAnimator.ofFloat(100f)
        mWaveAnimator!!.duration = (10 * 1000).toLong()
        mWaveAnimator!!.repeatCount = -1
        mWaveAnimator!!.interpolator = LinearInterpolator()
        mWaveAnimator!!.addUpdateListener {
            mVolume = mMaxVolume / 100f

            mAmplitude = (mHeight / 4 * mVolume).toInt()

            //voice speed 5 - 20
            if (mSourceVolume < 5) {
                mAllOffsetX += dp2px(5f)
            } else {
                val speed = 2 - (mSourceVolume / 100f * 1).toInt()
                mAllOffsetX += dp2px(speed.toFloat())
            }

            if (mAllOffsetX >= Integer.MAX_VALUE - 10000) {
                mAllOffsetX = 0
            }


            postInvalidate()
        }
        mWaveAnimator!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationRepeat(animation: Animator) {
                super.onAnimationRepeat(animation)
                //chageWave();
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                isWaveAnimRunning = false
            }
        })
        mFunctionChangeTag = Random().nextBoolean()
        mPeakValueIndex1 = Random().nextInt(WAVE_LINE_NUMBER / 3)
        mPeakValueIndex2 = WAVE_LINE_NUMBER / 3 + Random().nextInt(WAVE_LINE_NUMBER / 3)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (i in 1..WAVE_LINE_NUMBER) {
            mEachOffsetX = dp2px(10f) + dp2px(i.toFloat())
            val tempAmplitude: Int
            //峰值中间最大两边递减
            val chaju: Int
            val disTop: Int
            val disBot: Int
            if (i < mPeakValueIndex1) {
                chaju = Math.abs(i - mPeakValueIndex1)
            } else if (mPeakValueIndex1 < i && i < mPeakValueIndex2) {
                disTop = i - mPeakValueIndex1
                disBot = mPeakValueIndex2 - i
                chaju = Math.min(disTop, disBot)
            } else if (i > mPeakValueIndex2) {
                chaju = i - mPeakValueIndex2
            } else {
                chaju = 0
            }
            tempAmplitude = (WAVE_LINE_NUMBER - chaju * 3) * mAmplitude / WAVE_LINE_NUMBER

            //间距要越来越大
            mSpaceX = (dp2px(2f) + i / 2f * mXSpaceMultiple).toInt()
            //小球变大
            mBallRadius = 1 + i.toFloat() / WAVE_LINE_NUMBER.toFloat() * 1f
            //透明度
            val alpha = 25 + 230 * i / WAVE_LINE_NUMBER
            mPaint!!.alpha = alpha


            val xOffset = mAllOffsetX + mEachOffsetX * (WAVE_LINE_NUMBER - i)


            drawVoiceLine(canvas,
                    xOffset,
                    (mHeight / 3 + i * mSpaceY).toInt(),
                    tempAmplitude)
        }
    }


    /**
     * y=Asin(ωx+φ)+k
     *
     * @param canvas    画板
     * @param xOffset   x轴偏移量
     * @param yLocation y轴坐标
     */
    private fun drawVoiceLine(canvas: Canvas, xOffset: Int, yLocation: Int, tempAmplitude: Int) {
        var index = 0
        mPath!!.reset()
        mPath!!.moveTo(0f, 0f)


        while (index <= mWidth.toInt()) {
            //a的范围:0~a~0
            val singleTempAmp = 4 * tempAmplitude * index / mWidth - 4 * tempAmplitude * index / mWidth * index / mWidth

            val endY: Int
            //周期w
            if (mFunctionChangeTag) {
                endY = (Math.sin((index.toFloat() + xOffset.toFloat()).toDouble() * T.toDouble() * Math.PI / mWidth + 0) * singleTempAmp + yLocation).toInt()
            } else {
                endY = (Math.cos((index.toFloat() + xOffset.toFloat()).toDouble() * T.toDouble() * Math.PI / mWidth + 0) * singleTempAmp + yLocation).toInt()
            }
            mPath!!.moveTo(index.toFloat(), endY.toFloat())
            mPath!!.addCircle(index.toFloat(), endY.toFloat(), mBallRadius, Path.Direction.CCW)
            index += mSpaceX
        }
        //渐变 遮罩
        val mLinearGradient = LinearGradient(
                mCenterX, yLocation.toFloat() - tempAmplitude.toFloat() - mBallRadius,
                mCenterX, yLocation.toFloat() + tempAmplitude.toFloat() + mBallRadius,
                mTopColor,
                mBottomColor,
                Shader.TileMode.CLAMP)
        val mComposeShader = ComposeShader(mLinearGradient, mRadialGradient!!,
                PorterDuff.Mode.DST_ATOP)
        mPaint!!.shader = mComposeShader
        canvas.drawPath(mPath!!, mPaint!!)
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mHeight = measuredHeight.toFloat()
        mWidth = measuredWidth.toFloat()
        mCenterX = mWidth / 2
        mCenterY = mHeight / 2

        mRadialGradient = RadialGradient(
                mCenterX,
                mCenterY,
                mCenterX - dp2px(32f),
                mTopColor,
                Color.TRANSPARENT,
                Shader.TileMode.CLAMP)
    }

    //开始波浪 动画
    fun startWaveAnim() {
        if (!isWaveAnimRunning) {
            isWaveAnimRunning = true
            mWaveAnimator!!.start()
        }
    }

    //    private void chageWave() {
    //        mFunctionChangeTag = !mFunctionChangeTag;
    //        mPeakValueIndex1 = new Random().nextInt(WAVE_LINE_NUMBER / 3);
    //        mPeakValueIndex2 = WAVE_LINE_NUMBER / 3 + new Random().nextInt(WAVE_LINE_NUMBER / 3);
    //    }

    fun stopWaveAnim() {
        isWaveAnimRunning = false
        if (mWaveAnimator != null) {
            mWaveAnimator!!.pause()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mWaveAnimator != null) {
            stopWaveAnim()
            mWaveAnimator!!.cancel()
            mWaveAnimator!!.end()
        }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.INVISIBLE || visibility == View.GONE) {
            stopWaveAnim()
        } else {
            startWaveAnim()
        }
    }

    private fun dp2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    companion object {
        //直线条数
        private val WAVE_LINE_NUMBER = 20
        //音量初始值and最低值
        private val VOLUME_INIT = 10
        //音量间隔最大值
        private val VOLUME_MAX = 10
        //音量间隔阈值
        private val VOLUE_THRESHOLD = 5
    }
}
