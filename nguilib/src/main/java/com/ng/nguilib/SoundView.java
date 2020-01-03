package com.ng.nguilib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.Random;

/**
 * 描述:语音识别view
 *
 * @author Jzn
 * @date 2019-12-14
 */
public class SoundView extends View {
    private Paint mPaint;
    private float mHeight;
    private float mWidth;
    private Path mPath;
    //周期
    private float T = 2f;
    //总偏移量
    private int mAllOffsetX = 0;
    //每一条直线的偏移量
    private int mEachOffsetX = dp2px(1);
    //垂直方向间距
    private float mSpaceY = dp2px(2);
    //直线条数
    private final static int WAVE_LINE_NUMBER = 20;
    // 振幅
    private int mAmplitude = dp2px(10);
    //间距
    private int mSpaceX = dp2px(5);
    //小ball半径
    private float mBallRadius = dp2px(1);
    //上颜色
    private int mTopColor = Color.parseColor("#00BFFF");
    private int mBottomColor = Color.parseColor("#2126D4");
    //两个峰值
    private int mPeakValueIndex1 = 1;
    private int mPeakValueIndex2 = 1;
    //中心点坐标
    private float mCenterX;
    private float mCenterY;
    private boolean isWaveAnimRunning = false;
    private ValueAnimator mWaveAnimator;
    //sin和cos的切换
    private boolean mFunctionChangeTag = true;
    //透明度遮罩
    private RadialGradient mRadialGradient;
    //间距倍数
    private float mXSpaceMultiple = 1;
    //音量
    private float mSourceVolume = 0;

    private float mVolume = 0;
    private float mMaxVolume = 1;
    //音量初始值and最低值
    private final static int VOLUME_INIT = 10;
    //音量间隔最大值
    private final static int VOLUME_MAX = 10;
    //音量间隔阈值
    private final static int VOLUE_THRESHOLD = 10;

    //添加切换的估值器
    public void setVolume(int volume) {
        if (volume < 0 || volume > 100) {
            return;
        }
        mSourceVolume = volume;
        //mMaxVolume = VOLUME_INIT + mSourceVolume;

        int disparity = (int) Math.abs(mSourceVolume - mMaxVolume);
        if (disparity > VOLUE_THRESHOLD) {
            this.mMaxVolume = mMaxVolume < mSourceVolume ? mMaxVolume + 1 : mMaxVolume - 1;
            if (mMaxVolume > 100) {
                mMaxVolume = 100;
            } else if (mMaxVolume < VOLUME_INIT) {
                mMaxVolume = VOLUME_INIT;
            }
            //音量越大间距越小
            mXSpaceMultiple = (100f - mMaxVolume) / 100f / 2;
        }
    }

    public SoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //关闭硬件加速，否则遮罩会有问题
        this.setLayerType(LAYER_TYPE_SOFTWARE, null);
        //初始值
        mMaxVolume = VOLUME_INIT;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(dp2px(2));
        mPath = new Path();
        //anim
        mWaveAnimator = ValueAnimator.ofFloat
                (100f);
        mWaveAnimator.setDuration(10 * 1000);
        mWaveAnimator.setRepeatCount(-1);
        mWaveAnimator.setInterpolator(new LinearInterpolator());
        mWaveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mVolume = mMaxVolume / 100f;

                mAmplitude = (int) (mHeight / 4 * mVolume);

                //voice speed 5 - 20
                if (mSourceVolume < 5) {
                    mAllOffsetX += dp2px(5);
                } else {
                    int speed = 2 - (int) (mSourceVolume / 100f * 1);
                    mAllOffsetX += dp2px(speed);
                }

                if (mAllOffsetX >= Integer.MAX_VALUE - 10000) {
                    mAllOffsetX = 0;
                }


