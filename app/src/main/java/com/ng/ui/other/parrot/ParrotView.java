package com.ng.ui.other.parrot;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;

import com.ng.nguilib.utils.LogUtils;
import com.ng.nguilib.utils.Utils;

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
    //7中颜色
    private String mColors[] = {
            "#159BDB", "#1765E6",
            "#1BC0A5", "#129B85",
            "#6325FF", "#7843FF",
            "#872BAB", "#A237CB",
            "#9F882C", "#C9AA33",
            "#2687BB", "#31A4E2",
            "#2766FF", "#1C3DFF"
    };
    //默认颜色
    private String DEFAULT_COLOR = "#159BDB";
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
    private ArrayList<ParrotPillar> mParrotPillars;
    //柱子最大值
    private float mMaxValue;
    //柱子总值shou
    private float mTotalValue;
    //柱子最大长度
    private float mMaxLength = getResources().getDimensionPixelOffset(com.ng.nguilib.R.dimen.dd208);
    //柱子间隔
    private float mInterval = 0.5f;

    //圆心半径
    private float mCenterR = getResources().getDimensionPixelOffset(com.ng.nguilib.R.dimen.dd13);
    //圆心内半径
    private float mCenterInsideR = mCenterR - getResources().getDimensionPixelOffset(com.ng.nguilib.R.dimen.dd04);
    //圆心边粗
    private float mCenterThick = getResources().getDimensionPixelOffset(com.ng.nguilib.R.dimen.dd01);
    //圆心颜色
    private int mCenterColor = Color.parseColor("#01EAFF");
    //圆心背景色
    private int mCenterBgColor = Color.parseColor("#101851");
    //圆心距右偏移量
    private float mCenterMarginRight = getResources().getDimensionPixelOffset(com.ng.nguilib.R.dimen.dd28);
    //圆心距上偏移量
    private float mCenterMarginTop = getResources().getDimensionPixelOffset(com.ng.nguilib.R.dimen.dd90);
    //圆心坐标
    private float mCenterX, mCenterY;
    //圆心角度
    private float mAngle;
    //圆形范围
    private RectF mBgOval;
    private RectF mInsideOval;

    //文字嵌入圆弧距离
    private float mEmbeddedArcDistanceMax = getResources().getDimensionPixelOffset(com.ng.nguilib.R.dimen.dd55);
    private float mEmbeddedArcDistanceMin = getResources().getDimensionPixelOffset(com.ng.nguilib.R.dimen.dd05);
    private float mEmbeddedArcDistanceNow;
    //文字距离圆弧距离
    private float mPaddingText = getResources().getDimensionPixelOffset(com.ng.nguilib.R.dimen.dd03);
    //文字大小
    private float mMaxTextSize = getResources().getDimensionPixelOffset(com.ng.nguilib.R.dimen.dd11);
    private float mMinTextSize = getResources().getDimensionPixelOffset(com.ng.nguilib.R.dimen.dd06);
    //文字颜色
    @SuppressLint("ResourceType")
    private int mTextColor = ColorUtils.setAlphaComponent(getResources().getColor(com.ng.nguilib.R.color.nc306_black), 153);

    //loop
    private long LOOP_DURATION = 30 * 1000;

    //todo 自动缩放功能


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
        mPaint.setShader(null);

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
            if (lengthR > mCenterR)
                canvas.drawText(temp.getName(), mCenterX - mEmbeddedArcDistanceNow + lengthR + mPaddingText, mCenterY, mPaint);
        }
        canvas.restore();
        canvas.save();

        //rightTotalRotato -= lastFixAngle;

        float tempAngle = 180f - Math.abs(rightTotalRotato);


        mStrStartAngle = -tempAngle + mAngle;

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


            canvas.rotate(mStrStartAngle - fixAngle + lastFixAngle, mCenterX, mCenterY);

            lastFixAngle = fixAngle;


            //动态设置嵌入距离
            mEmbeddedArcDistanceNow = (mEmbeddedArcDistanceMin + (mEmbeddedArcDistanceMax - mEmbeddedArcDistanceMin) / mColumn * (mColumn - i));

            mStrStartAngle = mAngle + (mColumn > 1 ? mInterval : 0);

            //文字宽度
            float fontWidth = mPaint.measureText(temp.getName());
            if (lengthR > mCenterR)
                canvas.drawText(temp.getName(), mCenterX + mEmbeddedArcDistanceNow - lengthR - fontWidth - mPaddingText, mCenterY, mPaint);
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
        float lengthR = temp.getAnimLength();

        //设置双重渐变色
        int alpha = temp.getAlpha();
        int startColor = ColorUtils.setAlphaComponent(Color.parseColor(temp.getStartColor()), 255 * alpha / 100);
        int endColor = ColorUtils.setAlphaComponent(Color.parseColor(temp.getEndColor()), 255 * alpha / 100);

        RadialGradient radialGradient = new RadialGradient(mCenterX, mCenterY, mCenterX + lengthR,
                startColor, endColor, LinearGradient.TileMode.CLAMP
        );
        mPaint.setShader(radialGradient);


        //LogUtils.INSTANCE.d("length:" + temp.getAnimLength());
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
    }


    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setAntiAlias(true);

        setLayerType(LAYER_TYPE_HARDWARE, mPaint);


        //setBackground(DrawableUtils.createBackground());

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

        mCircleAnimator = ValueAnimator.ofFloat(0, 1f);
        mCircleAnimator.setDuration(DURATION);
        mCircleAnimator.setInterpolator(new LinearInterpolator());
        mCircleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mThickness = (float) animation.getAnimatedValue();
                getAngle();
            }
        });
        mCircleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (mAnimatorList != null && mAnimatorList.size() != 0)
                    mAnimatorList.get(mNowAnimIndex).start();

