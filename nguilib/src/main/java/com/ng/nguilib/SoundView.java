package com.ng.nguilib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2019-12-14
 */
public class SoundView extends View {
    private Paint mPaint;
    private float mHeight;
    private float mWidth;


    public SoundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(dip2px(2));
    }

    public SoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawVoiceLine(canvas, 0);
    }

    //小球间距
    float space = dip2px(1);
    //振幅
    private float amplitude = 1;
    //音量
    private float volume = 50;

    //y=Asin(ωx+φ)+k
    private void drawVoiceLine(Canvas canvas, float xOffset) {

        for (int j = (int) mWidth; j >= 1; j -= 1) {
            //这边必须保证起始点和终点的时候amplitude = 0;
            //amplitude 振幅
            amplitude = dip2px(20);

            float sin = amplitude * (float) Math.sin(j - xOffset) + mHeight / 2;

            canvas.drawPoint(j, sin, mPaint);

        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