                postInvalidate();
            }
        });
        mWaveAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                //chageWave();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isWaveAnimRunning = false;
            }
        });
        mFunctionChangeTag = new Random().nextBoolean();
        mPeakValueIndex1 = new Random().nextInt(WAVE_LINE_NUMBER / 3);
        mPeakValueIndex2 = WAVE_LINE_NUMBER / 3 + new Random().nextInt(WAVE_LINE_NUMBER / 3);
    }

    public SoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 1; i <= WAVE_LINE_NUMBER; i++) {
            mEachOffsetX = dp2px(10) + dp2px(i);
            int tempAmplitude;
            //峰值中间最大两边递减
            int chaju;
            int disTop;
            int disBot;
            if (i < mPeakValueIndex1) {
                chaju = Math.abs(i - mPeakValueIndex1);
            } else if (mPeakValueIndex1 < i && i < mPeakValueIndex2) {
                disTop = i - mPeakValueIndex1;
                disBot = mPeakValueIndex2 - i;
                chaju = Math.min(disTop, disBot);
            } else if (i > mPeakValueIndex2) {
                chaju = i - mPeakValueIndex2;
            } else {
                chaju = 0;
            }
            tempAmplitude = (WAVE_LINE_NUMBER - chaju * 3) * mAmplitude / WAVE_LINE_NUMBER;

            //间距要越来越大
            mSpaceX = (int) (dp2px(2) + i / 2f * mXSpaceMultiple);
            //小球变大
            mBallRadius = 1 + ((float) i / (float) WAVE_LINE_NUMBER) * 1f;
            //透明度
            int alpha = 25 + 230 * i / WAVE_LINE_NUMBER;
            mPaint.setAlpha(alpha);


            //xoffset 改为中间高两边低
            int xOffset = (mAllOffsetX
                    + mEachOffsetX * (WAVE_LINE_NUMBER - i));


            drawVoiceLine(canvas,
                    xOffset,
                    (int) (mHeight / 3 + i * mSpaceY),
                    tempAmplitude);
        }
    }


    /**
     * y=Asin(ωx+φ)+k
     *
     * @param canvas    画板
     * @param xOffset   x轴偏移量
     * @param yLocation y轴坐标
     */
    private void drawVoiceLine(Canvas canvas, int xOffset, int yLocation, int tempAmplitude) {
        int index = 0;
        mPath.reset();
        mPath.moveTo(0, 0);


        while (index <= (int) mWidth) {
            //a的范围:0~a~0
            float singleTempAmp = 4 * tempAmplitude * index / mWidth -
                    4 * tempAmplitude * index / mWidth * index / mWidth;

            int endY;
            //周期w
            if (mFunctionChangeTag) {
                endY = (int) (Math.sin(((float) index + (float) xOffset) * T * Math.PI / mWidth + 0)
                        * singleTempAmp + yLocation);
            } else {
                endY = (int) (Math.cos(((float) index + (float) xOffset) * T * Math.PI / mWidth + 0)
                        * singleTempAmp + yLocation);
            }
            mPath.moveTo(index, endY);
            mPath.addCircle(index, endY, mBallRadius, Path.Direction.CCW);
            index += mSpaceX;
        }
        //渐变 遮罩
        LinearGradient mLinearGradient = new LinearGradient(
                mCenterX, yLocation - tempAmplitude - mBallRadius,
                mCenterX, yLocation + tempAmplitude + mBallRadius,
                mTopColor,
                mBottomColor,
                Shader.TileMode.CLAMP);
        ComposeShader mComposeShader = new ComposeShader(mLinearGradient, mRadialGradient,
                PorterDuff.Mode.DST_ATOP);
        mPaint.setShader(mComposeShader);
        canvas.drawPath(mPath, mPaint);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;

        mRadialGradient = new RadialGradient(
                mCenterX,
                mCenterY,
                mCenterX - dp2px(32),
                mTopColor,
                Color.TRANSPARENT,
                Shader.TileMode.CLAMP);
    }

    //开始波浪 动画
    public void startWaveAnim() {
        if (!isWaveAnimRunning) {
            isWaveAnimRunning = true;
            mWaveAnimator.start();
        }
    }

//    private void chageWave() {
//        mFunctionChangeTag = !mFunctionChangeTag;
//        mPeakValueIndex1 = new Random().nextInt(WAVE_LINE_NUMBER / 3);
//        mPeakValueIndex2 = WAVE_LINE_NUMBER / 3 + new Random().nextInt(WAVE_LINE_NUMBER / 3);
//    }

    public void stopWaveAnim() {
        isWaveAnimRunning = false;
        if (mWaveAnimator != null) {
            mWaveAnimator.pause();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mWaveAnimator != null) {
            stopWaveAnim();
            mWaveAnimator.cancel();
            mWaveAnimator.end();
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == INVISIBLE || visibility == GONE) {
            stopWaveAnim();
        } else {
            startWaveAnim();
        }
    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
