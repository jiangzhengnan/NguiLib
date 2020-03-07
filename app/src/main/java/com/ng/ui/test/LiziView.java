package com.ng.ui.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.ng.nguilib.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-03-06
 */
public class LiziView extends View {
    private Context mContext;
    private Timer timer;
    private TimerTask timerTask;
    private float mWidth;
    private float mHeight;


    private long PERIOD = 1000 / (60);

    private List<Lizi> mPointList = Collections.synchronizedList(new ArrayList<>());

    private boolean isRunning = false;

    public void startAnim() {
        if (!isRunning) {
            isRunning = true;
            timer.schedule(timerTask, 0, PERIOD);

        }
    }

    public void stopAnim() {
        isRunning = false;
        timer.cancel();
    }

    //中心点坐标
    private float mCenterX;
    private float mCenterY;

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;

        if (mWidth != 0 && mHeight != 0) {
            mPointList.add(new Lizi(mWidth, mHeight));
            mRadialGradient = new RadialGradient(
                    mCenterX,
                    mHeight - 50,
                    mHeight,
                    Color.parseColor("#ec478c"),
                    Color.TRANSPARENT,
                    Shader.TileMode.CLAMP);
        }


    }

    public LiziView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LiziView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private Path mPath;
    private Paint mPaint;
    //透明度遮罩
    private RadialGradient mRadialGradient;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        for (int i = 0; i < mPointList.size(); i++) {
            Lizi temp = mPointList.get(i);

            mPath.addCircle(temp.x, temp.y, temp.size, Path.Direction.CCW);
            //透明度?

        }
        mPaint.setShader(mRadialGradient);
        canvas.drawPath(mPath, mPaint);
    }

    private void init(Context context) {
        mContext = context;
        //关闭硬件加速，否则遮罩会有问题
        this.setLayerType(LAYER_TYPE_SOFTWARE, null);

        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(false);//消除锯齿
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(dp2px(2));
        //mPaint.setColor(Color.WHITE);
        //mPaint.setAlpha(200);

        mPaint.setColor(Color.parseColor("#ec478c"));

        timer = new Timer();


        timerTask = new TimerTask() {
            @Override
            public void run() {
                LogUtils.INSTANCE.d("run " + mPointList.size());
                //create    1-10个
                if (mPointList.size() < 500) {
                    int addNum = new Random().nextInt(10) + 1;
                    for (int i = 0; i < addNum; i++) {
                        mPointList.add(new Lizi(mWidth, mHeight));
                    }
                }

                //running
                refreshAllPoint();

            }
        };
    }

    private void refreshAllPoint() {
        Iterator<Lizi> i = mPointList.iterator();
        while (i.hasNext()) {
            Lizi temp = i.next();
            temp.refresh();
            //判断是否死掉了，死掉了就移出去
            if (temp.isDead) {
                i.remove();
            }
        }
        postInvalidate();
    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
