package com.ng.nguilib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.ng.nguilib.utils.DensityUtil;
import com.ng.nguilib.utils.LogUtils;

import org.jetbrains.annotations.NotNull;

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

    private int maxSize;

    //饼图的paint
    private Paint mainPaint;

    //点击事件的计算
    private float centerX;
    private float centerY;

    //饼图的宽-平面视角下
    private int area2DWidth = 0;
    //饼图的高-平面视角下
    private int area2DHeight = 0;

    //饼图的3d视觉高度
    private int area3DHight = 0;

    private int thickness = DensityUtil.INSTANCE.dip2px(getContext(), 200f);
    // x =  y + sinα * R
    //  R圆的直径
    //  y是厚度
    //  x是视觉高度 即areaHight
    // 这里 指定直径R = 150dp   厚度Y = 100dp 角度=45度
    //则 areaHight =   100 + sin45 * 150
    // = 100 + 0.7*150 = 205
    //所以宽为150的时候，高必须为205，才满足45度角的条件

    private static final int LEFT = 0x01;
    private static final int RIGHT = 0x02;
    private static final int CENTER = 0x03;
    private int NOW_TAG = LEFT;
    float leftAngle = 0;
    float rightAngle = 0;

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
        maxSize = data.size();
        Collections.sort(mEntries, new MyCompare());
        postInvalidate();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        leftAngle = 0;
        rightAngle = 0;
        int max = mEntries.size();
        for (int i = 0; i < max; i++) {
            Collections.sort(mEntries, new MyCompare());


            mainPaint.setStyle(Paint.Style.FILL);

            if (mEntries.size() == 0) {
                break;
            }


            float tempAngle = mEntries.get(0).percent;
            int tempColor = mEntries.get(0).color;


            float halfAngle = tempAngle / 2;

            /*
              1.算法，先绘制最大的一块，居中
              2.剩下的 优先记录左边和右边的坐标值
              3.优先添加不会溢出的
             */
            if (i == 0) {
                leftAngle = 270f - halfAngle;
                rightAngle = 270f + halfAngle;
                if (rightAngle >= 360) {
                    rightAngle -= 360f;
                }
                NOW_TAG = CENTER;
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
            int perThickness = (int) ((tempAngle / 360f) * thickness * (maxSize / 2));
            float drawTempStartAngle = 0f;
            RectF tempRectF;

            float lineStartX = 0f;
            float lineStartY = 0f;
            float lineEndX = 0f;
            float lineEndY = 0f;

            float oX = centerX;
            float oY;
            float R = centerX;

            float bilv = ((float) (area3DHight - area2DHeight) )/ ((float)area2DHeight);

            LogUtils.INSTANCE.d("?????????????????: " + bilv);

            for (int j = 0; j <= perThickness; j++) {
                tempRectF = new RectF(0, area2DHeight - j, area2DWidth, area3DHight - j);
                oY = (area2DHeight + area3DHight) / 2 - j;
                switch (NOW_TAG) {
                    case CENTER:
                        drawTempStartAngle = leftAngle;

                        lineStartX = oX;
                        lineEndX = oX;
                        lineStartY = (area2DHeight + area3DHight) / 2;
                        lineEndY = oY;
                        break;
                    case LEFT:
                        drawTempStartAngle = leftAngle;
                        /**
                         * 左边夹角tempAngle   0< tempAngle < 180
                         *         90 <drawTempStartAngle <270
                         * 1.0-90
                         *
                         */

                        LogUtils.INSTANCE.d("开始: " + drawTempStartAngle + " " + tempAngle);

                        if (drawTempStartAngle <= 180) {
                            //startAngle 90-180
                            lineStartX = oX - (float) (R * Math.cos(drawTempStartAngle));
                            lineEndX = lineStartX;
                            lineStartY = oY + (float) (R * Math.sin(drawTempStartAngle));
                            lineEndY = lineStartY - j;

                        } else {
                            //startAngle 180-270
                            lineStartX = oX - (float) (R * Math.cos(Math.toRadians(drawTempStartAngle - 180f)));

                            LogUtils.INSTANCE.d("11: " + Math.cos(Math.toRadians(drawTempStartAngle - 180f)));

                            lineEndX = lineStartX;


                            lineStartY = oY - (float) (bilv * R * Math.sin(Math.toRadians(drawTempStartAngle - 180f)));

                            LogUtils.INSTANCE.d("22: " + Math.sin(Math.toRadians(drawTempStartAngle - 180f)));
                            LogUtils.INSTANCE.d("22 R: " + R);
                            LogUtils.INSTANCE.d("22 bilv: " + bilv);



                            LogUtils.INSTANCE.d("22:lineStartY  " + lineStartY);

                            lineEndY = lineStartY - j;
                        }

                        LogUtils.INSTANCE.d("lineStartX: " + lineStartX);
                        LogUtils.INSTANCE.d("lineEndX: " + lineEndX);
                        LogUtils.INSTANCE.d("lineStartY: " + lineStartY);
                        LogUtils.INSTANCE.d("lineEndY: " + lineEndY);

                        break;
                    case RIGHT:
                        drawTempStartAngle = rightAngle - tempAngle;
                        break;
                }


                //弧形
                mainPaint.setColor(tempColor);
                // canvas.drawArc(tempRectF, drawTempStartAngle, tempAngle, true, mainPaint);
                if (j == perThickness) {
                    drawTopLine(canvas, tempRectF, drawTempStartAngle, tempAngle);
                }

                //竖直线
                mainPaint.setColor(Color.WHITE);
                canvas.drawLine(lineStartX, lineStartY,
                        lineEndX, lineEndY,
                        mainPaint);
            }
        }
    }

    //绘制顶部轮廓线
    private void drawTopLine(Canvas canvas, RectF rectF, float startAngle, float swapAngle) {
        mainPaint.setStyle(Paint.Style.STROKE);
        mainPaint.setColor(Color.WHITE);
        canvas.drawArc(rectF, startAngle, swapAngle, true, mainPaint);
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
        switch (type) {
            case LEFT:
                if (angle <= 90f) {
                    return 180f + (90f - angle);
                } else if (angle <= 270f) {
                    return 270f - angle;
                } else {
                    return 270f + (angle - 270f);
                }
            case RIGHT:
                if (angle <= 90f) {
                    return angle + 90f;
                } else if (angle <= 270f) {
                    return angle + 90f;
                } else {
                    return angle - 270f;
                }
        }
        return 0;
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

        @NotNull
        @Override
        public String toString() {
            return "Entry{" +
                    "percent=" + percent +
                    ", color=" + color +
                    '}';
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        area2DWidth = getMeasuredWidth();
        area2DHeight = getMeasuredWidth();

        area3DHight = getMeasuredHeight();
        LogUtils.INSTANCE.d("宽：" + area2DWidth + "高：" + area2DHeight + " 3d高: " + area3DHight + "厚：" + thickness);

        centerX = area2DWidth / 2;
        LogUtils.INSTANCE.d("初始圆心坐标 x：" + centerX + "y：" + ((area2DHeight + area3DHight) / 2) + " R: " + centerX);

    }
}
