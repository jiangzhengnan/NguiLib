package com.ng.ui.other.parrot;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.ColorUtils;

import com.ng.nguilib.utils.LinearGradientUtil;
import com.ng.nguilib.utils.Utils;
import com.ng.ui.R;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 描述:酷酷的螺旋鹦鹉图😠
 * 工作版本
 *
 * @author Jzn
 * @date 2020-05-07
 */
public class ParrotView extends View {
    private static final long LOOP_DURATION = 10 * 1000;
    //基础属性
    private float mHeight;
    private float mWidth;
    //7种颜色
    private String[] mColors = {
            "#159BDB", "#1765E6",
            "#1BC0A5", "#129B85",
            "#6325FF", "#7843FF",
            "#872BAB", "#A237CB",
            "#9F882C", "#C9AA33",
            "#2687BB", "#31A4E2",
            "#2766FF", "#1C3DFF"
    };
    //定义常量pi（圆周率）
    private float pi = 3.1415926f;
    //基础组件
    private Paint mPaint;

    //柱子数量
    private float mColumn = 1;
    //柱子数组
    private ArrayList<ParrotPillar> mParrotPillars;
    //柱子最大长度
    private float mMaxLength = getResources().getDimensionPixelOffset(R.dimen.dd208);
    //柱子间隔
    private float mInterval = 0.5f;

    //圆心半径
    private float mCenterR = getResources().getDimensionPixelOffset(R.dimen.dd13);
    //圆心内半径
    private float mCenterInsideR = mCenterR - getResources().getDimensionPixelOffset(R.dimen.dd04);
    //圆心边粗
    private float mCenterThick = getResources().getDimensionPixelOffset(R.dimen.dd01);
    //圆心颜色
    private int mCenterColor = Color.parseColor("#01EAFF");
    //圆心背景色
    private int mCenterBgColor = Color.parseColor("#101851");
    //圆心距右偏移量
    private float mCenterMarginRight = getResources().getDimensionPixelOffset(R.dimen.dd23);
    //圆心距上偏移量
    private float mCenterMarginTop = getResources().getDimensionPixelOffset(R.dimen.dd90);
    //圆心坐标
    private float mCenterX, mCenterY;
    //圆心角度
    private float mAngle;
    //圆形范围
    private RectF mBgOval;
    private RectF mInsideOval;

    //文字嵌入圆弧距离
    private float mEmbeddedArcDistanceMax = getResources().getDimensionPixelOffset(R.dimen.dd60);
    private float mEmbeddedArcDistanceMin = getResources().getDimensionPixelOffset(R.dimen.dd08);
    private float mEmbeddedArcDistanceNow;
    //文字距离圆弧距离
    private float mPaddingText = getResources().getDimensionPixelOffset(R.dimen.dd04);
    //文字大小
    private float mMaxTextSize = getResources().getDimensionPixelOffset(R.dimen.dd10);
    private float mMinTextSize = getResources().getDimensionPixelOffset(R.dimen.dd05);
    //文字颜色
    @SuppressLint("ResourceType")
    private int mTextColor = ColorUtils.setAlphaComponent(getResources().getColor(R.color.nc306_black), 153);
    //文字绘制旋转角
    private float mStrStartAngle = -90;


    //动画组
    private long DURATION = 1200;
    private long SINGLE_DURATION = 700;
    private long SINGLE_INTERVAL = 1;
    private List<Animator> mAnimatorList = new ArrayList<>();
    private float mThickness = 1f;
    private ValueAnimator mCircleAnimator;

    private List<Animator> mValueAnimatorList = new ArrayList<>();


    private ValueAnimator mSwitchAnimator;

    private long VALUE_DURATION = 800;
    private long VALUE_SINGLE_DURATION = 800;

    private long SWITCH_DURATION = 400;

    private int mNowAnimIndex = 0;
    private boolean isAnimRunning = false;
    //private ValueAnimator mAnimator;
    //动画类型
    private final static String ANIM_TYPE_SWITCH = "switch";    //成对修改
    private final static String ANIM_TYPE_VALUE = "value";     //部分修改
    private final static String ANIM_TYPE_NUMBER = "number";    //全部修改
    private final static String ANIM_TYPE_NULL = "null";    //不修改
    private String mAnimType = ANIM_TYPE_NUMBER;


