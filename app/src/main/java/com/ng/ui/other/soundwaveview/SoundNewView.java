package com.ng.ui.other.soundwaveview;

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

import com.ng.nguilib.utils.MLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2019-12-20
 */
public class SoundNewView extends View {
    private Paint mPaint;
    private float mHeight;
    private float mWidth;
    private Path mPath;
    //周期
    private float T = 2f;
    //总偏移量
    private int mAllOffsetX = 0;
    //垂直方向间距
    private float mSpaceY = dp2px(3);
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
    //中心点坐标
    private float mCenterX;
    private float mCenterY;
    private boolean isWaveAnimRunning = false;


    private ValueAnimator mWaveAnimator;


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
    private final static int VOLUE_THRESHOLD = 5;


    //添加切换的估值器
    public void setVolume(int volume) {
        if (volume < 0 || volume > 100) {
            return;
        }
        mSourceVolume = volume;
        mMaxVolume = VOLUME_INIT + mSourceVolume;

        mVolume = mMaxVolume / 100f;

        postInvalidate();
    }

    public SoundNewView(Context context, AttributeSet attrs) {
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

                mAmplitude = (int) (mHeight / 4 * mVolume);


                mAllOffsetX += dp2px(5);


                //ball
                // 剔除已经执行完的点
                Iterator<DynamicAttenuationModel> iterator = ballModelList.iterator();
                while (iterator.hasNext()) {
                    DynamicAttenuationModel temp = iterator.next();
                    if (temp.getTotalDuration() + temp.startTime < System.currentTimeMillis()) {
                        iterator.remove();
                    }
                }

                // 如果为空则重新生成点
                if (ballModelList.size() == 0) {
                    //随机生成2-4个
                    int randomSize = new Random().nextInt(3) + 2;


                    for (int i = 0; i < randomSize; i++) {
                        ballModelList.add(new DynamicAttenuationModel());
                    }
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
    }

    //随机小球数组
    private volatile List<DynamicAttenuationModel> ballModelList = new ArrayList<>();


    //圆圈位置计算方程
    private synchronized float equation(float xFloat, float yFloat) {

        long currentTime = System.currentTimeMillis();
        // 波动衰减修正
        /// 波动最大振幅
        float maxAmplitude = mHeight / 3;
        float attenuationOffset = 0;
        synchronized (ballModelList) {
            Iterator<DynamicAttenuationModel> iterator = ballModelList.iterator();
            while (iterator.hasNext()) {
                DynamicAttenuationModel model = iterator.next();
                if (model == null) {
                    continue;
                }

                double process = 0;
                if ((currentTime - model.startTime - model.upDuration < 0) && model.upDuration > 0) {
                    //处于上升阶段
                    process = (currentTime - model.startTime) / (double) model.upDuration;
                } else if ((currentTime - model.startTime - model.getTotalDuration() < 0) && model.downDuration > 0) {
                    //处于下降阶段
                    process = 1 - (currentTime - model.startTime - model.upDuration) / (double) model.downDuration;
                } else {
                    //已经结束，不处理
                    continue;
                }


                //距离中心点距离
                double distance = Math.abs(
                        Math.sqrt(Math.pow(model.centerPoint.x - xFloat, 2))
                                + Math.pow(model.centerPoint.y - yFloat, 2)
                );

                // 范围衰减，中心最大
                double distanceProcess = Math.min(
                        Math.max((model.range - distance) / model.range, 0),
                        1
                );


                attenuationOffset += maxAmplitude * process * distanceProcess * model.amplitude;


            }
        }


        return attenuationOffset;

    }


    public SoundNewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        for (int i = 1; i <= WAVE_LINE_NUMBER; i++) {
            int tempAmplitude;

            tempAmplitude = mAmplitude;

            //小球变大
            mBallRadius = 1 + ((float) i / (float) WAVE_LINE_NUMBER) * 1f;
            //透明度
            //int alpha = 25 + 230 * i / WAVE_LINE_NUMBER;
            mPaint.setAlpha(255);

            drawVoiceLine(canvas,
                    mAllOffsetX,
                    (int) (mHeight / 3 + i * mSpaceY),
                    tempAmplitude, i);
        }
    }


    /**
     * y=Asin(ωx+φ)+k
     *
     * @param canvas    画板
     * @param xOffset   x轴偏移量
     * @param yLocation y轴坐标
     */
    private void drawVoiceLine(Canvas canvas, int xOffset, int yLocation, int tempAmplitude, int index) {
        int xPos = 0;
        mPath.reset();
        mPath.moveTo(0, 0);


        while (xPos <= (int) mWidth) {
            //a的范围:0~a~0

            //基础振幅
//            float singleTempAmp = 4 * tempAmplitude * xPos / mWidth -
//                    4 * tempAmplitude * xPos / mWidth * xPos / mWidth;


            int endY;
            //周期w
            int baseY = (int) (Math.sin(((float) xPos + (float) xOffset) * T * Math.PI / mWidth + 0)
                    * tempAmplitude + yLocation);


            float xFloat = ((float) xPos) / mWidth;
            float yFloat = ((float) index) / WAVE_LINE_NUMBER;

            float pianyiLiang = equation(xFloat, yFloat);


            endY = (int) (baseY + pianyiLiang);


            mPath.moveTo(xPos, endY);


            mPath.addCircle(xPos, endY, mBallRadius, Path.Direction.CCW);
            xPos += mSpaceX;
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

        //todo 暂时只用一个遮罩
        mPaint.setShader(mLinearGradient);
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

    //动态衰减
    public class DynamicAttenuationModel {
        //衰减开始时间
        public long startTime;
        /// 上升时间
        public long upDuration;
        /// 下降时间
        public long downDuration;
        /// 衰减中心点(0-1)
        public Point centerPoint;
        /// 波及范围(0-1)
        public float range;
        /// 波动振幅
        public float amplitude;

        public long getTotalDuration() {
            return upDuration + downDuration;
        }

        //随机生成
        public DynamicAttenuationModel() {
            Random random = new Random();
            startTime = System.currentTimeMillis();
            upDuration = nextLong(random, 100, 400);
            downDuration = nextLong(random, 200, 800);
            centerPoint = new Point(random.nextFloat(), random.nextFloat());
            range = nextFloat(random, 0.4f, 0.8f);
            amplitude = nextFloat(random, -1, 1);

            MLog.INSTANCE.d("生成的点: " + this.toString());
        }

        long nextLong(Random random, long minValue, long maxValue) {
            if (minValue > maxValue) {
                return 0;
            }
            long num = maxValue - minValue;
            return minValue + (long) (random.nextDouble() * num);
        }

        float nextFloat(Random random, float minValue, float maxValue) {
            if (minValue > maxValue) {
                return 0;
            }
            float num = maxValue - minValue;
            return minValue + (float) (random.nextDouble() * num);
        }

        @Override
        public String toString() {
            return "DynamicAttenuationModel{" +
                    "startTime=" + startTime +
                    ", upDuration=" + upDuration +
                    ", downDuration=" + downDuration +
                    ", centerPoint=" + centerPoint +
                    ", range=" + range +
                    ", amplitude=" + amplitude +
                    '}';
        }
    }


    public class Point {
        public float x;
        public float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
