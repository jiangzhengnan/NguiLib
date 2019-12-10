package com.ng.nguilib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ng.nguilib.utils.DensityUtil;
import com.ng.nguilib.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2019-12-10
 */
public class PointLoadingView extends View {
    private final int STATE_RUNNING = 0x1;
    private final int STATE_STOP = 0x2;
    private static int mAnimState = 0x2;

    private float mHeight;
    private float[] mPointHeight;
    private float mWidth;
    private float mPointSpace;

    //time
    private static final long TIME_ALL_INTERVAL = 300;
    private static final long TIME_POINT_INTERVAL = 200;
    private static final long TIME_DOWN_ANIM = 300;
    private static final long TIME_UP_ANIM = 600;

    //R of point
    private static int mPointDiameter;

    //default point size
    private static final int POINT_SIZE = 3;

    //tools
    private Paint mPointPaint;
    private Paint mBgPaint;
    private AnimatorSet mDownAnimSet;
    private AnimatorSet mUpAnimSet;

    //color
    private static final int DEFAULT_BG_COLOR = Color.WHITE;
    private static final int DEFAULT_POINT_COLOR = Color.BLACK;

    public void setColor(int bgColor, int pointColor) {
        mBgPaint.setColor(bgColor);
        mPointPaint.setColor(pointColor);

    }

    public PointLoadingView(Context context) {
        super(context);
        init(context);
    }

    public PointLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        mAnimState = STATE_STOP;
        mPointDiameter = DensityUtil.INSTANCE.dip2px(context, 6f);
        mPointHeight = new float[POINT_SIZE];
        initPaint();
    }

    private void initPaint() {
        mPointPaint = new Paint();
        mPointPaint.setColor(DEFAULT_POINT_COLOR);
        mPointPaint.setStrokeWidth(mPointDiameter);
        mPointPaint.setAntiAlias(true);
        mPointPaint.setStrokeCap(Paint.Cap.ROUND);
        mPointPaint.setStyle(Paint.Style.FILL);
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setColor(DEFAULT_BG_COLOR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        drawPoints(canvas);
    }

    private void drawBg(Canvas canvas) {
        RectF bgRectF = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(bgRectF, mHeight / 2, mHeight / 2, mBgPaint);
    }

    private void drawPoints(Canvas canvas) {
        for (int i = 0; i < POINT_SIZE; i++) {
            canvas.drawPoint((i + 1) * mPointSpace, mPointHeight[i], mPointPaint);
        }
    }


    private void initAnim() {
        //down
        if (mDownAnimSet == null) {
            mDownAnimSet = new AnimatorSet();
            List<Animator> mDownAnimList = new ArrayList<>();
            for (int i = 0; i < POINT_SIZE; i++) {
                ValueAnimator tempDownAmt = ValueAnimator.ofFloat(0f, 100f);
                tempDownAmt.setDuration(TIME_DOWN_ANIM);
                tempDownAmt.setInterpolator(new AccelerateInterpolator());
                tempDownAmt.setStartDelay(i * TIME_POINT_INTERVAL);
                final int finalI = i;
                tempDownAmt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float temp = animation.getAnimatedFraction();
                        mPointHeight[finalI] = mHeight / 2 + (mHeight / 2 + mPointDiameter) * temp;
                        postInvalidate();
                    }
                });
                mDownAnimList.add(tempDownAmt);
            }
            mDownAnimSet.playTogether(mDownAnimList);
            mDownAnimSet.setStartDelay(TIME_ALL_INTERVAL);
            mDownAnimSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (mAnimState != STATE_STOP)
                        mUpAnimSet.start();
                }
            });
        }

        //up
        if (mUpAnimSet == null) {
            mUpAnimSet = new AnimatorSet();
            List<Animator> mUpAnimList = new ArrayList<>();
            for (int i = 0; i < POINT_SIZE; i++) {
                ValueAnimator tempUpAmt = ValueAnimator.ofFloat(0f, 100f);
                tempUpAmt.setDuration(TIME_UP_ANIM);
                tempUpAmt.setInterpolator(new DecelerateInterpolator());
                tempUpAmt.setStartDelay(i * TIME_POINT_INTERVAL);
                final int finalI = i;
                tempUpAmt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float temp = animation.getAnimatedFraction();
                        mPointHeight[finalI] = mHeight + mPointDiameter - (mHeight / 2 + mPointDiameter) * temp;
                        postInvalidate();
                    }
                });
                mUpAnimList.add(tempUpAmt);
            }
            mUpAnimSet.playTogether(mUpAnimList);
            mUpAnimSet.setStartDelay(TIME_ALL_INTERVAL);
            mUpAnimSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (mAnimState != STATE_STOP)
                        mDownAnimSet.start();
                }
            });
        }
    }


    public void startLoadingAnim() {
        if (mDownAnimSet != null && mAnimState == STATE_STOP) {
            mAnimState = STATE_RUNNING;
            mDownAnimSet.start();
        }
    }

    public void stopLoadingAnim() {
        mAnimState = STATE_STOP;
        if (mDownAnimSet != null) {
            mDownAnimSet.cancel();
            mDownAnimSet.end();
        }
        if (mUpAnimSet != null) {
            mUpAnimSet.cancel();
            mUpAnimSet.end();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopLoadingAnim();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        logd("visibility: " + visibility);
        if (visibility == VISIBLE) {
            startLoadingAnim();
        } else {
            stopLoadingAnim();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        mPointSpace = mWidth / (POINT_SIZE + 1);
        for (int i = 0; i < POINT_SIZE; i++) {
            mPointHeight[i] = mHeight / 2;
        }
        initAnim();
        logd(mHeight + "   " + mWidth + "   " + mPointSpace);
    }

    public void logd(String txt) {
        Log.d("nangua", txt);
    }
}
