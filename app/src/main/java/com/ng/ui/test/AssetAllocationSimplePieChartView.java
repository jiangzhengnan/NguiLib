package com.ng.ui.test;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.ng.nguilib.utils.DensityUtil;


/**
 * 描述:去掉圆角，去掉渐变色，宽度6dp 间隙6-8dp  颜色 nc401 nc201 nc216 nc203
 *
 * @author Jzn
 * @date 2019-12-09
 */
public class AssetAllocationSimplePieChartView extends View {


    private float mCenterX, mCenterY;
    private Paint mPaint;
    private int mProgressWidth;
    private RectF mRectFProgressArc;
    private int mPadding;
    private int colorStart;
    private int colorEnd;
    private SimpePieChartData[] mDatas;
    private boolean mIsLargerMax;
    private ViewModel[] mViewModel;

    public AssetAllocationSimplePieChartView(Context context) {
        this(context, null);
    }

    public AssetAllocationSimplePieChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AssetAllocationSimplePieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        mProgressWidth = dp2px(6f);
        mRectFProgressArc = new RectF();

        colorStart = Color.argb(255, 0x5c, 0xea, 0xf5);
        colorEnd = Color.argb(255, 0x12, 0xb0, 0xff);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mPadding = Math.max(
                Math.max(getPaddingLeft(), getPaddingTop()),
                Math.max(getPaddingRight(), getPaddingBottom())
        );


        mPadding = mPadding + dp2px(10f);
        int width = resolveSize(DensityUtil.INSTANCE.dip2px(getContext(), 160), widthMeasureSpec);

        setMeasuredDimension(width, width);

        mCenterX = mCenterY = getMeasuredWidth() / 2f;


        mRectFProgressArc.set(
                mPadding,
                mPadding,
                getMeasuredWidth() - mPadding,
                getMeasuredWidth() - mPadding
        );
    }


    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        if (mViewModel != null && mViewModel.length > 0) {
            for (int i = 0; i < mViewModel.length; i++) {
                drawwideRec(canvas, mViewModel[i]);
            }
        }

    }

    private void drawwideRec(Canvas canvas, ViewModel model) {
        if (mIsLargerMax) {
            if (model.isMax) {
                mProgressWidth = dp2px(14f);
            } else {
                mProgressWidth = dp2px(10f);
            }
        }
        /**
         * 画进度圆弧背景
         */
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mProgressWidth);
        canvas.drawArc(mRectFProgressArc, model.startAngle + 1, model.sweepAngle - 2, false, mPaint);

        mPaint.setAlpha(255);
        /**
         * 画进度圆弧(起始到信用值)
         */
        if (model.sweepAngle < 5) {
            mPaint.setShader(null);
            mPaint.setColor(model.colorEnd);
        } else {
            mPaint.setShader(generateSweepGradient(model.startAngle, model.sweepAngle, model.colorStart, model.colorEnd));
        }

        canvas.drawArc(mRectFProgressArc, model.startAngle + 1,
                model.sweepAngle, false, mPaint);
    }

    private SweepGradient generateSweepGradient(float angle, float sweepAngle, int startColor, int endColor) {
        SweepGradient sweepGradient = new SweepGradient(mCenterX, mCenterY,
                new int[]{startColor, endColor},
                new float[]{0, (float) (sweepAngle + 7) / 360}
        );
        Matrix matrix = new Matrix();
        matrix.setRotate(angle - 7, mCenterX, mCenterY);
        sweepGradient.setLocalMatrix(matrix);

        return sweepGradient;
    }

    public void setCapButt() {
        mPaint.setStrokeCap(Paint.Cap.BUTT);
    }

    public void setData(SimpePieChartData[] datas, boolean isLargerMax) {
        this.mIsLargerMax = isLargerMax;
        mDatas = datas;
        mViewModel = calcViewModel(datas);
        invalidate();
    }

    private ViewModel[] calcViewModel(SimpePieChartData[] datas) {
        if (datas == null || datas.length == 0) {
            return null;
        }

        float account = 0;
        ViewModel[] models = new ViewModel[datas.length];

        int maxIndex = 0;
        float maxValue = datas[0].value;
        for (int i = 0; i < datas.length; i++) {
            account += datas[i].value;
            if (datas[i].value > maxValue) {
                maxValue = datas[i].value;
                maxIndex = i;
            }
        }
        if (account == 0) {
            return null;
        }

        int startAngle = 270;
        int split = 14;
        if (mPaint.getStrokeCap() == Paint.Cap.BUTT) {
            split = 4;
        }
        int max = 360 - datas.length * split;
        for (int i = 0; i < datas.length; i++) {
            ViewModel viewModel = new ViewModel();
            if (i == maxIndex) {
                viewModel.isMax = true;
            }
            viewModel.colorStart = datas[i].colorStar;
            viewModel.colorEnd = datas[i].colorEnd;
            viewModel.startAngle = startAngle;
            viewModel.sweepAngle = Math.max(1, datas[i].value * max / account);

            startAngle += viewModel.sweepAngle + split;
            models[i] = viewModel;
        }
        return models;
    }

    private class ViewModel {
        public float startAngle;
        public float sweepAngle;
        public int colorStart;
        public int colorEnd;
        public boolean isMax;
    }


    public static class SimpePieChartData {
        public float value;
        public int colorStar;
        public int colorEnd;

        public SimpePieChartData(float value, int colorStar, int colorEnd) {
            this.value = value;
            this.colorStar = colorStar;
            this.colorEnd = colorEnd;
        }
    }
}