    //初始化
    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setAntiAlias(true);
        setLayerType(LAYER_TYPE_HARDWARE, mPaint);
        //todo daki
        loopHandler = new LoopHandler(this);

        initAnim();
    }

    //初始化动画
    private void initAnim() {
        mCircleAnimator = ValueAnimator.ofFloat(0, 1f);
        mCircleAnimator.setDuration(DURATION);
        mCircleAnimator.setInterpolator(new LinearInterpolator());
        mCircleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mThickness = (float) animation.getAnimatedValue();
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
            }
        });


        mSwitchAnimator = ValueAnimator.ofFloat(0f, 1f);
        mSwitchAnimator.setDuration(SWITCH_DURATION);
        mSwitchAnimator.setInterpolator(new DecelerateInterpolator());

        mValueAnimatorList = new ArrayList<>();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Utils.isEmpty(mParrotPillars)) {
            return;
        }

        //NgLogUtils.INSTANCE.d("onDraw:" + mColumn + " " + mAngle + " " + mParrotPillars.toString());

        //绘制螺旋图
        for (int i = 0; i < mParrotPillars.size(); i++) {
            drawSingleColumn(canvas, mParrotPillars.get(i), i);
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

    /**
     * 考虑到文字无论如何要看起来是正的，所以要做两个循环旋转角度来做😭
     * 因为drawtext 绘制文字不会居中的坑(对应基准线Baseline)，为了让字看起来是在中间的,所以加了一个角度微调的逻辑
     *
     * @param canvas canvas
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
            canvas.rotate(mStrStartAngle + fixAngle - lastFixAngle, mCenterX, mCenterY);
            lastFixAngle = fixAngle;
            //计算右侧偏移总角度
            rightTotalRotato += mStrStartAngle;
            //动态设置嵌入距离
            mEmbeddedArcDistanceNow = (mEmbeddedArcDistanceMin + (mEmbeddedArcDistanceMax - mEmbeddedArcDistanceMin) / mColumn * (mColumn - i));
            //增加固定加角
            mStrStartAngle = mAngle + (mColumn > 1 ? mInterval : 0);

            mPaint.setAlpha(temp.getStrAlpha());
            //绘制
            if (lengthR > mCenterR)
                canvas.drawText(temp.getName(), mCenterX - mEmbeddedArcDistanceNow + lengthR + mPaddingText, mCenterY, mPaint);
        }
        canvas.restore();
        canvas.save();
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
            mPaint.setAlpha(temp.getStrAlpha());
            if (lengthR > mCenterR)
                canvas.drawText(temp.getName(), mCenterX + mEmbeddedArcDistanceNow - lengthR - fontWidth - mPaddingText, mCenterY, mPaint);
        }
        mPaint.setAlpha(153);
        canvas.restore();
    }

    //返回指定的文字高度
    public float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        //文字基准线的下部距离-文字基准线的上部距离 = 文字高度
        return fm.descent - fm.ascent;
    }

    private HashMap<Integer, RadialGradient> mShaders = new HashMap<>();

    //绘制单个弧片
    private void drawSingleColumn(Canvas canvas, ParrotPillar temp, int index) {
        mPaint.setStyle(Paint.Style.FILL);
        float lengthR = temp.getAnimLength();
        //设置双重渐变色
        int alpha = temp.getAlpha();
        int startColor = ColorUtils.setAlphaComponent(Color.parseColor(temp.getStartColor()), 255 * alpha / 100);
        int endColor = ColorUtils.setAlphaComponent(Color.parseColor(temp.getEndColor()), 255 * alpha / 100);

        RadialGradient radialGradient;
        if (mShaders != null && mShaders.get(index) != null) {
            radialGradient = mShaders.get(index);
        } else {
            radialGradient = new RadialGradient(mCenterX, mCenterY, mCenterX + temp.getLength(),
                    startColor, endColor, RadialGradient.TileMode.CLAMP
            );
            mShaders.put(index, radialGradient);
        }
        mPaint.setShader(radialGradient);
        RectF oval = new RectF(mCenterX - lengthR, mCenterY - lengthR,
                mCenterX + lengthR, mCenterY + lengthR);
        canvas.drawArc(oval, temp.getStartAngle(), mAngle, true, mPaint);
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


    /**
     * 核心方法
     * 步骤:
     * 根据动画类型执行动画
     *
     * @param dataList 数据
     */
    public void setData(ArrayList<ParrotPillar> dataList) {
        //运行中则不执行
        if (isAnimRunning) {
            return;
        }
        //判断数据准确性
        if (isDataError(dataList)) {
            return;
        }
        //判断当前需要执行的动画类型,判断是否要修改,处理保存数据
        Collections.sort(dataList);
        resetData(dataList);

        mAnimType = getAnimType(dataList, mParrotPillars);
        if (mAnimType.equals(ANIM_TYPE_NULL)) return;

        if (isAnimRunning) {
            return;
        }
        isAnimRunning = true;
        //根据动画类型执行动画
        switch (mAnimType) {
            case ANIM_TYPE_SWITCH:
                resetParameter(dataList);
                startSwitchAnim(dataList);
                break;
            case ANIM_TYPE_VALUE:
                resetParameter(dataList);
                startValueAnim(dataList);
                break;
            case ANIM_TYPE_NUMBER:
                this.mParrotPillars = dataList;
                //初始化参数
                resetParameter(mParrotPillars);
                //转化固定比率,设置透明度和颜色
                startNumberAnim();
                break;
        }

        //开启loop
//        if (!isLoop) {
//            isLoop = true;
//            loopHandler.sendEmptyMessageDelayed(0, LOOP_DURATION);
//        }
    }

    public void clearData() {
        stopAllAnim();
        if (!Utils.isEmpty(mParrotPillars)) {
            mParrotPillars.clear();
        }
    }

    //对换更新动画
    private void startSwitchAnim(ArrayList<ParrotPillar> newData) {
        //startValueAnim(newDataList);
        mNewParrotPillars = newData;
        //需要更新的值
        List<Integer> mValueUpdateIndex = new ArrayList<>();
        for (int i = 0; i < mNewParrotPillars.size(); i++) {
            if (!mNewParrotPillars.get(i).equals(mParrotPillars.get(i))) {
                mValueUpdateIndex.add(i);
            }
            mNewParrotPillars.get(i).setAnimLength(mParrotPillars.get(i).getAnimLength());
        }
        //不是两个就爪巴
        if (mValueUpdateIndex.size() != 2) {
            startValueAnim(newData);
            return;
        }
        int index1 = mValueUpdateIndex.get(0);
        int index2 = mValueUpdateIndex.get(1);
        ParrotPillar switch1 = mParrotPillars.get(index1);
        ParrotPillar switch2 = mParrotPillars.get(index2);
        float switch1Angle = switch1.getStartAngle();
        float switch2Angle = switch2.getStartAngle();
        float switch1Length = switch1.getAnimLength();
        float switch2Length = switch2.getAnimLength();

        int switch1StartColor = Color.parseColor(switch1.getStartColor());
        int switch1EndColor = Color.parseColor(switch1.getEndColor());
        int switch2StartColor = Color.parseColor(switch2.getStartColor());
        int switch2EndColor = Color.parseColor(switch2.getEndColor());

        mSwitchAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //角度 互换
                float fraction = animation.getAnimatedFraction();
                switch1.setStartAngle(switch1Angle + (switch2Angle - switch1Angle) * fraction);
                switch2.setStartAngle(switch2Angle + (switch1Angle - switch2Angle) * fraction);

                mNewParrotPillars.get(index2).setStartAngle(switch1.getStartAngle());
                mNewParrotPillars.get(index1).setStartAngle(switch2.getStartAngle());

                //高度互换
                switch1.setAnimLength(switch1Length + (switch2Length - switch1Length) * fraction);
                switch2.setAnimLength(switch2Length + (switch1Length - switch2Length) * fraction);
                mNewParrotPillars.get(index2).setAnimLength(switch1.getAnimLength());
                mNewParrotPillars.get(index1).setAnimLength(switch2.getAnimLength());
                //颜色互换
                switch1.setStartColor(getHexString(LinearGradientUtil.getCurrentColor(fraction, switch1StartColor, switch2StartColor)));
                switch1.setEndColor(getHexString(LinearGradientUtil.getCurrentColor(fraction, switch1EndColor, switch2EndColor)));

                switch2.setStartColor(getHexString(LinearGradientUtil.getCurrentColor(fraction, switch2StartColor, switch1StartColor)));
                switch2.setEndColor(getHexString(LinearGradientUtil.getCurrentColor(fraction, switch2EndColor, switch1EndColor)));

                mNewParrotPillars.get(index2).setStartColor(switch1.getStartColor());
                mNewParrotPillars.get(index2).setEndColor(switch1.getEndColor());

                mNewParrotPillars.get(index1).setStartColor(switch2.getStartColor());
                mNewParrotPillars.get(index1).setEndColor(switch2.getEndColor());

                //文字互换
                postInvalidate();
            }
        });

        mSwitchAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                switch1.setStrAlpha(0);
                switch2.setStrAlpha(0);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                switch1.setStrAlpha(155);
                switch2.setStrAlpha(155);
                mParrotPillars = mNewParrotPillars;
                isAnimRunning = false;
                postInvalidate();
            }
        });
        mSwitchAnimator.start();
    }

    //新的
    private ArrayList<ParrotPillar> mNewParrotPillars;

    //值更新动画
    private void startValueAnim(ArrayList<ParrotPillar> newData) {
        mNewParrotPillars = newData;

        //需要更新的值
        List<Integer> mValueUpdateIndex = new ArrayList<>();
        for (int i = 0; i < mNewParrotPillars.size(); i++) {
            if (!mNewParrotPillars.get(i).equals(mParrotPillars.get(i))) {
                mValueUpdateIndex.add(i);
            }

        }
        //重置动画队列
        if (Utils.isEmpty(mValueAnimatorList)) {
            mValueAnimatorList = new ArrayList<>();
        } else {
            mValueAnimatorList.clear();
        }

        for (int i = 0; i < mColumn; i++) {
            if (mValueUpdateIndex.contains(i)) {
                ParrotPillar newTemp = mNewParrotPillars.get(i);
                ParrotPillar nowTemp = mParrotPillars.get(i);
                ValueAnimator mTempAnimator = ValueAnimator.ofFloat(1f, 1.05f, 1f);
                mTempAnimator.setDuration((long) (VALUE_DURATION + 500l * Math.random()));
                mTempAnimator.setStartDelay((long) (200 + VALUE_SINGLE_DURATION * Math.random()));
                mTempAnimator.setInterpolator(getRamdomInterPolator());
                final int finalI = i;
                mTempAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) animation.getAnimatedValue();
                        float fraction = animation.getAnimatedFraction();

                        float tempAnimLenght = nowTemp.getLength() * value;
                        float strAplhaRate = Math.abs((fraction - 0.5f) / 0.5f);

                        if (!nowTemp.getName().equals(newTemp.getName())) {
                            //透明度和文字修改
                            if (fraction > 0.5f) {
                                nowTemp.setName(newTemp.getName());
                            }
                            nowTemp.setStrAlpha((int) (153 * strAplhaRate));
                        } else {
                            nowTemp.setStrAlpha(153);
                        }

                        nowTemp.setAnimLength(tempAnimLenght);
                        newTemp.setAnimLength(tempAnimLenght);

                        //mCenterThick
                        if (finalI == mValueUpdateIndex.get(mValueUpdateIndex.size() - 1)) {
                            mCenterThick = getResources().getDimensionPixelOffset(R.dimen.dd01) +
                                    getResources().getDimensionPixelOffset(R.dimen.dd02) * (1 - strAplhaRate);
                        }


                        postInvalidate();
                    }
                });
                mTempAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        //开启下一个
                        mNowAnimIndex += 1;
                        if (mNowAnimIndex < mValueAnimatorList.size()) {
                            mValueAnimatorList.get(mNowAnimIndex).start();
                        }

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (finalI == mValueUpdateIndex.get(mValueUpdateIndex.size() - 1)) {
                            isAnimRunning = false;
                            postInvalidate();
                        }
                    }
                });
                mValueAnimatorList.add(mTempAnimator);
            }
        }
        mNowAnimIndex = 0;

        Collections.shuffle(mValueAnimatorList);
        mValueAnimatorList.get(0).start();
    }

    private TimeInterpolator getRamdomInterPolator() {
        int random = (int) (Math.random() * 7 + 1);
        switch (random) {
            case 0:
                return new AccelerateDecelerateInterpolator();
            case 1:
                return new AccelerateInterpolator();
            case 2:
                return new DecelerateInterpolator();
            case 3:
            case 5:
            case 6:
            case 7:
                return new LinearInterpolator();
            case 4:
                return new OvershootInterpolator();

        }
        return new LinearInterpolator();
    }

    //全量更新动画
    private void startNumberAnim() {
        //重置动画队列
        if (Utils.isEmpty(mAnimatorList)) {
            mAnimatorList = new ArrayList<>();
        } else {
            mAnimatorList.clear();
        }
        for (int i = 0; i < mColumn; i++) {
            ParrotPillar tempColum = mParrotPillars.get(i);
            ValueAnimator mTempAnimator = ValueAnimator.ofFloat(0, 1f);
            mTempAnimator.setDuration(SINGLE_DURATION);
            mTempAnimator.setStartDelay(SINGLE_INTERVAL);
            mTempAnimator.setInterpolator(new OvershootInterpolator());
            final int finalI = i;
            mTempAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    tempColum.setAnimLength(tempColum.getLength() * (float) animation.getAnimatedValue());
                    tempColum.setStrAlpha((int) (153 * (float) animation.getAnimatedValue()));

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
        //清空单个数据
        for (ParrotPillar temp : mParrotPillars) {
            temp.setAnimLength(0);
        }
        mNowAnimIndex = 0;
        mCircleAnimator.start();
    }

    //循环操作动画
    private void startLoopAnim() {
        //  NgLogUtils.INSTANCE.d("startLoopAnim~~ l   o    o   p");
    }


    /**
     * 得到动画类型
     *
     * @param newData 新数据
     * @param oldData 原本的数据
     * @return
     */
    private String getAnimType(ArrayList<ParrotPillar> newData, ArrayList<ParrotPillar> oldData) {

        if (Utils.isEmpty(oldData) || newData.size() != oldData.size()) {
            //    NgLogUtils.INSTANCE.d("********** getAnimType ldData为空，或数据量不等,执行数量切换动画");
            return ANIM_TYPE_NUMBER;
        }

        //  NgLogUtils.INSTANCE.d("newData:" + newData.toString());
        //  NgLogUtils.INSTANCE.d("oldData:" + oldData.toString());

        if (newData.equals(oldData)) {
            //     NgLogUtils.INSTANCE.d("********** getAnimType 相等");
            return ANIM_TYPE_NULL;
        }

        //判断是switch还是value
        Collections.sort(oldData);
        int swtichNumber = 0;
        for (int i = 0; i < newData.size(); i++) {
            if (newData.get(i).getName() != oldData.get(i).getName()) {
                swtichNumber++;
            }
        }
        if (swtichNumber == 2) {
            //   NgLogUtils.INSTANCE.d("********** getAnimType 对换切换动画");
            return ANIM_TYPE_SWITCH;
        }
        //默认全部更新
        //  NgLogUtils.INSTANCE.d("********** getAnimType 值切换动画");
        return ANIM_TYPE_VALUE;
    }

    //转化固定比率,设置透明度和颜色
    private void resetData(ArrayList<ParrotPillar> dataList) {
        //柱子最大值
        float mMaxValue = 0;
        //柱子总值shou
        float mTotalValue = 0;
        for (ParrotPillar temp : dataList) {
            if (mMaxValue < temp.getValue().floatValue()) {
                mMaxValue = temp.getValue().floatValue();
            }
            mTotalValue += temp.getValue().floatValue();
            //todo dakai
//            USStateItem nameItem = USStateUtil.Companion.getItemByCode(temp.getName());
//            if (nameItem != null)
//                temp.setName(nameItem.getName());
        }
        float interval;
        int tempColumn = dataList.size();
        float startAngle = -90f;
        float interValNum;
        if (tempColumn == 1) {
            interValNum = 0;
        } else if (tempColumn == 2) {
            interValNum = 1;
        } else {
            interValNum = tempColumn;
        }
        float tempAngle = (360f * 1f - interValNum * mInterval) / tempColumn;

        for (int i = 0; i < tempColumn; i++) {
            ParrotPillar temp = dataList.get(i);
            //得到当前颜色和透明度
            int colorLenght = mColors.length / 2;//7
            int tempIndex = i + 1;
            int tens = tempIndex / colorLenght;
            int alpha = 100 - tens * 10;
            if (alpha < 50) {
                alpha = 50;
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
            //设置固定比率
            float ratio;
            if (i == 0) {
                ratio = 1f;
            } else if (i < tempColumn / 8f) {    // 2/3
                interval = 0.666f / (tempColumn / 8f);
                ratio = 1f - i * interval;

            } else if (i < tempColumn / 4f) {       // 1/6
                interval = 0.166f / (tempColumn / 4f);
                ratio = 0.333f - (i - tempColumn / 8f) * interval;

            } else {                             // 1/6
                interval = 0.15f / (tempColumn * 5 / 8);
                ratio = 0.166f - (i - tempColumn * 5 / 8) * interval;
            }

            if (ratio < 0.02) {
                ratio = 0.02f;
            }
            temp.setRatio(ratio);
            temp.setLength(mCenterR + mMaxLength * temp.getRatio());

            //设置每一个的初始角度
            temp.setStartAngle(startAngle);
            startAngle += (tempAngle + (tempColumn > 1 ? mInterval : 0));
        }

    }

    //初始化参数
    private void resetParameter(ArrayList<ParrotPillar> dataList) {
        mColumn = dataList.size();
        mNowAnimIndex = 0;
        //这里要多算一点不然跟不上。。可能是动画启动耗费的时间吧orz
        SINGLE_INTERVAL = DURATION / (long) (mColumn * 2);
        //得到单个角度
        getAngle();
    }

    //验证数据正确性
    private boolean isDataError(ArrayList<ParrotPillar> parrotPillars) {
        //null
        if (Utils.isEmpty(parrotPillars)) {
            return true;
        }
        //包含name空
        for (ParrotPillar temp : parrotPillars) {
            if (Utils.isEmpty(temp.getName())) {
                return true;
            }
        }
        return false;
    }

    //得到单个弧度
    private void getAngle() {
        float interValNum;
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
        stopAllAnim();
        if (loopHandler != null) {
            loopHandler.removeCallbacksAndMessages(null);
            loopHandler = null;
        }
        super.onDetachedFromWindow();
    }

    private volatile boolean isLoop = false;
    private LoopHandler loopHandler;

    //采用弱引用防止内存泄漏
    private static final class LoopHandler extends Handler {
        private WeakReference<ParrotView> parrotViewWeakReference;

        private LoopHandler(ParrotView clockView) {
            parrotViewWeakReference = new WeakReference<>(clockView);
        }

        @Override
        public void handleMessage(@NotNull Message msg) {
            ParrotView view = parrotViewWeakReference.get();
            if (view != null) {
                if (view.getVisibility() == View.VISIBLE) {
                    //只有可见的情况下才执行loop
                    view.startLoop();
                }
                //loop
                long LOOP_DURATION = 10 * 1000;
                sendEmptyMessageDelayed(1, LOOP_DURATION);//每10秒一轮回
            }
        }
    }

    //开始循环
    private void startLoop() {

        if (isDataError(mParrotPillars) || isAnimRunning) {
            return;
        }
        startLoopAnim();
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
        if (!Utils.isEmpty(mValueAnimatorList)) {
            for (Animator temp : mValueAnimatorList) {
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
        if (mSwitchAnimator != null) {
            if (visibility == View.VISIBLE) {
                mSwitchAnimator.resume();
            } else {
                mSwitchAnimator.pause();
            }
        }
    }

    private void stopAllAnim() {
        if (!Utils.isEmpty(mAnimatorList)) {
            for (Animator temp : mAnimatorList) {
                temp.cancel();
            }
            mAnimatorList.clear();
        }
        if (!Utils.isEmpty(mValueAnimatorList)) {
            for (Animator temp : mValueAnimatorList) {
                temp.cancel();
            }
            mValueAnimatorList.clear();
        }
        if (mCircleAnimator != null) {
            mCircleAnimator.cancel();
        }
        if (mSwitchAnimator != null) {
            mSwitchAnimator.cancel();
        }
        isAnimRunning = false;
    }


    private String getHexString(int color) {
        String s = "#";
        int colorStr = (color & 0xff000000) | (color & 0x00ff0000) | (color & 0x0000ff00) | (color & 0x000000ff);
        s = s + Integer.toHexString(colorStr);
        return s;
    }

}









