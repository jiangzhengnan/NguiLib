package com.ng.ui.other.parrot;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.ng.nguilib.utils.LogUtils;
import com.ng.ui.R;
import com.webull.webulltv.webulldata.parrot.ParrotPillar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 描述:酷酷的螺旋鹦鹉图
 * todo 自动缩放逻辑
 *
 * @author Jzn
 * @date 2020-05-07
 */
public class ParrotView extends View {
    //基础属性
    private float mHeight;
    private float mWidth;
    private int mStartColor = Color.parseColor("#01EAFF");
    private int mEndColor = Color.parseColor("#D51C89");
    private long DURATION = 1500;
    private long SINGLE_DURATION = 700;
    private long SINGLE_INTERVAL = 1;


    //基础组件
    private Paint mPaint;

    //柱子数量
    private float mColumn = 1;
    //柱子数组
    private List<ParrotPillar> mParrotPillars;
    //柱子最大值
    private float mMaxValue;
    //柱子最大长度
    private float mMaxLength = getResources().getDimensionPixelOffset(R.dimen.dd300);
    //柱子间隔
    private float mInterval = 0.3f;

    //圆心半径
    private float mCenterR = getResources().getDimensionPixelOffset(R.dimen.dd20);
    //圆心内半径
    private float mCenterInsideR = mCenterR - getResources().getDimensionPixelOffset(R.dimen.dd04);
    //圆心边粗
    private float mCenterThick = getResources().getDimensionPixelOffset(R.dimen.dd01);
    //圆心颜色
    private int mCenterColor = Color.parseColor("#01EAFF");
    //圆心背景色
    private int mCenterBgColor = Color.parseColor("#101851");
    //圆心距右偏移量
    private float mCenterMarginRight = getResources().getDimensionPixelOffset(R.dimen.dd10);
    //圆心距上偏移量
    private float mCenterMarginTop = getResources().getDimensionPixelOffset(R.dimen.dd50);
    //圆心坐标
    private float mCenterX, mCenterY;
    //圆心角度
    private float mAngle;
    private RectF mBgOval;
    private RectF mInsideOval;


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mParrotPillars == null || mParrotPillars.size() == 0)
            return;

        //绘制螺旋图
        mStartAngle = -90;
        for (ParrotPillar temp : mParrotPillars) {
            drawSingleColumn(canvas, temp);
            mStartAngle += (mAngle + (mColumn > 1 ? mInterval : 0));
        }


        //绘制圆心bg
        mPaint.setColor(mCenterBgColor);
        canvas.drawOval(mBgOval, mPaint);
        //绘制圆心圈
        mPaint.setColor(mCenterColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mCenterThick);
        canvas.drawArc(mInsideOval, -90, 360f * mThickness, false, mPaint);

    }

    private float mStartAngle = -90;
    private float mTotalAngle = 0;

    private void drawSingleColumn(Canvas canvas, ParrotPillar temp) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(temp.getColor());

        //LogUtils.INSTANCE.d("length:" + temp.getAnimLength());
        float lengthR = temp.getAnimLength() / 2;
        RectF oval = new RectF(mCenterX - lengthR, mCenterY - lengthR,
                mCenterX + lengthR, mCenterY + lengthR);

        //LogUtils.INSTANCE.d("mStartAngle:" + mStartAngle + " mAngle:" + (mAngle + (mColumn > 1 ? mInterval : 0)));

        mTotalAngle += (mAngle + (mColumn > 1 ? mInterval : 0));
        canvas.drawArc(oval, mStartAngle, mAngle, true, mPaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        mCenterX = mWidth / 2 - mCenterMarginRight;
        mCenterY = mHeight / 2 + mCenterMarginTop;

        mBgOval = new RectF(mCenterX - mCenterR, mCenterY - mCenterR,
                mCenterX + mCenterR, mCenterY + mCenterR);
        mInsideOval = new RectF(mCenterX - mCenterInsideR, mCenterY - mCenterInsideR,
                mCenterX + mCenterInsideR, mCenterY + mCenterInsideR);
        LogUtils.INSTANCE.d("圆心:" + mCenterX + " " + mCenterY);
    }


    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    }

    private boolean isAnimRunning = false;
    //private ValueAnimator mAnimator;

    //动画组
    private List<Animator> mAnimatorList = new ArrayList<>();

    private float mThickness = 1f;

    private ValueAnimator mCircleAnimator;

    private int mNowAnimIndex = 0;


    //start anim
    public void startAnim() {
        if (isAnimRunning) {
            return;
        }
        isAnimRunning = true;

        //清空数据
        for (ParrotPillar temp : mParrotPillars) {
            temp.setAnimLength(0);
        }

        mNowAnimIndex = 0;
        mAnimatorList.get(mNowAnimIndex).start();


        mCircleAnimator = ValueAnimator.ofFloat(0, 1f);
        mCircleAnimator.setDuration(DURATION);
        mCircleAnimator.setInterpolator(new LinearInterpolator());
        mCircleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mThickness = (float) animation.getAnimatedValue();
                LogUtils.INSTANCE.d("animing: " + mThickness);
                getAngle();
                postInvalidate();
            }
        });
        mCircleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        mCircleAnimator.start();

    }

    public void setData(ArrayList<ParrotPillar> mParrotPillars) {
        mColumn = mParrotPillars.size();

        //这里要多算一点不然跟不上。。可能是动画启动耗费的时间吧orz
        SINGLE_INTERVAL = DURATION / (long) (mColumn * 2);


        //排序
        Collections.sort(mParrotPillars);

        this.mParrotPillars = mParrotPillars;
        //转换比率
        for (ParrotPillar temp : mParrotPillars) {
            if (mMaxValue < temp.getValue().floatValue())
                mMaxValue = temp.getValue().floatValue();
        }

        for (int i = 0; i < mColumn; i++) {
            ParrotPillar temp = mParrotPillars.get(i);
            int color = LinearGradientUtil.getCurrentColor(((float) i / (float) mColumn), mStartColor, mEndColor);
            temp.setColor(color);
            temp.setRatio(temp.getValue().floatValue() / mMaxValue);
            temp.setLength(mCenterR + mMaxLength * temp.getRatio());
            //temp.setAnimLength(mCenterR + mMaxLength * temp.getRatio());

        }


        LogUtils.INSTANCE.d("mMaxValue: " + mMaxValue + " mParrotPillars:" + mParrotPillars.toString());

        //得到单个角度
        getAngle();

        //初始化动画
        mAnimatorList = new ArrayList<>();
        for (int i = 0; i < mColumn; i++) {
            final ParrotPillar tempColum = mParrotPillars.get(i);
            ValueAnimator mTempAnimator = ValueAnimator.ofFloat(0, 1f);
            mTempAnimator.setDuration(SINGLE_DURATION);
            mTempAnimator.setStartDelay(SINGLE_INTERVAL);
            mTempAnimator.setInterpolator(new OvershootInterpolator());
            final int finalI = i;
            mTempAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    LogUtils.INSTANCE.d(finalI + " 开始执行了");
                    float mTempThickness = (float) animation.getAnimatedValue();
                    tempColum.setAnimLength(tempColum.getLength() * mTempThickness);
                    postInvalidate();
                }
            });
            mTempAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    //开启下一个
                    mNowAnimIndex += 1;
                    if (mNowAnimIndex < mColumn)
                        mAnimatorList.get(mNowAnimIndex).start();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    LogUtils.INSTANCE.d(finalI + " eee " + mColumn);
                    if (finalI == mColumn - 1) {
                        isAnimRunning = false;
                    }
                }
            });
            mAnimatorList.add(mTempAnimator);
        }

        postInvalidate();
    }

    private void getAngle() {
        float interValNum = 0;
        if (mColumn == 1) {
            interValNum = 0;
        } else if (mColumn == 2) {
            interValNum = 1;
        } else {
            interValNum = mColumn;
        }
        mAngle = (360f * 1f - interValNum * mInterval) / mColumn;
    }

    public ParrotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ParrotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!(mAnimatorList == null || mAnimatorList.size() == 0)) {
            for (Animator temp : mAnimatorList) {
                temp.cancel();
            }
        }
        if (mCircleAnimator != null) {
            mCircleAnimator.cancel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (!(mAnimatorList == null || mAnimatorList.size() == 0)) {
            for (Animator temp : mAnimatorList) {
                if (temp.isStarted() && temp.isRunning())
                    if (visibility == View.VISIBLE) {
                        temp.resume();
                    } else {
                        temp.pause();
                    }
            }
        }

        if (mCircleAnimator != null) {
            if (visibility == View.VISIBLE) {
                mCircleAnimator.resume();
            } else {
                mCircleAnimator.pause();
            }
        }
    }

}



