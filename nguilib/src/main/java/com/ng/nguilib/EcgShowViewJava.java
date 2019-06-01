package com.ng.nguilib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @ProjectName: SdkEamples
 * @Package: com.zjw.sdkeamples.test
 * @Description:
 * @Author: Eden
 * @CreateDate: 2019/5/17 9:05
 */
public class EcgShowViewJava extends View {

    private int SHOW_MODEL = 0;
    public static final int SHOW_MODEL_ALL = 0x00;
    public static final int SHOW_MODEL_DYNAMIC_SCROLL = 0x01;
    public static final int SHOW_MODEL_DYNAMIC_REFRESH = 0x02;

    private float mWidth;
    private float mHeight;
    private Paint paint;
    private Path path;
    private String[] dataStrList;

    private int scrollIndex = 0;
    Timer timer;
    TimerTask timerTask;
    private static final float INTERVAL_SCROLL_REFRESH = 80f;

    private static List<Float> refreshList;
    private int showIndex;

    private static float MAX_VALUE = 20f;
    private int intervalNumHeart;
    private float intervalRowHeart;
    private float intervalColumnHeart;
    private static float HEART_LINE_STROKE_WIDTH = 5f;
    private float[] data;
    private float mHeartLinestrokeWidth;

    private static float GRID_LINE_STROKE_WIDTH = 3f;
    private static float GRID_WIDTH_AND_HEIGHT = 10f;
    private int row;
    private float intervalRow;
    private int column;
    private float intervalColumn;
    private float mGridLinestrokeWidth;
    private float mGridstrokeWidthAndHeight;


    public EcgShowViewJava(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        path = new Path();
    }

    public EcgShowViewJava(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mGridLinestrokeWidth = dip2px(GRID_LINE_STROKE_WIDTH);
        mGridstrokeWidthAndHeight = dip2px(GRID_WIDTH_AND_HEIGHT);

        column = (int) (mWidth / mGridstrokeWidthAndHeight);
        intervalColumn = mWidth / column;
        row = (int) (mHeight / mGridstrokeWidthAndHeight);
        intervalRow = mHeight / row;

        mHeartLinestrokeWidth = dip2px(HEART_LINE_STROKE_WIDTH);
        initData();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawGrid(canvas);

        switch (SHOW_MODEL) {
            case SHOW_MODEL_ALL:
                drawHeartAll(canvas);
                break;
            case SHOW_MODEL_DYNAMIC_SCROLL:
                drawHeartScroll(canvas);
                break;
            case SHOW_MODEL_DYNAMIC_REFRESH:
                drawHeartRefresh(canvas);
                break;

        }

    }

    public void showLine(float point) {
        if (refreshList == null) {
            refreshList = new ArrayList<>();
            data = new float[intervalNumHeart];
        }
        refreshList.add(point);

        postInvalidate();
    }


    private void drawHeartRefresh(Canvas canvas) {
        paint.reset();
        path.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#31CE32"));
        paint.setStrokeWidth(mGridLinestrokeWidth);
        paint.setAntiAlias(true);
        path.moveTo(0, mHeight / 2);


        int nowIndex = refreshList==null?0:refreshList.size();

        if (nowIndex == 0) {
            return;
        }

        if (nowIndex < intervalNumHeart) {
            showIndex = nowIndex - 1;
        } else {
            showIndex = (nowIndex - 1) % intervalNumHeart;
        }


        for (int i = 0; i < intervalNumHeart; i++) {
            if (i > refreshList.size() - 1) {
                break;
            }
            if (nowIndex <= intervalNumHeart) {
                data[i] =  refreshList.get(i);
            } else {
                int times = (nowIndex - 1) / intervalNumHeart;

                int temp = times * intervalNumHeart + i;

                if (temp < nowIndex) {
                    data[i] = refreshList.get(temp);
                }
            }
        }

        float nowX;
        float nowY;
        for (int i = 0; i < data.length; i++) {
            nowX = i * intervalRowHeart;
            float dataValue = data[i];
            if (dataValue > 0) {
                if (dataValue > (MAX_VALUE * 0.8f)) {
                    dataValue = (MAX_VALUE * 0.8f);
                }
            } else {
                if (dataValue < (-MAX_VALUE * 0.8f)) {
                    dataValue = -(MAX_VALUE * 0.8f);
                }
            }
            nowY = mHeight / 2 - (dataValue * intervalColumnHeart);

            if ((i - 1) == showIndex) {
                path.moveTo(nowX, nowY);

            } else {
                path.lineTo(nowX, nowY);
            }

        }

        canvas.drawPath(path, paint);
    }

