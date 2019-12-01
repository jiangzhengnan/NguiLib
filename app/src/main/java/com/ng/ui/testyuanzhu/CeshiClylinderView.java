package com.ng.ui.testyuanzhu;

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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @ProjectName: NGUI
 * @Package: com.ng.ui.testyuanzhu
 * @Description:
 * @Author: Pumpkin
 * @CreateDate: 2019/12/1
 */
public class CeshiClylinderView extends View {
    private Paint mWhitePaint;
    private Paint mCirclePaint;
    private float mCircleStrokeWidth = 2;
    private float mMaxRadius = 200;

    /* Camera旋转的最大角度 */
    private float mMaxCameraRotate = 15;

    /* 我们今天的主角 */
    private Matrix mMatrix;
    private Camera mCamera;

    /* Camera绕X轴旋转的角度 */
    private float mCameraRotateX;
    /* Camera绕Y轴旋转的角度 */
    private float mCameraRotateY;

    /* 手指松开时时钟晃动的动画 */
    private ValueAnimator mShakeAnim;


    private void init(){
        mMatrix = new Matrix();
        mCamera = new Camera();

        //白色大圆的画笔
        mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWhitePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mWhitePaint.setStrokeWidth(mCircleStrokeWidth);
        mWhitePaint.setColor(Color.WHITE);

        //内部蓝色圆环的画笔
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mCircleStrokeWidth);
        mCirclePaint.setColor(Color.WHITE);
        mCirclePaint.setColor(0xff237EAD);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setCameraRotate(canvas);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mMaxRadius, mWhitePaint);

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mMaxRadius / 6 * 2, mCirclePaint);
    }

    private void setCameraRotate(Canvas mCanvas) {
        mMatrix.reset();
        mCamera.save();
        mCamera.rotateX(mCameraRotateX);//绕x轴旋转
        mCamera.rotateY(mCameraRotateY);//绕y轴旋转
        mCamera.getMatrix(mMatrix);//计算对于当前变换的矩阵，并将其复制到传入的mMatrix中
        mCamera.restore();
        /**
         * Camera默认位于视图的左上角，故生成的矩阵默认也是以其左上角为旋转中心，
         * 所以在动作之前调用preTranslate将mMatrix向左移动getWidth()/2个长度，
         * 向上移动getHeight()/2个长度，
         * 使旋转中心位于矩阵的中心位置，动作之后再post回到原位
         */
        mMatrix.preTranslate(-getWidth() / 2, -getHeight() / 2);
        mMatrix.postTranslate(getWidth() / 2, getHeight() / 2);
        mCanvas.concat(mMatrix);//将mMatrix与canvas中当前的Matrix相关联
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mShakeAnim != null && mShakeAnim.isRunning()) {
                    mShakeAnim.cancel();
                }
                getCameraRotate(event);
                invalidate();
            case MotionEvent.ACTION_MOVE:
                //根据手指坐标计算Camera应该旋转的角度
                getCameraRotate(event);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //松开手指，时钟复原并伴随晃动动画
                startShakeAnim();
                break;
        }
        return true;
    }
    /**
     * 时钟晃动动画
     */
    private void startShakeAnim() {
        Log.d("nangua","staertDONGHUA");
        final String cameraRotateXName = "cameraRotateX";
        final String cameraRotateYName = "cameraRotateY";
        PropertyValuesHolder cameraRotateXHolder =
                PropertyValuesHolder.ofFloat(cameraRotateXName, mCameraRotateX, 0);
        PropertyValuesHolder cameraRotateYHolder =
                PropertyValuesHolder.ofFloat(cameraRotateYName, mCameraRotateY, 0);
        mShakeAnim = ValueAnimator.ofPropertyValuesHolder(cameraRotateXHolder,
                cameraRotateYHolder );
        mShakeAnim.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                //http://inloop.github.io/interpolator/
                float f = 0.571429f;
                return (float) (Math.pow(2, -2 * input) * Math.sin((input - f / 4) * (2 * Math.PI) / f) + 1);
            }
        });
        mShakeAnim.setDuration(500);
        mShakeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCameraRotateX = (float) animation.getAnimatedValue(cameraRotateXName);
                mCameraRotateY = (float) animation.getAnimatedValue(cameraRotateYName);
                postInvalidate();
            }
        });
        mShakeAnim.start();
    }


    private void getCameraRotate(MotionEvent event) {
        float rotateX = -(event.getY() - getHeight() / 2);
        float rotateY = (event.getX() - getWidth() / 2);
        /**
         *为什么旋转角度要这样计算：
         * 当Camera.rotateX(x)的x为正时，图像围绕X轴，上半部分向里下半部分向外，进行旋转，
         * 也就是手指触摸点要往上移。这个x就会与event.getY()的值有关，x越大，绕X轴旋转角度越大，
         * 以圆心为基准，手指往上移动，event.getY() - getHeight() / 2的值为负，
         * 故 float rotateX = -(event.getY() - getHeight() / 2)
         * 同理，
         * 当Camera.rotateY(y)的y为正时，图像围绕Y轴，右半部分向里左半部分向外，进行旋转，
         * 也就是手指触摸点要往右移。这个y就会与event.getX()的值有关，y越大，绕Y轴旋转角度越大，
         * 以圆心为基准，手指往右移动，event.getX() - getWidth() / 2的值为正，
         * 故 float rotateY = event.getX() - getWidth() / 2
         */

        /**
         * 此时得到的rotateX、rotateY 其实是以圆心为基准，手指移动的距离，
         * 这个值很大，不能用来作为旋转的角度，
         * 所以还需要继续处理
         */

        //求出移动距离与半径之比。mMaxRadius为白色大圆的半径
        float percentX = rotateX / mMaxRadius;
        float percentY = rotateY / mMaxRadius;

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

        //将最终的旋转角度控制在一定的范围内，这里mMaxCameraRotate的值为15，效果比较好
        mCameraRotateX = percentX * mMaxCameraRotate;
        mCameraRotateY = percentY * mMaxCameraRotate;
    }
    public CeshiClylinderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CeshiClylinderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
}
