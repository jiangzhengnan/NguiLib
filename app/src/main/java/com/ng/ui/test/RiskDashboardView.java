package com.ng.ui.test;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Jamin at 2019-11-20
 */

public class RiskDashboardView extends View {


    public static final int LEVEL_SAFE = 0;
    public static final int LEVEL_CAUTION = 1;
    public static final int LEVEL_AT_RISK = 2;
    private int mRadius; // 画布边缘半径（去除padding后的半径）
    private int mStartAngle = 145; // 起始角度
    private int mSweepAngle = 270; // 绘制角度
    private int mMin = 0; // 最小值
    private int mMax = 240; // 最大值
    private int mSection = 10; // 值域（mMax-mMin）等分份数
    private int mPortion = 4; // 一个mSection等分份数
    private String mHeaderText = "Caution"; // 表头
    private int mRiskValue = 650; // 设置的值
    private int mSolidRiskValue = mRiskValue; // 信用分(设定后不变)
    private int mSparkleWidth; // 亮点宽度
    private int mProgressWidth; // 进度圆弧宽度
    private float mLength1; // 刻度顶部相对边缘的长度
    private int mCalibrationWidth; // 刻度圆弧宽度
    private float mLength2; // 刻度读数顶部相对边缘的长度

    private int mPadding;
    private float mCenterX, mCenterY; // 圆心坐标
    private Paint mPaint;
    private RectF mRectFProgressArc;
    private RectF mRectFCalibrationFArc;
    private RectF mRectFTextArc;
    private Path mPath;
    private Rect mRectText;
    private boolean isAnimFinish = true;
    private boolean isFirstVisible = true;

    private float mAngleWhenAnim;
    private int colorStart;
    private int colorMid;
    private int colorEnd;
    private int colorYelloe;
    private int colorGray;
    private int colorTxt;

    private String[] strings = new String[]{"Safe", "Caution", "AtRisk"};
    private int mCurrentLevel;

    public RiskDashboardView(Context context) {
        this(context, null);
    }

    public RiskDashboardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RiskDashboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mSparkleWidth = dp2px(10);
        mProgressWidth = dp2px(3);
        mCalibrationWidth = dp2px(22);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mRectFProgressArc = new RectF();
        mRectFCalibrationFArc = new RectF();
        mRectFTextArc = new RectF();
        mPath = new Path();
        mRectText = new Rect();

        colorStart = Color.argb(255, 0xd, 0xcf, 0x99);
        colorMid = Color.argb(255, 0xf1, 0xc0, 0x2c);
        colorEnd = Color.RED;
        colorYelloe = Color.argb(255, 0xe3, 0xc1, 0x33);
        //colorGray = Color.argb(255, 0x2b, 0x32, 0x4b);
        colorGray = Color.GRAY;
        colorTxt = Color.RED;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mPadding = Math.max(
                Math.max(getPaddingLeft(), getPaddingTop()),
                Math.max(getPaddingRight(), getPaddingBottom())
        );
        setPadding(mPadding, mPadding, mPadding, mPadding);

        mLength1 = mPadding + mSparkleWidth / 2f + dp2px(8);
        mLength2 = mLength1 + mCalibrationWidth + dp2px(1) + dp2px(5);

        int width = resolveSize(dp2px(240), widthMeasureSpec);
        mRadius = (width - mPadding * 2) / 2;

        setMeasuredDimension(width, width - dp2px(30));

        mCenterX = mCenterY = getMeasuredWidth() / 2f;
        mRectFProgressArc.set(
                mPadding + mSparkleWidth / 2f,
                mPadding + mSparkleWidth / 2f,
                getMeasuredWidth() - mPadding - mSparkleWidth / 2f,
                getMeasuredWidth() - mPadding - mSparkleWidth / 2f
        );

        mRectFCalibrationFArc.set(
                mLength1 + mCalibrationWidth / 2f,
                mLength1 + mCalibrationWidth / 2f,
                getMeasuredWidth() - mLength1 - mCalibrationWidth / 2f,
                getMeasuredWidth() - mLength1 - mCalibrationWidth / 2f
        );

