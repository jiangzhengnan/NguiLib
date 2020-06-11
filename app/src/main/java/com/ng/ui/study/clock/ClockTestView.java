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
public class ClockTestView extends View {
    private Paint mPaint = new Paint();

    private Paint mSecoundPaint = new Paint();

    private int mWidth, mHeight, mRadius;
    private float mStrokeWidth = 20f;
    private int x, y;

    //anim
    /* 触摸时作用在Camera的矩阵 */
    private Matrix mCameraMatrix;
    /* 照相机，用于旋转时钟实现3D效果 */
    private Camera mCamera;
    /* camera绕X轴旋转的角度 */
    private float mCameraRotateX;
    /* camera绕Y轴旋转的角度 */
    private float mCameraRotateY;
    /* camera旋转的最大角度 */
    private float mMaxCameraRotate = 10;
    /* 指针的在x轴的位移 */
    private float mCanvasTranslateX;
    /* 指针的在y轴的位移 */
    private float mCanvasTranslateY;
    /* 指针的最大位移 */
    private float mMaxCanvasTranslate;
    /* 手指松开时时钟晃动的动画 */
    private ValueAnimator mShakeAnim;

    public ClockTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);


        mSecoundPaint.setAntiAlias(true);
        mSecoundPaint.setColor(Color.BLUE);
        mSecoundPaint.setStyle(Paint.Style.STROKE);


        mCameraMatrix = new Matrix();
        mCamera = new Camera();
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

        mMaxCanvasTranslate = 0.02f * mRadius;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setCameraRotate(canvas);
        drawCircle(canvas);

        invalidate();
    }

    private void drawCircle(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, mRadius, mPaint);

        mPaint.setStrokeWidth(mStrokeWidth/2);
        canvas.drawCircle(x, y, mRadius/2, mPaint);

        canvas.translate(mCanvasTranslateX * 1.2f, mCanvasTranslateY * 1.2f);
        canvas.drawCircle(x, y, mRadius/4, mPaint);

        mSecoundPaint.setStrokeWidth(mStrokeWidth/2);
        canvas.translate(mCanvasTranslateX * 2f, mCanvasTranslateY * 2f);
        canvas.drawCircle(x, y, mRadius/4, mSecoundPaint);
    }

    /**
     * 设置3D时钟效果，触摸矩阵的相关设置、照相机的旋转大小
     * 应用在绘制图形之前，否则无效
     */
    private void setCameraRotate(Canvas canvas) {
        mCameraMatrix.reset();
        mCamera.save();

        mCamera.rotateX(mCameraRotateX);//绕x轴旋转角度
        mCamera.rotateY(mCameraRotateY);//绕y轴旋转角度

        mCamera.getMatrix(mCameraMatrix);//相关属性设置到matrix中

        mCamera.restore();
        //camera在view左上角那个点，故旋转默认是以左上角为中心旋转
        //故在动作之前pre将matrix向左移动getWidth()/2长度，向上移动getHeight()/2长度
        mCameraMatrix.preTranslate(-getWidth() / 2, -getHeight() / 2);
        //在动作之后post再回到原位
        mCameraMatrix.postTranslate(getWidth() / 2, getHeight() / 2);
        canvas.concat(mCameraMatrix);//matrix与canvas相关联
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mShakeAnim != null && mShakeAnim.isRunning()) {
                    mShakeAnim.cancel();
                }
                getCameraRotate(event);
                getCanvasTranslate(event);
                break;
            case MotionEvent.ACTION_MOVE:
                //根据手指坐标计算camera应该旋转的大小
                getCameraRotate(event);
                getCanvasTranslate(event);
                break;
            case MotionEvent.ACTION_UP:
                //松开手指，时钟复原并伴随晃动动画
                startShakeAnim();
                break;
        }
        return true;
    }

    /**
     * 获取camera旋转的大小
     *
     * @param event motionEvent
     */
    private void getCameraRotate(MotionEvent event) {
        float rotateX = -(event.getY() - getHeight() / 2);  //中分线上，是正的，中分线下，是负的
        float rotateY = (event.getX() - getWidth() / 2);    //???
        //求出此时旋转的大小与半径之比
        float[] percentArr = getPercent(rotateX, rotateY);
        //最终旋转的大小按比例匀称改变
        mCameraRotateX = percentArr[0] * mMaxCameraRotate;
        mCameraRotateY = percentArr[1] * mMaxCameraRotate;
    }


    /**
     * 当拨动时钟时，会发现时针、分针、秒针和刻度盘会有一个较小的偏移量，形成近大远小的立体偏移效果
     * 一开始我打算使用 matrix 和 camera 的 mCamera.translate(x, y, z) 方法改变 z 的值
     * 但是并没有效果，所以就动态计算距离，然后在 onDraw()中分零件地 mCanvas.translate(x, y)
     *
     * @param event motionEvent
     */
    private void getCanvasTranslate(MotionEvent event) {
        float translateX = (event.getX() - getWidth() / 2);
        float translateY = (event.getY() - getHeight() / 2);
        //求出此时位移的大小与半径之比
        float[] percentArr = getPercent(translateX, translateY);
        //最终位移的大小按比例匀称改变
        mCanvasTranslateX = percentArr[0] * mMaxCanvasTranslate;
        mCanvasTranslateY = percentArr[1] * mMaxCanvasTranslate;
    }

    /**
     * 获取一个操作旋转或位移大小的比例
     *
     * @param x x大小
     * @param y y大小
     * @return 装有xy比例的float数组
     */
    private float[] getPercent(float x, float y) {
        float[] percentArr = new float[2];
        float percentX = x / mRadius;
        float percentY = y / mRadius;
        if (percentX > 1) {
            percentX = 1;
        } else if (percentX < -1) {
            percentX = -1;
        }
        if (percentY > 1) {
            percentY = 1;
        } else if (percentY < -1) {
            percentY = -1;
        }
        percentArr[0] = percentX;
        percentArr[1] = percentY;
        return percentArr;
    }


    /**
     * 时钟晃动动画
     */
    private void startShakeAnim() {
        final String cameraRotateXName = "cameraRotateX";
        final String cameraRotateYName = "cameraRotateY";
        final String canvasTranslateXName = "canvasTranslateX";
        final String canvasTranslateYName = "canvasTranslateY";
        PropertyValuesHolder cameraRotateXHolder =
                PropertyValuesHolder.ofFloat(cameraRotateXName, mCameraRotateX, 0);
        PropertyValuesHolder cameraRotateYHolder =
                PropertyValuesHolder.ofFloat(cameraRotateYName, mCameraRotateY, 0);
        PropertyValuesHolder canvasTranslateXHolder =
                PropertyValuesHolder.ofFloat(canvasTranslateXName, mCanvasTranslateX, 0);
        PropertyValuesHolder canvasTranslateYHolder =
                PropertyValuesHolder.ofFloat(canvasTranslateYName, mCanvasTranslateY, 0);
        mShakeAnim = ValueAnimator.ofPropertyValuesHolder(cameraRotateXHolder,
                cameraRotateYHolder, canvasTranslateXHolder, canvasTranslateYHolder);
        mShakeAnim.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                //http://inloop.github.io/interpolator/
                float f = 0.571429f;
                return (float) (Math.pow(2, -2 * input) * Math.sin((input - f / 4) * (2 * Math.PI) / f) + 1);
            }
        });
        mShakeAnim.setDuration(1000);
        mShakeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCameraRotateX = (float) animation.getAnimatedValue(cameraRotateXName);
                mCameraRotateY = (float) animation.getAnimatedValue(cameraRotateYName);
                mCanvasTranslateX = (float) animation.getAnimatedValue(canvasTranslateXName);
                mCanvasTranslateY = (float) animation.getAnimatedValue(canvasTranslateYName);
            }
        });
        mShakeAnim.start();
    }
}
