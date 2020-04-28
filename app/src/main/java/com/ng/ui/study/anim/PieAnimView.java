package com.ng.ui.study.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import androidx.annotation.Nullable;

import com.ng.nguilib.utils.LogUtils;
import com.ng.ui.R;

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

    //anim
    private int mAnimDuration;

    //属性

    private int mPaddingLeft;
    private int mPaddingTop;
    private int mPaddingRight;
    private int mPaddingBottom;

    private Interpolator mInterpolator;

    private int mStrokeSize;
    private int mStrokeColor;
    private Paint.Cap mStrokeCap;
    private Paint.Join mStrokeJoin;

    private boolean mRotateDirection;
    private boolean mIsRotate;

    private boolean mAlphaSwitch;
    private int mAlphaNum;


    //running
    private Path mPath;
    private Paint mPaint;
    private RectF mDrawBound;

    //index
    private int mNowShape;
    private int mNextShape;

    public void setPadding(int padding) {
        if (padding == 0) {
            return;
        }
        mPaddingLeft = padding;
        mPaddingTop = padding;
        mPaddingRight = padding;
        mPaddingBottom = padding;
    }

    public int getmPaddingLeft() {
        return mPaddingLeft;
    }

    public void setmPaddingLeft(int mPaddingLeft) {
        this.mPaddingLeft = mPaddingLeft;
    }

    public int getmPaddingTop() {
        return mPaddingTop;
    }

    public void setmPaddingTop(int mPaddingTop) {
        this.mPaddingTop = mPaddingTop;
    }

    public int getmPaddingRight() {
        return mPaddingRight;
    }

    public void setmPaddingRight(int mPaddingRight) {
        this.mPaddingRight = mPaddingRight;
    }

    public int getmPaddingBottom() {
        return mPaddingBottom;
    }

    public void setmPaddingBottom(int mPaddingBottom) {
        this.mPaddingBottom = mPaddingBottom;
    }

    public Interpolator getmInterpolator() {
        return mInterpolator;
    }

    public void setmInterpolator(Interpolator mInterpolator) {
        this.mInterpolator = mInterpolator;
    }

    public int getmStrokeSize() {
        return mStrokeSize;
    }

    public void setmStrokeSize(int mStrokeSize) {
        this.mStrokeSize = mStrokeSize;
    }

    public int getmStrokeColor() {
        return mStrokeColor;
    }

    public void setmStrokeColor(int mStrokeColor) {
        this.mStrokeColor = mStrokeColor;
    }

    public Paint.Cap getmStrokeCap() {
        return mStrokeCap;
    }

    public void setmStrokeCap(Paint.Cap mStrokeCap) {
        this.mStrokeCap = mStrokeCap;
    }

    public Paint.Join getmStrokeJoin() {
        return mStrokeJoin;
    }

    public void setmStrokeJoin(Paint.Join mStrokeJoin) {
        this.mStrokeJoin = mStrokeJoin;
    }

    public boolean ismRotateDirection() {
        return mRotateDirection;
    }

    public void setmRotateDirection(boolean mRotateDirection) {
        this.mRotateDirection = mRotateDirection;
    }

    public boolean ismIsRotate() {
        return mIsRotate;
    }

    public void setmIsRotate(boolean mIsRotate) {
        this.mIsRotate = mIsRotate;
    }

    public void setAnimDate(List<PieAnimShape> mPieAnimShapes) {
        this.mPieAnimShapes = mPieAnimShapes;
    }

    @SuppressLint("CustomViewStyleable")
    public PieAnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PieAnimStyle);
        int resId;

        mPaddingLeft = ta.getDimensionPixelSize(R.styleable.PieAnimStyle_pie_paddingLeft, 0);
        mPaddingTop = ta.getDimensionPixelSize(R.styleable.PieAnimStyle_pie_paddingTop, 0);
        mPaddingRight = ta.getDimensionPixelSize(R.styleable.PieAnimStyle_pie_paddingRight, 0);
        mPaddingBottom = ta.getDimensionPixelSize(R.styleable.PieAnimStyle_pie_paddingBottom, 0);
        setPadding(ta.getDimensionPixelSize(R.styleable.PieAnimStyle_pie_padding, 0));

        mAnimDuration = ta.getInteger(R.styleable.PieAnimStyle_pie_animDuration, 1000);

        if ((resId = ta.getResourceId(R.styleable.PieAnimStyle_pie_interpolator, 0)) != 0)
            mInterpolator = AnimationUtils.loadInterpolator(context, resId);

        mStrokeSize = ta.getDimensionPixelSize(R.styleable.PieAnimStyle_pie_strokeSize, dp2px(3));
        mStrokeColor = ta.getColor(R.styleable.PieAnimStyle_pie_strokeColor, 0x000000);

        int cap = ta.getInteger(R.styleable.PieAnimStyle_pie_strokeCap, 0);
        switch (cap) {
            case 0:
                mStrokeCap = Paint.Cap.BUTT;
                break;
            case 1:
                mStrokeCap = Paint.Cap.ROUND;
                break;
            case 2:
                mStrokeCap = Paint.Cap.SQUARE;
                break;
        }

        int join = ta.getInteger(R.styleable.PieAnimStyle_pie_strokeJoin, 0);
        switch (join) {
            case 0:
                mStrokeJoin = Paint.Join.MITER;
                break;
            case 1:
                mStrokeJoin = Paint.Join.ROUND;
                break;
            case 2:
                mStrokeJoin = Paint.Join.BEVEL;
                break;
        }

        mIsRotate = ta.getBoolean(R.styleable.PieAnimStyle_pie_rotateSwitch, false);
        mRotateDirection = ta.getBoolean(R.styleable.PieAnimStyle_pie_rotateDirection, true);

        mAlphaSwitch = ta.getBoolean(R.styleable.PieAnimStyle_pie_alphaSwitch, false);
        mAlphaNum = ta.getInteger(R.styleable.PieAnimStyle_pie_alphaNum, 0);

        ta.recycle();
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        mPaint.setStrokeWidth(mStrokeSize);
        mPaint.setStyle(Paint.Style.STROKE);

        mPaint.setColor(mStrokeColor);
        mPaint.setStrokeCap(mStrokeCap);
        mPaint.setStrokeJoin(mStrokeJoin);

    }


    public void showShape() {
        if (mPieAnimShapes == null || mPieAnimShapes.size() == 0) {
            return;
        }
        mNowShape = 0;
        showShape(mPieAnimShapes.get(mNowShape));
    }


    private void showShape(PieAnimShape pieAnimShape) {
        mPath.reset();
        List<String> points = pieAnimShape.getPoints();

        float tempX = -1;
        float tempY = -1;
        for (int i = 0; i < points.size(); i++) {
            String pointStr = points.get(i);
            float x = getX(Float.parseFloat(pointStr.split(",")[0]));
            float y = getY(Float.parseFloat(pointStr.split(",")[1]));

            if (tempX == -1 && tempY == -1) {
                //LogUtils.INSTANCE.d("moveTo " + x + " " + y);
                mPath.moveTo(x, y);
                tempX = x;
                tempY = y;
            } else {
                //LogUtils.INSTANCE.d("lineTo " + x + " " + y);
                mPath.lineTo(x, y);
                tempX = -1;
                tempY = -1;
            }

        }
        invalidate();
    }

    private float getX(float value) {
        return mDrawBound.left + mDrawBound.width() * value;
    }

    private float getY(float value) {
        //改为正常zhen直角坐标系
        return mDrawBound.top + mDrawBound.height() * value;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mDrawBound = new RectF(mPaddingLeft, mPaddingTop, getMeasuredWidth() - mPaddingRight, getMeasuredHeight() - mPaddingBottom);
    }

    public void changeShape() {
        if (mPieAnimShapes == null || mPieAnimShapes.size() <= 1) {
            return;
        }

        mNextShape = mNowShape + 1;
        if (mNextShape > mPieAnimShapes.size() - 1) {
            mNextShape = 0;
        }

        LogUtils.INSTANCE.d("startAnim: " + mNowShape + " -> " + mNextShape);

        start();
    }

    //anim
    private ValueAnimator mAnimator;
    private boolean isAnimRunning = false;
    private float mThickness = 0f;

    private float mDegree = 0f;


    @Override
    public void start() {

        if (isAnimRunning) {
            return;
        }

        isAnimRunning = true;

        mAnimator = ValueAnimator.ofFloat(0, 1f);
        mAnimator.setDuration(mAnimDuration);
        if (mInterpolator != null)
            mAnimator.setInterpolator(mInterpolator);
        mAnimator.addUpdateListener(animation -> {
            mThickness = (float) animation.getAnimatedValue();
            update(mPieAnimShapes.get(mNowShape), mPieAnimShapes.get(mNextShape));
            postInvalidate();
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimRunning = false;
                mThickness = 0f;
                mDegree += (mRotateDirection ? 180 : -180);
                mNowShape++;
                if (mNowShape > mPieAnimShapes.size() - 1) {
                    mNowShape = 0;
                }

            }
        });
        mAnimator.start();

    }

    //updatePathBetweenStates
    private void update(PieAnimShape nowShape, PieAnimShape nextShape) {

        mPath.reset();

        List<Float> nowPoints = nowShape.getRealPoints();
        List<Float> nextPoints = nextShape.getRealPoints();
        int count = Math.max(nowPoints.size(), nextPoints.size()) / 4;
        for (int i = 0; i < count; i++) {
            int index = i * 4;
            float x1, y1, x2, y2;
            if (index >= nowPoints.size()) {
                x1 = y1 = x2 = y2 = 0.5f;
            } else {
                x1 = nowPoints.get(index);
                y1 = nowPoints.get(index + 1);
                x2 = nowPoints.get(index + 2);
                y2 = nowPoints.get(index + 3);
            }
            float x3, y3, x4, y4;
            if (index >= nextPoints.size()) {
                x3 = y3 = x4 = y4 = 0.5f;
            } else {
                x3 = nextPoints.get(index);
                y3 = nextPoints.get(index + 1);
                x4 = nextPoints.get(index + 2);
                y4 = nextPoints.get(index + 3);
            }
            float moveX = getX(x1 + (x3 - x1) * mThickness);
            float moveY = getY(y1 + (y3 - y1) * mThickness);
            mPath.moveTo(moveX, moveY);

            float lineX = getX(x2 + (x4 - x2) * mThickness);
            float lineY = getY(y2 + (y4 - y2) * mThickness);

            if (!(moveX == lineX && lineX == lineY && mThickness == 1f)) {
                mPath.lineTo(lineX, lineY);
            }
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int restoreCount = canvas.save();
        if (mIsRotate) {
            float degrees = mDegree + (mRotateDirection ? 180 : -180) * (mThickness);
            canvas.rotate(degrees, mDrawBound.centerX(), mDrawBound.centerY());
        }
        if (mAlphaSwitch) {
            LogUtils.INSTANCE.d("thickness:" + mThickness);
            int alpha = (int) (Math.abs(0.5 - mThickness) / 0.5 * (255 - mAlphaNum)) + mAlphaNum;
            LogUtils.INSTANCE.d("alpha:" + alpha + " " + mThickness);
            mPaint.setAlpha(alpha);

        }

        canvas.drawPath(mPath, mPaint);
        canvas.restoreToCount(restoreCount);


    }


    @Override
    public void stop() {
        if (!isRunning())
            return;
        isAnimRunning = false;
        mAnimator.cancel();
        invalidate();
    }


    @Override
    public boolean isRunning() {
        return isAnimRunning;
    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}