        mPaint.setTextSize(sp2px(10));
        mPaint.getTextBounds("0", 0, "0".length(), mRectText);
        mRectFTextArc.set(
                mLength2 + mRectText.height(),
                mLength2 + mRectText.height(),
                getMeasuredWidth() - mLength2 - mRectText.height(),
                getMeasuredWidth() - mLength2 - mRectText.height()
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //drawwideRec(canvas);
        /**
         * 画长刻度
         * 画好起始角度的一条刻度后通过canvas绕着原点旋转来画剩下的长刻度
         */
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(dp2px(2));
        mPaint.setColor(colorGray);
        float x0 = mCenterX;
        float y0 = mPadding + mLength1 + dp2px(1);
        // 逆时针到开始处


        /**
         * 画短刻度
         * 同样采用canvas的旋转原理
         */
        mPaint.setStrokeWidth(dp2px(2));
        float x2 = mCenterX;
        float y2 = y0 + mCalibrationWidth - dp2px(2);
        mPaint.setShader(null);
        // 逆时针到开始处
        canvas.save();
        canvas.drawLine(x0, y0, x2, y2, mPaint);
        canvas.drawLine(x0, y2 + dp2px(10), x2, y2 + dp2px(15), mPaint);
        int max = mSection * mPortion / 2;
        float degree = mSweepAngle / (max * 2);
        for (int i = 0; i < max; i++) {
            canvas.rotate(-degree, mCenterX, mCenterY);
            canvas.drawLine(x0, y0, x2, y2, mPaint);
            canvas.drawLine(x0, y2 + dp2px(10), x2, y2 + dp2px(12), mPaint);
            if (i == 13) {
                canvas.drawLine(x0, y2 + dp2px(10), x2, y2 + dp2px(15), mPaint);
            }
        }
//
//        canvas.restore();
//        // 顺时针到结尾处
//        canvas.save();
//
//
//        for (int i = 0; i < max; i++) {
//            canvas.rotate(degree, mCenterX, mCenterY);
//            canvas.drawLine(x0, y0, x2, y2, mPaint);
//            canvas.drawLine(x0, y2 + dp2px(10), x2, y2 + dp2px(12), mPaint);
//            if (i == 13) {
//                canvas.drawLine(x0, y2 + dp2px(10), x2, y2 + dp2px(15), mPaint);
//            }
//        }
//        canvas.restore();
//
//        canvas.save();
//        canvas.rotate(mSweepAngle, mCenterX, mCenterY);
//        max = mSection * mPortion / 2;
//
//
//        for (int i = 0; i < (mSection * mPortion) * (mAngleWhenAnim - mStartAngle) / mSweepAngle; i++) {
//            if (i < max) {
//                mPaint.setColor(getColorChanges(colorStart, colorMid, i, max));
//            } else {
//                mPaint.setColor(getColorChanges(colorMid, colorEnd, i - max, max));
//            }
//
//            canvas.drawLine(x0, y0, x2, y2, mPaint);
//            canvas.rotate(degree, mCenterX, mCenterY);
//        }
//        canvas.drawLine(x0, y0 - dp2px(5), x2, y2, mPaint);
//        canvas.restore();


        /**
         * 画长刻度读数
         * 添加一个圆弧path，文字沿着path绘制
         */
        mPaint.setTextSize(sp2px(10));
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(colorTxt);
        canvas.drawText(getCenterText(0), mCenterX - getWidth() * 28 / 100, mCenterY - dp2px(4), mPaint);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(getCenterText(1), mCenterX, mCenterY - getHeight() * 28 / 100, mPaint);
        mPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(getCenterText(2), mCenterX + getWidth() * 28 / 100, mCenterY - dp2px(4), mPaint);

        /**
         * 画表头
         */
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(getCenterTextColor());
        mPaint.setTextSize(sp2px(18));
        canvas.drawText(getCenterText(mCurrentLevel), mCenterX, mCenterY, mPaint);
        mPaint.setColor(colorGray);

    }

    private String getCenterText(int level) {
        try {
            return strings[level];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mHeaderText;
    }

    private int getCenterTextColor() {
        int value = colorMid;
        switch (mCurrentLevel) {
            case LEVEL_SAFE:
                value = colorStart;
                break;
            case LEVEL_CAUTION:
                value = colorMid;
                break;
            case LEVEL_AT_RISK:
                value = colorEnd;
                break;

        }
        return value;
    }

//    private void drawwideRec(Canvas canvas) {
//        if (true) {
//            return;
//        }
//        /**
//         * 画进度圆弧背景
//         */
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(mProgressWidth);
//        mPaint.setAlpha(80);
//        canvas.drawArc(mRectFProgressArc, mStartAngle + 1, mSweepAngle - 2, false, mPaint);
//
//        mPaint.setAlpha(255);
//        if (isAnimFinish) {
//            /**
//             * 画进度圆弧(起始到信用值)
//             */
//            mPaint.setShader(generateSweepGradient());
//            canvas.drawArc(mRectFProgressArc, mStartAngle + 1,
//                    calculateRelativeAngleWithValue(mRiskValue) - 2, false, mPaint);
//        } else {
//            /**
//             * 画进度圆弧(起始到信用值)
//             */
//            mPaint.setShader(generateSweepGradient());
//            canvas.drawArc(mRectFProgressArc, mStartAngle + 1,
//                    mAngleWhenAnim - mStartAngle - 2, false, mPaint);
//        }
//    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().getDisplayMetrics());
    }

//    private SweepGradient generateSweepGradient() {
//        SweepGradient sweepGradient = new SweepGradient(mCenterX, mCenterY,
//                new int[]{Color.argb(255, 0xd, 0xcf, 0x99), Color.argb(255, 0xf1, 0xc0, 0x2c), Color.RED},
//                new float[]{0, 0.3f, 0.7f}
//        );
//        Matrix matrix = new Matrix();
//        matrix.setRotate(mStartAngle - 1, mCenterX, mCenterY);
//        sweepGradient.setLocalMatrix(matrix);
//
//        return sweepGradient;
//    }


