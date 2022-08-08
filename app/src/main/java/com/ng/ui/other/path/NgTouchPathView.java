package com.ng.ui.other.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.Nullable;


/**
 * @author : jiangzhengnan.jzn@alibaba-inc.com
 * @creation : 2022/08/04
 * @description :
 * 纯绘制
 */
public class NgTouchPathView extends View {
    private int mTouchSlop;
    @Nullable
    private Path mPath;
    @Nullable
    private RectF mPathRectF;
    @Nullable
    private Paint mPaint;
    @Nullable
    private OnTouchPathCallBack mPathCallBack;

    private float mDownX = 0;
    private float mDownY = 0;
    private float mLastX = 0;
    private float mLastY = 0;
    private boolean isCanClick = false;
    private static final int PATH_START = 0;
    private static final int PATH_MOVE = 1;

    /**
     * 滑动阈值
     */
    private float mScrollThreshold;
    private boolean mScrollClicked;
    private float mScrollDistance;
    private float mStrokeWidth;
    private int mPaintColor;
    private boolean isSupportCanClick = false;

    public NgTouchPathView(final Context context) {
        super(context);
        init();
    }

    public NgTouchPathView(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mStrokeWidth = 50;
        mPaintColor = Color.RED;
        //todo cd控制
        mScrollThreshold = 200;
        // todo isSupportCanClick = cd控制
    }

    public void setPathCallBack(@Nullable final OnTouchPathCallBack pathCallBack) {
        mPathCallBack = pathCallBack;
    }

    public interface OnTouchPathCallBack {
        void onClick();

        void onPathClick();
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if (!isEnabled() || event == null) {
            return false;
        }
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                isCanClick = true;
                //防止触发滑动事件
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                addPath(touchX, touchY, PATH_START);
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(touchX - mDownX) > mTouchSlop || Math.abs(touchY - mDownY) > mTouchSlop) {
                    isCanClick = false;
                }
                addPath(touchX, touchY, PATH_MOVE);
                break;
            case MotionEvent.ACTION_UP:
                if (isSupportCanClick && isCanClick && mPathCallBack != null) {
                    mPathCallBack.onClick();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            default:
                return false;
        }
        mLastX = touchX;
        mLastY = touchY;
        return true;
    }

    private void addPath(final float touchX, final float touchY, final int action) {
        if (mPath == null) {
            mPath = new Path();
        }
        switch (action) {
            case PATH_START:
                mScrollDistance = 0;
                mPath.moveTo(touchX, touchY);
                break;
            case PATH_MOVE:
                mPath.lineTo(touchX, touchY);
                mScrollDistance += Math.sqrt(Math.pow(Math.abs(touchX - mLastX), 2) + Math.pow(Math.abs(touchY - mLastY), 2));
                break;
        }
        if (mPathRectF == null) {
            mPathRectF = new RectF(Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0);
        }
        mPathRectF.left = Math.min(mPathRectF.left, touchX);
        mPathRectF.right = Math.max(mPathRectF.right, touchX);
        mPathRectF.top = Math.min(mPathRectF.top, touchY);
        mPathRectF.bottom = Math.max(mPathRectF.bottom, touchY);
        invalidate((int) mPathRectF.left,
                   (int) mPathRectF.top,
                   (int) mPathRectF.right,
                   (int) mPathRectF.bottom);
        if (mScrollDistance > mScrollThreshold && !mScrollClicked && mPathCallBack != null) {
            mScrollClicked = true;
            mPathCallBack.onPathClick();
        }
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setAntiAlias(true);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setColor(mPaintColor);
            mPaint.setStrokeWidth(mStrokeWidth);
        }
        if (mPath != null) {
            canvas.drawPath(mPath, mPaint);
        }
    }
}