//                //开始loop
//                if (!isLoop) {
//                    isLoop = true;
//                    loopHandler.sendEmptyMessageDelayed(0, LOOP_DURATION);
//                }
            }
        });
        mCircleAnimator.start();

    }

    public void setData(ArrayList<ParrotPillar> parrotPillars) {
        LogUtils.INSTANCE.d("setdata:" + parrotPillars);
        if (Utils.isEmpty(parrotPillars)) {
            return;
        }


        mColumn = parrotPillars.size();
        mTotalValue = 0;
        //这里要多算一点不然跟不上。。可能是动画启动耗费的时间吧orz
        SINGLE_INTERVAL = DURATION / (long) (mColumn);

        //排序
        Collections.sort(parrotPillars);


        this.mParrotPillars = parrotPillars;
        //转换比率
        for (ParrotPillar temp : mParrotPillars) {
            if (mMaxValue < temp.getValue().floatValue())
                mMaxValue = temp.getValue().floatValue();

            mTotalValue += temp.getValue().floatValue();


//            USStateItem nameItem = USStateUtil.Companion.getItemByCode(temp.getName());
//            if (nameItem != null)
//                temp.setName(nameItem.getName());
        }

        float interval = 0f;
        for (int i = 0; i < mColumn; i++) {
            ParrotPillar temp = mParrotPillars.get(i);

            //得到当前颜色和透明度
            int colorLenght = mColors.length / 2;//7
            int tempIndex = i + 1;
            int tens = tempIndex / colorLenght;
            int alpha = 100 - tens * 10;
            if (alpha < 100) {
                alpha = 100;
            }

            temp.setAlpha(alpha);

            int index = tempIndex % colorLenght;

            if (index == 0) {
                index = colorLenght;
            }

            int startColorIndex = index * 2 - 2;


            int endColorIndex = index * 2 - 1;
            if (mColors.length > startColorIndex && startColorIndex >= 0) {
                temp.setStartColor(mColors[startColorIndex]);
            } else {
                temp.setStartColor(mColors[0]);
            }


            if (mColors.length > endColorIndex && endColorIndex >= 0) {
                temp.setEndColor(mColors[endColorIndex]);
            } else {
                temp.setEndColor(mColors[0]);
            }


            float ratio = 0f;

            if (i == 0) {
                ratio = 1f;
            } else if (i < mColumn / 8f) {    // 2/3
                interval = 0.666f / (mColumn / 8f);
                ratio = 1f - i * interval;

            } else if (i < mColumn / 4f) {       // 1/6
                interval = 0.166f / (mColumn / 4f);
                ratio = 0.333f - (i - mColumn / 8f) * interval;

            } else {                             // 1/6
                interval = 0.15f / (mColumn * 5 / 8);
                ratio = 0.166f - (i - mColumn * 5 / 8) * interval;
            }

            if (ratio < 0.02) {
                ratio = 0.02f;
            }

            temp.setRatio(ratio);
            temp.setLength(mCenterR + mMaxLength * temp.getRatio());
            //temp.setAnimLength(mCenterR + mMaxLength * temp.getRatio());
        }


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
                    postInvalidate();

                }
            });
            mTempAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    //开启下一个
                    mNowAnimIndex += 1;
                    if (mNowAnimIndex < mAnimatorList.size()) {
                        mAnimatorList.get(mNowAnimIndex).start();
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (finalI == mColumn - 1) {
                        isAnimRunning = false;


                    }
                }
            });
            mAnimatorList.add(mTempAnimator);
        }


        startAnim();
    }

//    private volatile boolean isLoop = false;
//    @SuppressLint("HandlerLeak")
//    private Handler loopHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            setData(mParrotPillars);
//            if (loopHandler != null)
//                loopHandler.sendEmptyMessageDelayed(0, LOOP_DURATION);
//        }
//    };

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
        if (!Utils.isEmpty(mAnimatorList)) {
            for (Animator temp : mAnimatorList) {
                temp.cancel();
            }
        }
        if (mCircleAnimator != null) {
            mCircleAnimator.cancel();
        }
//        if (loopHandler != null) {
//            loopHandler.removeCallbacksAndMessages(null);
//        }

        super.onDetachedFromWindow();
    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    @Override
//    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
//        super.onVisibilityChanged(changedView, visibility);
//        if (!Utils.isEmpty(mAnimatorList)) {
//            for (Animator temp : mAnimatorList) {
//                if (temp.isStarted() && temp.isRunning())
//                    if (visibility == View.VISIBLE) {
//                        temp.resume();
//                    } else {
//                        temp.pause();
//                    }
//            }
//        }
//
//        if (mCircleAnimator != null) {
//            if (visibility == View.VISIBLE) {
//                mCircleAnimator.resume();
//            } else {
//                mCircleAnimator.pause();
//            }
//        }
//    }

}


