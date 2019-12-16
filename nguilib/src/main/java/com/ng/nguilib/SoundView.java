package com.ng.nguilib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ng.nguilib.utils.LogUtils;

import java.util.Random;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2019-12-14
 */
public class SoundView extends View {
    private Paint mPaint;
    private float mHeight;
    private float mWidth;
    private Path mPath;


    public SoundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(dp2px(2));
        mPath = new Path();
    }

    public SoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //音量
    private float volume = 0;
    private float maxVolume = 1;


    public void setVolume(int volume) {
        if (volume != 0) {
            this.maxVolume = volume;


            postInvalidate();
        }
    }

    //总偏移量
    private int allOffsetX = 0;
    //每一条直线的偏移量
    private int eachOffsetX = dp2px(1);

    //垂直方向间距
    private float spaceY = dp2px(2);
    //直线条数
    private int waveNumber = 24;

    // 振幅
    private int amplitude = dp2px(10);
    // 波长
    private int width;
    //间距
    private int spaceX = dp2px(3);
    //小正方形边长/2
    private float halfSide = dp2px(1);

    //上颜色
    private int topColor = Color.parseColor("#00BFFF");
    private int bottomColor = Color.parseColor("#2126D4");


    //三个峰值
    int peakValueIndex1 = 1;
    int peakValueIndex2 = 12;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // LogUtils.INSTANCE.d("kashi~~~~~~~~~: " + "  all:" + amplitude);

        for (int i = 1; i <= waveNumber; i++) {

            eachOffsetX = dp2px(10) + dp2px(i);

            int tempAmplitude = 0;

            //峰值中间最大两边递减
            int chaju;
            int disTop;
            int disBot;
            if (i < peakValueIndex1) {
                chaju = Math.abs(i - peakValueIndex1);
            } else if (peakValueIndex1 < i && i < peakValueIndex2) {
                disTop = i - peakValueIndex1;
                disBot = peakValueIndex2 - i;
                chaju = Math.min(disTop, disBot);
            } else {
                chaju = 0;
            }

            chaju = chaju;


            tempAmplitude = (waveNumber - chaju) * amplitude / waveNumber;


            tempAmplitude += amplitude / 5;

            //间距要越来越大
            spaceX = dp2px(2) + i / 5;

            //小球变大
            halfSide = 1 + ((float) i / (float) waveNumber) * 1f;
            //LogUtils.INSTANCE.d(" side: " + halfSide);

            //透明度
            int alpha = 50 + 205 * i / waveNumber;
            mPaint.setAlpha(alpha);

            drawVoiceLine(canvas,
                    allOffsetX + eachOffsetX * (waveNumber - i),
                    (int) (mWidth / 3 + i * spaceY),
                    tempAmplitude);
        }

    }

    /**
     * y=Asin(ωx+φ)+k
     *
     * @param canvas
     * @param xOffset   x轴偏移量
     * @param yLocation y轴坐标
     */
    private void drawVoiceLine(Canvas canvas, int xOffset, int yLocation, int tempAmplitude) {
        int index = 0;
        mPath.reset();
        mPath.moveTo(0, 0);

        LogUtils.INSTANCE.d("xOffset: " + xOffset);

        while (index <= (int) mWidth) {

            //这边必须保证起始点和终点的时候amplitude = 0;
            //amplitude 振幅  volume 0 - 10
            float singleTempAmp = ((mWidth - Math.abs((float) index - mWidth / 2)) / mWidth) * tempAmplitude;
            //LogUtils.INSTANCE.d("zhenfu: " + singleTempAmp);

            int endY = 0;
            if (chage) {
                endY = (int) (Math.sin(((float) index + (float) xOffset) / mWidth * 2f * Math.PI + 0)
                        * singleTempAmp + yLocation);
            } else {
                endY = (int) (Math.cos(((float) index + (float) xOffset) / mWidth * 2f * Math.PI + 0)
                        * singleTempAmp + yLocation);
            }

            mPath.moveTo(index, endY);


            mPath.addCircle(index, endY, halfSide, Path.Direction.CCW);

            //透明度从中间向两边递减

            index += spaceX;

        }


        LinearGradient linearGradient = new LinearGradient(
                centerX, yLocation - tempAmplitude - halfSide,
                centerX, yLocation + tempAmplitude + halfSide,
                topColor,
                bottomColor,
                Shader.TileMode.CLAMP);

        RadialGradient radialGradient = new RadialGradient(
                centerX,
                centerY,
                centerY,
                topColor,
                Color.TRANSPARENT,
                Shader.TileMode.CLAMP);

        ComposeShader composeShader = new ComposeShader(radialGradient, linearGradient,
                PorterDuff.Mode.SRC_ATOP);

        mPaint.setShader(radialGradient);
        canvas.drawPath(mPath, mPaint);

    }


    private float centerX;
    private float centerY;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        centerX = mWidth / 2;
        centerY = mHeight / 2;
        width = (int) mWidth;
        //waveNumber = (int) ((mWidth/3 - amplitude) / spaceY);
        LogUtils.INSTANCE.d("几条: " + waveNumber + " amplitude： " + amplitude);
    }

    private boolean isAnimRunning = false;

    ValueAnimator mAnimator;

    boolean chage = true;


    //test 动画
    public void startAnim() {
        if (!isAnimRunning) {
            LogUtils.INSTANCE.d("startSingleAnim");
            isAnimRunning = true;
            mAnimator = ValueAnimator.ofFloat
                    (0f, 20f, 30f,20f,30f,40f,30f,50f,40f,70f,60f,90f,100f,0f);
            mAnimator.setDuration(30 * 1000);
            mAnimator.setRepeatCount(-1);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    volume = (float) animation.getAnimatedValue() * maxVolume / 10000f;

                    allOffsetX += dp2px(5);
                    if (allOffsetX >= Integer.MAX_VALUE - 10000) {
                        allOffsetX = 0;
                    }

                    amplitude = (int) (mHeight / 6 * volume);

                    //LogUtils.INSTANCE.d("volue: " + volume + "   max: " + maxVolume + " am:" + amplitude);


                    postInvalidate();
                }
            });
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationRepeat(Animator animation) {
                    super.onAnimationRepeat(animation);
                    chage = !chage;
                    peakValueIndex1 = new Random().nextInt(waveNumber / 2);
                    peakValueIndex2 = waveNumber / 2 + new Random().nextInt(waveNumber / 2);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    isAnimRunning = false;
                }
            });
            mAnimator.start();
        }
    }


    public void stopAnim() {
        isAnimRunning = false;
        if (mAnimator != null) {
            mAnimator.pause();
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator.end();
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            // startAnim();
        } else {
            stopAnim();
        }

    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