    /**
     * 相对起始角度计算信用分所对应的角度大小
     */
    private float calculateRelativeAngleWithValue(int value) {
        return value;
    }


    public int getRiskValue() {
        return mRiskValue;
    }

    /**
     * 设置信用值
     *
     * @param riskValue 信用值
     */
    public void setRiskValue(int riskValue) {
        if (mSolidRiskValue == riskValue || riskValue < mMin || riskValue > mMax) {
            return;
        }

        mSolidRiskValue = riskValue;
        mRiskValue = riskValue;
        postInvalidate();
    }


    /**
     * 设置风险值   0 是安全，1 是中等，2 是危险
     *
     * @param level
     */
    public void setRiskLevel(int level) {
        if (!isAnimFinish) {
            return;
        }
        mCurrentLevel = level;
        //setRiskValueWithAnim(getLevelValue(level));
    }

    private int getLevelValue(int level) {
        int value = 0;
        switch (level) {
            case LEVEL_SAFE:
                value = 40;
                break;
            case LEVEL_CAUTION:
                value = 120;
                break;
            case LEVEL_AT_RISK:
                value = 240;
                break;

        }
        return value;
    }


    public void startRiskValueWithAnim() {
        if (isFirstVisible) {
            isFirstVisible = false;
            setRiskValueWithAnim(getLevelValue(mCurrentLevel));
        }
    }

    /**
     * 设置信用值并播放动画
     *
     * @param creditValue 信用值
     */
    public void setRiskValueWithAnim(int creditValue) {
        if (creditValue < mMin || creditValue > mMax || !isAnimFinish) {
            return;
        }

        mSolidRiskValue = creditValue;
        mRiskValue = creditValue;
        // 计算最终值对应的角度，以扫过的角度的线性变化来播放动画
        float degree = calculateRelativeAngleWithValue(mSolidRiskValue);
        ValueAnimator degreeValueAnimator = ValueAnimator.ofFloat(mStartAngle, mStartAngle + degree);
        degreeValueAnimator.setInterpolator(new DecelerateInterpolator());
        degreeValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                mAngleWhenAnim = (float) animation.getAnimatedValue();

                Log.d("nangua", "mAngleWhenAnim: " + mAngleWhenAnim);

                postInvalidate();
            }
        });

        long duartion = creditValue * 6;
        if (duartion < 800) {
            duartion = 800;
        }

        degreeValueAnimator.setDuration(duartion);
        degreeValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAnimFinish = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimFinish = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                isAnimFinish = true;
            }
        });
        degreeValueAnimator.start();
    }


    /*
     * 从color1 渐变到 color2,以下是一种颜色到另一种颜色需要执行runNum次才能彻底渐变结束；
     */
    private int getColorChanges(int cl1, int cl2, int cur, int max) {
        int R, G, B;
        // 此处逻辑功能是：需要执行3种颜色的渐变；
        int flag;
        if (cur > max) {
            flag = cur - max;
        } else {
            flag = cur;
        }
        // 颜色的渐变，应该把分别获取对应的三基色，然后分别进行求差值；这样颜色渐变效果最佳
        R = Color.red(cl1) + (Color.red(cl2) - Color.red(cl1)) * flag / max;
        G = Color.green(cl1) + (Color.green(cl2) - Color.green(cl1)) * flag
                / max;
        B = Color.blue(cl1) + (Color.blue(cl2) - Color.blue(cl1)) * flag
                / max;

        return Color.rgb(R, G, B);
    }

    public void setColorEnd(int colorEnd) {
        this.colorEnd = colorEnd;
    }

    public void setColorStart(int colorStart) {
        this.colorStart = colorStart;
    }

    public void setColorMid(int colorMid) {
        this.colorMid = colorMid;
    }

    public void setColorGray(int colorGray) {
        this.colorGray = colorGray;
    }

    public void setStrings(String[] strings) {
        this.strings = strings;
    }
}