    private void drawHeartScroll(Canvas canvas) {
        if (data == null || data.length == 0) {
            return;
        }
        paint.reset();
        path.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#31CE32"));
        paint.setStrokeWidth(mGridLinestrokeWidth);
        paint.setAntiAlias(true);
        path.moveTo(0, mHeight / 2);

        int scrollStartIndex = 0;
        int scrollEndIndex = 0;

        scrollEndIndex = scrollIndex;

        scrollStartIndex = scrollEndIndex - intervalNumHeart;
        if (scrollStartIndex < 0) {
            scrollStartIndex = 0;
        }

        float nowX;
        float nowY;
        for (int i = scrollStartIndex; i < scrollEndIndex; i++) {
            nowX = (i - scrollStartIndex) * intervalRowHeart;

            float dataValue = data[i];
            if (dataValue > 0) {
                if (dataValue > (MAX_VALUE * 0.8f)) {
                    dataValue = (MAX_VALUE * 0.8f);
                }
            } else {
                if (dataValue < (-MAX_VALUE * 0.8f)) {
                    dataValue = -(MAX_VALUE * 0.8f);
                }
            }
            nowY = mHeight / 2 - (dataValue * intervalColumnHeart);
            path.lineTo(nowX, nowY);
            LogUtils.d("drawHeartScroll " + nowX + " " + nowY);
        }

        canvas.drawPath(path, paint);
        postInvalidate();

    }

    private void drawHeartAll(Canvas canvas) {
        if (data == null || data.length == 0) {
            return;
        }
        paint.reset();
        path.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#31CE32"));
        paint.setStrokeWidth(mGridLinestrokeWidth);
        paint.setAntiAlias(true);
        path.moveTo(0, mHeight / 2);
        float nowX;
        float nowY;
        for (int i = 0; i < data.length; i++) {
            nowX = i * intervalRowHeart;
            float dataValue = data[i];
            if (dataValue > 0) {
                if (dataValue > (MAX_VALUE * 0.8f)) {
                    dataValue = (MAX_VALUE * 0.8f);
                }
            } else {
                if (dataValue < (-MAX_VALUE * 0.8f)) {
                    dataValue = -(MAX_VALUE * 0.8f);
                }
            }
            nowY = mHeight / 2 - (dataValue * intervalColumnHeart);
            path.lineTo(nowX, nowY);
        }

        canvas.drawPath(path, paint);

    }

    private void drawGrid(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#D8D8D8"));
        paint.setStrokeWidth(mGridLinestrokeWidth);
        paint.setAntiAlias(true);
        for (int i = 0; i <= column; i++) {
            float iTempC = i * intervalColumn;
            path.moveTo(iTempC, 0);
            path.lineTo(iTempC, mHeight);
        }
        for (int i = 0; i <= row; i++) {
            path.moveTo(0, i * intervalRow);
            path.lineTo(mWidth, i * intervalRow);
        }
        canvas.drawPath(path, paint);
    }

    public void setData(String dataStr, int model) {
        if (dataStr != null)
            dataStrList = dataStr.split(",");
        //当前模式
        this.SHOW_MODEL = model;
        initData();
    }


    private void initData() {
        LogUtils.d(Thread.currentThread().getName() + "initData: " + SHOW_MODEL);
        int dataLength;
        switch (SHOW_MODEL) {
            case SHOW_MODEL_ALL:
                dataLength = dataStrList.length;

                if (dataLength > mWidth) {
                    dataLength = (int) mWidth;
                }
                data = new float[dataLength];
                for (int i = 0; i < dataLength; i++) {
                    data[i] = Float.parseFloat(dataStrList[i]);
                }
                intervalNumHeart = data.length;
                intervalRowHeart = mWidth / intervalNumHeart;
                intervalColumnHeart = mHeight / (MAX_VALUE * 2);
                break;
            case SHOW_MODEL_DYNAMIC_SCROLL:
                dataLength = dataStrList.length;

                data = new float[dataLength];
                for (int i = 0; i < dataLength; i++) {
                    data[i] = Float.parseFloat(dataStrList[i]);
                }
                intervalRowHeart = mWidth / dip2px(INTERVAL_SCROLL_REFRESH);
                intervalNumHeart = (int) (mWidth / intervalRowHeart);
                intervalColumnHeart = mHeight / (MAX_VALUE * 2);
                startScrollTimer();

                break;
            case SHOW_MODEL_DYNAMIC_REFRESH:


                intervalRowHeart = mWidth / dip2px(INTERVAL_SCROLL_REFRESH);
                intervalNumHeart = (int) (mWidth / intervalRowHeart);
                intervalColumnHeart = mHeight / (MAX_VALUE * 2);

                break;
        }
        LogUtils.d(Thread.currentThread().getName() +"initDataEnd : " + intervalColumnHeart);

    }

    private void startScrollTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (scrollIndex < data.length) {
                    scrollIndex++;
                } else {
                    scrollIndex = 0;
                }
            }
        };
        timer.schedule(timerTask, 0, 50);
    }


    private int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
