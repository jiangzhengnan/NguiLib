package com.ng.ui.testsoundview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.ng.ui.R;

import java.util.ArrayList;

/**
 * Created by zhaocheng on 2016/11/3.
 */

public class SoundWiveView extends View {
    //View默认最小宽度
    private static final int DEFAULT_MIN_WIDTH = 500;
    private static final int LINE_SIZE = 10;
    private final DashPathEffect pathEffect;
    private final CornerPathEffect pathEffect2;
    private int voiceLineColor;
    //圆环的边距
    private int pandding = 10;
    //圆环的宽度
    private int widthing = 5;
    private Context mContext;
    private Paint mPaint;
    private final String TAG = "RecordView";
    private double r;
    private float translateX = 0;
    /**
     * 振幅
     */
    private float amplitude = 1;
    /**
     * 音量
     */
    private float volume = 10;
    private int fineness = 1;
    private float targetVolume = 1;
    private float maxVolume = 100;
    private boolean isSet = false;
    /**
     * 灵敏度
     */
    private int sensibility = 4;
    private boolean canSetVolume = false;
    private ArrayList<Path> paths;
    private ValueAnimator degreeValueAnimator;

    public SoundWiveView(Context context) {
        this(context, null);
    }

    public SoundWiveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        paths = new ArrayList<>(LINE_SIZE);
        for (int i = 0; i < LINE_SIZE; i++) {
            paths.add(new Path());
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStyle(Paint.Style.STROKE);
        pathEffect = new DashPathEffect(new float[]{1, 2}, 2);
        pathEffect2 = new CornerPathEffect(80);
        voiceLineColor = Color.parseColor("#45C3E5");
    }

    /**
     * 当布局为wrap_content时设置默认长宽
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }

    private int measure(int origin) {
        int result = DEFAULT_MIN_WIDTH;
        int specMode = MeasureSpec.getMode(origin);
        int specSize = MeasureSpec.getSize(origin);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("tttsttt", "111start");
        lineChange();
        drawVoiceLine2(canvas, translateX);
       // drawVoiceLine2(canvas, translateX + 20);
        Log.e("tttsttt", "111end");
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 画声纹（播放）
     */
    private void drawVoiceLine2(Canvas canvas, float x) {
        mPaint.setColor(voiceLineColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        canvas.save();
        int moveY = getHeight() / 2;
        int pandY = getWidth() / 12;
        for (int i = 0; i < paths.size(); i++) {
            paths.get(i).reset();
            paths.get(i).moveTo(getWidth() - pandY, getHeight() / 2);
        }
        for (float j = getWidth() * 11 / 12 - 1; j >= getWidth() / 12; j -= fineness) {
            float i = j - getWidth() / 12;
            //这边必须保证起始点和终点的时候amplitude = 0;
            //amplitude 振幅
            amplitude = 4 * volume * i / getWidth() - 4 * volume * i / getWidth() * i / getWidth() * 12 / 10;
            for (int n = 1; n <= paths.size(); n++) {
                float sin = amplitude * (float) Math.sin((i - Math.pow(1.22, n)) * Math.PI / 180 - x);
                paths.get(n - 1).lineTo(j, (2 * n * sin / paths.size() - 15 * sin / paths.size() + moveY));
            }
        }


        for (int n = 0; n < paths.size(); n++) {
            if (n == paths.size() - 1) {
                mPaint.setAlpha(255);
                mPaint.setPathEffect(pathEffect2);

            } else {
                mPaint.setPathEffect(pathEffect);
                mPaint.setAlpha(n * 130 / paths.size());
            }
            if (mPaint.getAlpha() > 0) {
                canvas.drawPath(paths.get(n), mPaint);
            }
        }
        canvas.restore();
    }

    public void start() {
        canSetVolume = true;
        degreeValueAnimator = ValueAnimator.ofFloat(0, 1000);
        degreeValueAnimator.setInterpolator(new LinearInterpolator());
        degreeValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                translateX = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        long duartion = 1000 * 150;
        if (duartion < 800) {
            duartion = 800;
        }

        degreeValueAnimator.setDuration(duartion);
        degreeValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }
        });
        degreeValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        degreeValueAnimator.start();
    }


    private void lineChange() {
        if (volume < targetVolume && isSet) {
            volume++;
        } else {
            isSet = false;
            if (volume <= 10) {
                volume = 10;
            } else {
                if (volume < getHeight() / 30) {
                    volume--;
                } else {
                    volume--;
                }
            }
        }
    }

    public void setVolume(int volume) {
        if (volume > 100)
            volume = volume / 100;
        volume = volume * 2 / 5;
        if (!canSetVolume)
            return;
        if (volume > maxVolume * sensibility / 30) {
            isSet = true;
            this.targetVolume = getHeight() * volume / 2 / maxVolume;
            Log.d(TAG, "targetVolume: " + targetVolume);
        }
    }


    public void cancel() {
        canSetVolume = false;
        targetVolume = 1;
        postInvalidate();
        if (degreeValueAnimator != null) {
            degreeValueAnimator.cancel();
        }
    }

    public boolean getPlayStatus() {
        return canSetVolume;
    }
}
