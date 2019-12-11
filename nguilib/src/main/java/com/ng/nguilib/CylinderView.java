package com.ng.nguilib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.ng.nguilib.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @ProjectName: NGUI
 * @Package: com.ng.nguilib.ball
 * @Description:
 * @Author: Pumpkin
 * @CreateDate: 2019/11/24
 */
public class CylinderView extends View {


    //区域百分比
    private ArrayList<Entry> mEntries;

    //饼图的paint
    private Paint mainPaint;

    //点击事件的计算
    private float centerX;
    private float centerY;


    public CylinderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public CylinderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    //初始化 画笔
    private void initPaint() {
        mainPaint = new Paint();
        mainPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mainPaint.setAntiAlias(true);
    }

    public void setData(ArrayList<Entry> data) {
        mEntries = data;
        Collections.sort(mEntries, new MyCompare());
        LogUtils.INSTANCE.d("****传入的数据: " + mEntries.toString());
        postInvalidate();
    }

    //饼图的宽高
    private int areaWidth = 300;

    private int areaHight = 410;
    int areaX = 1;
    int areaY = 300;
    private int thickness = 200;

    // x =  y + sinα * R
    //  R圆的直径
    //  y是厚度
    //  x是视觉高度 即areaHight
    // 这里 指定直径R = 300   厚度Y = 200 角度=45度
    //则 areaHight =   200 + sin45 * 300
    // = 200 + 0.7*300 = 350


    private static final int LEFT = 0x01;
    private static final int RIGHT = 0x02;

    private int NOW_TAG = LEFT;

    float leftAngle = 0;
    float rightAngle = 0;

    //1.排序
    //2. 绘制
    // 0度右边边， 顺时针画
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mEntries == null) {
            return;
        }

        leftAngle = 0;
        rightAngle = 0;

        RectF tempRectF = new RectF(areaX, areaY, areaX + areaWidth, areaHight);
        int max = mEntries.size();
        for (int i = 0; i < max; i++) {
            Collections.sort(mEntries, new MyCompare());


            mainPaint.setStyle(Paint.Style.FILL);

            if (mEntries.size() == 0) {
                break;
            }


            float tempAngle = mEntries.get(0).percent;
            mainPaint.setColor(mEntries.get(0).color);

            float halfAngle = tempAngle / 2;


            /**
             * 1.算法，先绘制最大的一块，居中
             * 2.剩下的 优先记录左边和右边的坐标值   优先添加不会溢出的
             *      270
             * 180        0
             *       90
             */
            if (i == 0) {
                leftAngle = 270f - halfAngle;
                rightAngle = 270f + halfAngle;
                if (rightAngle >= 360) {
                    rightAngle -= 360f;
                }
                NOW_TAG = LEFT;
                mEntries.remove(0);

            } else {
                //1.找到当前小的边

                if (getDistanceToCenter(leftAngle, LEFT) > getDistanceToCenter(rightAngle, RIGHT)) {
                    //左边比右边大，取右边
                    NOW_TAG = RIGHT;
                    tempAngle = getNextAngle(getDistanceToCenter(rightAngle, RIGHT));
                    rightAngle += tempAngle;
                    if (rightAngle >= 360) {
                        rightAngle -= 360f;
                    }

                } else {
                    //右边比左边大，取左边
                    NOW_TAG = LEFT;
                    tempAngle = getNextAngle(getDistanceToCenter(leftAngle, LEFT));

                    leftAngle -= tempAngle;


                }

            }



            //绘制各个弧度
            int perThickness = (int) ((tempAngle / 360f) * thickness);
            for (int j = 0; j <= perThickness; j++) {
                tempRectF = new RectF(areaX, areaY - j, areaX + areaWidth, areaHight - j);

                if (i == 0) {
                    canvas.drawArc(tempRectF, leftAngle, tempAngle, true, mainPaint);
                    if (j == perThickness) {
                        mainPaint.setStyle(Paint.Style.STROKE);
                        mainPaint.setColor(Color.rgb(255, 255, 255));
                        tempRectF = new RectF(areaX, areaY - j, areaX + areaWidth, areaHight - j);
                    }
                } else {

                    switch (NOW_TAG) {
                        case LEFT:
                            canvas.drawArc(tempRectF, leftAngle, tempAngle, true, mainPaint);

                            if (j == perThickness) {
                                mainPaint.setStyle(Paint.Style.STROKE);
                                mainPaint.setColor(Color.rgb(255, 255, 255));
                                canvas.drawArc(tempRectF, leftAngle, tempAngle, true, mainPaint);
                            }
                            break;
                        case RIGHT:
                            canvas.drawArc(tempRectF, rightAngle - tempAngle, tempAngle, true, mainPaint);
                            if (j == perThickness) {
                                mainPaint.setStyle(Paint.Style.STROKE);
                                mainPaint.setColor(Color.rgb(255, 255, 255));
                                canvas.drawArc(tempRectF, rightAngle - tempAngle, tempAngle, true, mainPaint);
                            }
                            break;
                    }
                }

            }

        }


    }

    private float getNextAngle(float distanceAngle) {

        float tempAngle;
        if (mEntries.size() == 0) {
            return 0;
        }
        tempAngle = mEntries.get(0).percent;

        if (mEntries.size() == 1) {
            mEntries.remove(0);
        } else {

            float allAngle = tempAngle + distanceAngle;
            if (allAngle <= 180f) {
                //如果加起来不大于180f
                mEntries.remove(0);
            } else {
                //如果加起来大于180f，则一直找，找到不大于的位置
                int tempindex = 0;

                while (tempindex < mEntries.size()) {
                    tempindex++;


                    if (tempindex < mEntries.size() && mEntries.get(tempindex).percent + distanceAngle <= 180f) {
                        tempAngle = mEntries.get(tempindex).percent;
                        mEntries.remove(tempindex);
                    }
                }
                mEntries.remove(0);
            }
        }
        return tempAngle;
    }

    //得到相对中心位置的绝对距离
    private float getDistanceToCenter(float angle, int type) {

        float result = 0;
        switch (type) {
            case LEFT:
                if (90f <= angle && angle <= 270f) {
                    //90-270
                    result = 270f - angle;
                    break;
                }

                if (angle <= 360f) {
                    //270-360
                    result = 270f + (angle - 270f);
                    break;
                } else {
                    //0 - 90
                    result = 180f + (90f - angle);
                    break;
                }

            case RIGHT:

                if (90f <= angle && angle <= 270f) {
                    //90-270
                    result = angle + 90f;
                    break;
                }

                if (angle <= 90f) {
                    //0 - 90
                    result = angle + 90f;
                    break;
                } else {
                    //270-360
                    result = angle - 270f;
                    break;
                }
        }


        return result;

    }

    class MyCompare implements Comparator<Entry> {

        @Override
        public int compare(Entry o1, Entry o2) {
            return Float.compare(o2.percent, o1.percent);
        }
    }

    public static class Entry {
        float percent;
        int color;

        public Entry(float percent, int color) {
            this.percent = percent;
            this.color = color;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "percent=" + percent +
                    ", color=" + color +
                    '}';
        }
    }
}
