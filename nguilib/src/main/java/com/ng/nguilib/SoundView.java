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
import android.view.animation.LinearInterpolator;

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


    //总偏移量
    private int allOffsetX = 0;
    //每一条直线的偏移量 todo
    private int eachOffsetX = dp2px(50);

    //垂直方向间距
    private float spaceY = dp2px(4);
    //直线条数
    private int waveNumber = 15;

    // 振幅 todo
    private int amplitude = dp2px(10);
    // 波长
    private int width;
    //间距
    private int spaceX = dp2px(4);
    //小正方形边长/2
    private float halfSide = dp2px(1);

    //上颜色
    private int topColor = Color.parseColor("#B1B1DB");
    private int bottomColor = Color.parseColor("#2126D4");

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 1; i <= waveNumber; i++) {
            Random random = new Random();

            spaceX = dp2px(i * 2);  //间距要越来越大
            amplitude = dp2px(i);
            eachOffsetX = (int) (i * centerX / 2) + dp2px(i);
            //width = (int) (mWidth * (i + 3) / (i + 2));  //会抖动

            drawVoiceLine(canvas,
                    allOffsetX + eachOffsetX,
                    (int) (mWidth / 3 - amplitude / 2 + i * spaceY));
        }

    }

    /**
     * y=Asin(ωx+φ)+k
     *
     * @param canvas
     * @param xOffset   x轴偏移量
     * @param yLocation y轴坐标
     */
    private void drawVoiceLine(Canvas canvas, int xOffset, int yLocation) {
        int index = 0;
        mPath.reset();
        mPath.moveTo(0, 0);

        while (index <= width) {
            float endY = (float) (Math.sin(((float) index + (float) xOffset) / (float) width * 4f * Math.PI + 0)
                    * (float) amplitude + yLocation);
            mPath.moveTo(index, endY);


            //mPath.addCircle(index,endY,dp2px(2), Path.Direction.CCW);
            RectF tempRf = new RectF(index - halfSide, endY - halfSide,
                    index + halfSide, endY + halfSide);
            mPath.addRect(tempRf, Path.Direction.CCW);
            index += spaceX;
        }

        LinearGradient linearGradient = new LinearGradient(
                centerX, yLocation - amplitude - halfSide,
                centerX, yLocation + amplitude + halfSide,
                topColor,
                bottomColor,
                Shader.TileMode.CLAMP);
        RadialGradient radialGradient = new RadialGradient(
                centerX,
                yLocation,
                centerX,
                Color.WHITE,
                Color.TRANSPARENT,
                Shader.TileMode.CLAMP);
        ComposeShader composeShader = new ComposeShader(radialGradient, linearGradient,
                PorterDuff.Mode.SRC_ATOP);
        mPaint.setShader(composeShader);
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

    public void startAnim() {
        if (!isAnimRunning) {
            LogUtils.INSTANCE.d("startSingleAnim");
            isAnimRunning = true;
            mAnimator = ValueAnimator.ofInt(0, (int) mWidth);
            mAnimator.setDuration(3 * 1000);
            mAnimator.setRepeatCount(-1);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    allOffsetX = (int) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            mAnimator.addListener(new AnimatorListenerAdapter() {
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
