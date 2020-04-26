package com.ng.ui.study.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import com.ng.nguilib.utils.LogUtils;

import java.util.List;

/**
 * 描述:json动画解析类
 * from：MyAnimStudyActivity
 * LineMorphingDrawable
 *
 * @author Jzn
 * @date 2020-04-17
 */
public class PieAnimView extends View implements Animatable {
    private List<PieAnimShape> mPieAnimShapes;

    private Path mPath;
    private Paint mPaint;
    private RectF mDrawBound;

    private int mNowShape;
    private int mNextShape;

    //anim
    private long mStartTime;
    private float mAnimProgress;
    private int mAnimDuration = 3000;


    public void setAnimDate(List<PieAnimShape> mPieAnimShapes) {
        this.mPieAnimShapes = mPieAnimShapes;
    }


    public PieAnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);

        mDrawBound = new RectF();
    }


    public void showShape(int i) {
        if (mPieAnimShapes == null || mPieAnimShapes.size() == 0) {
            return;
        }
        mNowShape = i;
        showShape(mPieAnimShapes.get(i));
    }


    private void showShape(PieAnimShape pieAnimShape) {
        mPath.reset();
        List<String> points = pieAnimShape.getPoints();

        float tempX = 0;
        float tempY = 0;
        for (int i = 0; i < points.size(); i++) {
            String pointStr = points.get(i);
            float x = getX(Float.parseFloat(pointStr.split(",")[0]));
            float y = getY(Float.parseFloat(pointStr.split(",")[1]));

            if (tempX == 0 && tempY == 0) {
                LogUtils.INSTANCE.d("moveTo " + x + " " + y);
                mPath.moveTo(x, y);
                tempX = x;
                tempY = y;
            } else {
                LogUtils.INSTANCE.d("lineTo " + x + " " + y);
                mPath.lineTo(x, y);
                tempX = 0;
                tempY = 0;
            }

        }
        invalidate();
    }

    private float getX(float value) {
        return mDrawBound.width() * value;
    }

    private float getY(float value) {
        return mDrawBound.height() * (1 - value);//改为正常zhen直角坐标系
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mDrawBound = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    public void changeShape() {
        if (mPieAnimShapes == null || mPieAnimShapes.size() <= 1) {
            return;
        }

        mNextShape = mNowShape + 1;
        if (mNextShape > mPieAnimShapes.size() - 1) {
            mNextShape = 0;
        }
        start();
    }

    private ValueAnimator mAnimator;
    private boolean isAnimRunning = false;
    private float mThickness = 0f;

    @Override
    public void start() {

        if (isAnimRunning) {
            return;
        }
        resetAnimation();

        isAnimRunning = true;
        LogUtils.INSTANCE.d("startAnim: " + mNowShape + " -> " + mNextShape);

        mAnimator = ValueAnimator.ofFloat(0, 1f);
        mAnimator.setDuration(mAnimDuration);
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mThickness = (float) animation.getAnimatedValue();
                update(mPieAnimShapes.get(mNowShape), mPieAnimShapes.get(mNextShape));
                postInvalidate();
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimRunning = false;
                mThickness = 0f;
            }
        });
        mAnimator.start();

    }

    //updatePathBetweenStates
    private void update(PieAnimShape nowShape, PieAnimShape nextShape) {
        LogUtils.INSTANCE.d(mThickness + " update now:" + nowShape.toString());
        LogUtils.INSTANCE.d(mThickness + " update next:" + nextShape.toString());

        mPath.reset();


        List<Float> nowPoints = nowShape.getRealPoints();
        List<Float> nextPoints = nextShape.getRealPoints();

        int count = Math.max(nowPoints.size(), nextPoints.size()) / 4;


        for (int i = 0; i < count; i++) {
            int index = i * 4;
            float x1;
            float y1;
            float x2;
            float y2;
            if (index >= nowPoints.size()) {
                x1 = 0.5f;
                y1 = 0.5f;
                x2 = 0.5f;
                y2 = 0.5f;
            } else {
                x1 = nowPoints.get(index);
                y1 = nowPoints.get(index + 1);
                x2 = nowPoints.get(index + 2);
                y2 = nowPoints.get(index + 3);
            }

            float x3;
            float y3;
            float x4;
            float y4;
            if (index >= nextPoints.size()) {
                x3 = 0.5f;
                y3 = 0.5f;
                x4 = 0.5f;
                y4 = 0.5f;
            } else {
                x3 = nextPoints.get(index);
                y3 = nextPoints.get(index + 1);
                x4 = nextPoints.get(index + 2);
                y4 = nextPoints.get(index + 3);
            }

            mPath.moveTo(getX(x1 + (x3 - x1) * mThickness), getY(y1 + (y3 - y1) * mThickness));
            mPath.lineTo(getX(x2 + (x4 - x2) * mThickness), getY(y2 + (y4 - y2) * mThickness));

        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);

    }


    @Override
    public void stop() {
        if (!isRunning())
            return;
        isAnimRunning = false;
        mAnimator.cancel();
        invalidate();
    }

    private void resetAnimation() {
        mStartTime = SystemClock.uptimeMillis();
        mAnimProgress = 0f;
    }

    @Override
    public boolean isRunning() {
        return isAnimRunning;
    }
}