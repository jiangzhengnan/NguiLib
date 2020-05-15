package com.ng.ui.other.parrot;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
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
import androidx.core.graphics.ColorUtils;

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
    private long DURATION = 1200;
    private long SINGLE_DURATION = 700;
    private long SINGLE_INTERVAL = 1;
    //定义常量pi（圆周率）
    private float pi = 3.1415926f;
    //基础组件
    private Paint mPaint;

    //柱子数量
    private float mColumn = 1;
    //柱子数组
    private List<ParrotPillar> mParrotPillars;
    //柱子最大值
    private float mMaxValue;
    //柱子最大长度
    private float mMaxLength = getResources().getDimensionPixelOffset(R.dimen.dd80);
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
    //圆形范围
    private RectF mBgOval;
    private RectF mInsideOval;

    //文字嵌入圆弧距离
    private float mEmbeddedArcDistanceMax = getResources().getDimensionPixelOffset(R.dimen.dd00);
    private float mEmbeddedArcDistanceMin = getResources().getDimensionPixelOffset(R.dimen.dd00);
    private float mEmbeddedArcDistanceNow;
    //文字距离圆弧距离
    private float mPaddingText = getResources().getDimensionPixelOffset(R.dimen.dd03);
    //文字大小
    private float mMaxTextSize = getResources().getDimensionPixelOffset(R.dimen.dd15);
    private float mMinTextSize = getResources().getDimensionPixelOffset(R.dimen.dd05);
    //文字颜色
    @SuppressLint("ResourceType")
    private int mTextColor = ColorUtils.setAlphaComponent(Color.parseColor("#ffffff"), 153);

    //动画类型
    public static final int ANIM_TYPE_NORMAL = 1;//普通转圈
    public static final int ANIM_TYPE_COLECT = 2;//收回
    public static final int ANIM_TYPE_BESSEL_COLECT = 3;//贝塞尔收回
    private int mAnimType;



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (Utils.isEmpty(mParrotPillars))
            return;

        //绘制螺旋图
        mStartAngle = -90;
        for (ParrotPillar temp : mParrotPillars) {
            drawSingleColumn(canvas, temp);
            mStartAngle += (mAngle + (mColumn > 1 ? mInterval : 0));
        }

        //绘制文字
        drawText(canvas);

        //绘制圆心bg
        mPaint.setColor(mCenterBgColor);
        canvas.drawOval(mBgOval, mPaint);
        //绘制圆心圈
        mPaint.setColor(mCenterColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mCenterThick);
        canvas.drawArc(mInsideOval, -90, 360f * mThickness, false, mPaint);


    }


    private float mStrStartAngle = -90;


    /**
     * 考虑到文字无论如何要看起来是正的，所以要做两个循环旋转角度来做😭
     * 因为drawtext 绘制文字不会居中的坑(对应基准线Baseline)，为了让字看起来是在中间的,所以加了一个角度微调的逻辑
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        canvas.save();
        mPaint.setColor(mTextColor);

        mStrStartAngle = -90 + mAngle / 2;

        int middle = (int) (mColumn / 2);

        float rightTotalRotato = 0;
        float lastFixAngle = 0;
        //右边
        for (int i = 0; i < middle; i++) {
            ParrotPillar temp = mParrotPillars.get(i);
            float lengthR = temp.getAnimLength();
            //动态设置文字大小
            mPaint.setTextSize(mMinTextSize + (mMaxTextSize - mMinTextSize) / mColumn * (mColumn - i));


            //计算微调角度
            float strHeight = getFontHeight(mPaint);
            //这里要多除一次2，因为对应基准线Baseline,所以相当于偏移了1/4的高度
            float fixAngle = strHeight / 2 / (2 * pi * lengthR) * 360 / 2;
            //LogUtils.INSTANCE.d(strHeight + " " + (2 * pi * lengthR) + " " + " 调整角度:" + fixAngle +"  ---"+ lastFixAngle);
            canvas.rotate(mStrStartAngle + fixAngle - lastFixAngle, mCenterX, mCenterY);
            lastFixAngle = fixAngle;
            //计算右侧偏移总角度
            rightTotalRotato += mStrStartAngle;
            //动态设置嵌入距离
            mEmbeddedArcDistanceNow = (mEmbeddedArcDistanceMin + (mEmbeddedArcDistanceMax - mEmbeddedArcDistanceMin) / mColumn * (mColumn - i));
            //增加固定加角
            mStrStartAngle = mAngle + (mColumn > 1 ? mInterval : 0);
            //绘制
            if (lengthR > mCenterR) {
                switch (mAnimType) {
                    case ANIM_TYPE_NORMAL:
                        canvas.drawText(temp.getName(), mCenterX - mEmbeddedArcDistanceNow + lengthR + mPaddingText, mCenterY, mPaint);
                        break;
                    case ANIM_TYPE_COLECT:

                        break;
                    case ANIM_TYPE_BESSEL_COLECT:

                        break;
                }
            }
        }
        canvas.restore();
        canvas.save();
        float tempAngle = 180f - Math.abs(rightTotalRotato);

        mStrStartAngle = -tempAngle + mAngle / 2;

        lastFixAngle = 0;
        //左边
        for (int i = middle; i < mColumn; i++) {
            ParrotPillar temp = mParrotPillars.get(i);
            float lengthR = temp.getAnimLength();
            //动态设置文字大小
            mPaint.setTextSize(mMinTextSize + (mMaxTextSize - mMinTextSize) / mColumn * (mColumn - i));


            //计算微调角度
            float strHeight = getFontHeight(mPaint);
            //这里要多除一次2，因为对应基准线Baseline,所以相当于偏移了1/4的高度
            float fixAngle = strHeight / 2 / (2 * pi * lengthR) * 360 / 2;
            canvas.rotate(mStrStartAngle + fixAngle - lastFixAngle, mCenterX, mCenterY);
            lastFixAngle = fixAngle;


            //动态设置嵌入距离
            mEmbeddedArcDistanceNow = (mEmbeddedArcDistanceMin + (mEmbeddedArcDistanceMax - mEmbeddedArcDistanceMin) / mColumn * (mColumn - i));

            mStrStartAngle = mAngle + (mColumn > 1 ? mInterval : 0) + 0.5f;    //这里偷懒写法，应该加上文字高度/2所占位置的角度大小

            //文字宽度
            float fontWidth = mPaint.measureText(temp.getName());
            if (lengthR > mCenterR) {
                canvas.drawText(temp.getName(), mCenterX + mEmbeddedArcDistanceNow - lengthR - fontWidth - mPaddingText, mCenterY, mPaint);
            }
        }

        canvas.restore();
    }

    /**
     * @return 返回指定的文字高度
     */
    public float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        //文字基准线的下部距离-文字基准线的上部距离 = 文字高度
        return fm.descent - fm.ascent;
    }

    private float mStartAngle = -90;

    private void drawSingleColumn(Canvas canvas, ParrotPillar temp) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(temp.getColor());
        //LogUtils.INSTANCE.d("length:" + temp.getAnimLength());
        float lengthR = temp.getAnimLength();
        RectF oval = new RectF(mCenterX - lengthR, mCenterY - lengthR,
                mCenterX + lengthR, mCenterY + lengthR);
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

    public void setData(ArrayList<ParrotPillar> mParrotPillars,int animType) {
        this.mAnimType = animType;
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
                    float mTempThickness = (float) animation.getAnimatedValue();
                    tempColum.setAnimLength(tempColum.getLength() * mTempThickness);


                    // LogUtils.INSTANCE.d(finalI + " 开始执行了" + (tempColum.getLength() * mTempThickness));

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
        if (!Utils.isEmpty(mAnimatorList)) {
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
        if (!Utils.isEmpty(mAnimatorList)) {
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




