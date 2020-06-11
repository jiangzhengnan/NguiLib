package com.ng.ui.study.clock;

import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-06-05
 */
public class ClockTestView2 extends View {
    private Paint mPaint = new Paint();

    private Paint mSecoundPaint = new Paint();

    private int mWidth, mHeight, mRadius;
    private float mStrokeWidth = 20f;
    private int x, y;

    //三维
    /* camera绕X轴旋转的角度 */
    private float mCameraRotateX;
    /* camera绕Y轴旋转的角度 */
    private float mCameraRotateY;

    public ClockTestView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);


        mSecoundPaint.setStrokeWidth(mStrokeWidth);
        mSecoundPaint.setAntiAlias(true);
        mSecoundPaint.setColor(Color.BLUE);
        mSecoundPaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        x = mWidth / 2;
        y = mHeight / 2;
        mRadius = mWidth > mHeight ? mHeight : mWidth;
        mRadius = mRadius/2;
        mRadius -= mStrokeWidth;


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCircle(canvas);

        invalidate();
    }


    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(x, y, mRadius, mPaint);

        canvas.drawCircle(x, y, mRadius/2, mPaint);

        canvas.drawCircle(x, y, mRadius/2, mSecoundPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

        }
        return super.onTouchEvent(event);
    }
}
