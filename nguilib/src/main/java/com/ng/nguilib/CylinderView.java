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
    private ArrayList<Float> percent;

    //饼图的paint
    private Paint mainPaint;


    //点击事件的计算
    private float centerX;
    private float centerY;

    //颜色数组
    private int[] colors = new int[]{
            Color.rgb(54, 217, 169), Color.rgb(0, 171, 255),
            Color.rgb(80, 195, 252), Color.rgb(13, 142, 207),
            Color.rgb(2, 211, 21), Color.rgb(176, 222, 9),
            Color.rgb(248, 255, 1), Color.rgb(252, 210, 2),
            Color.rgb(255, 159, 13), Color.rgb(255, 100, 0),
            Color.rgb(234, 14, 0),
    };
    //阴影数组
    private int[] shade_colors = new int[]{
            Color.rgb(26, 164, 123), Color.rgb(0, 154, 230), Color.rgb(21, 178, 255), Color.rgb(5, 102, 153),
            Color.rgb(3, 147, 15), Color.rgb(124, 158, 8), Color.rgb(212, 218, 2), Color.rgb(219, 183, 6),
            Color.rgb(214, 135, 5), Color.rgb(210, 90, 13), Color.rgb(199, 13, 1),
    };

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

    public void setData(ArrayList<Float> data) {
        this.percent = data;
        Collections.sort(percent, new MyCompare());
        LogUtils.INSTANCE.d("****传入的数据: " + percent.toString());
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

        if (percent == null) {
            return;
        }

        leftAngle = 0;
        rightAngle = 0;

        RectF tempRectF = new RectF(areaX, areaY, areaX + areaWidth, areaHight);
        int max = percent.size();
        for (int i = 0; i < max; i++) {
            Collections.sort(percent, new MyCompare());


            mainPaint.setStyle(Paint.Style.FILL);
            mainPaint.setColor(colors[i]);

            float tempAngle = percent.get(0);

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

            } else {
                //1.找到当前小的边

                if (getDistanceToCenter(leftAngle, LEFT) > getDistanceToCenter(rightAngle, RIGHT)) {
                    //左边比右边大，取右边
                    NOW_TAG = RIGHT;
                    tempAngle = getNextAngle(getDistanceToCenter(rightAngle, RIGHT));
                    rightAngle += rightAngle;

                    LogUtils.INSTANCE.d("放右边： " + tempAngle);

                } else {
                    //右边比左边大，取左边
                    NOW_TAG = LEFT;
                    tempAngle = getNextAngle(getDistanceToCenter(leftAngle, LEFT));

                    leftAngle += tempAngle;


                    LogUtils.INSTANCE.d("放左边： " + tempAngle);

                }

            }


            //绘制各个弧度
            int perThickness = (int) ((tempAngle / 360f) * thickness);
            for (int j = 0; j <= perThickness; j++) {
                tempRectF = new RectF(areaX, areaY - j, areaX + areaWidth, areaHight - j);
                if (j == perThickness) {
                    mainPaint.setStyle(Paint.Style.STROKE);
                    mainPaint.setColor(Color.rgb(255, 255, 255));
                }

                switch (NOW_TAG) {
                    case LEFT:
                        canvas.drawArc(tempRectF, leftAngle + tempAngle, tempAngle, true, mainPaint);
                        break;
                    case RIGHT:
                        canvas.drawArc(tempRectF, rightAngle, tempAngle, true, mainPaint);

                        break;
                }
            }

            percent.remove(0);
        }


    }

    private float getNextAngle(float distanceAngle) {

        float tempAngle;
        if (percent.size() == 0) {
            return 0;
        }
        tempAngle = percent.get(0);

        if (percent.size() == 1) {
            percent.remove(0);
            return tempAngle;
        } else {

            float allAngle = tempAngle + distanceAngle;
            if (allAngle <= 180f) {
                //如果加起来不大于180f
                percent.remove(0);
                return tempAngle;
            } else {
                //如果加起来大于180f，则一直找，找到不大于的位置
                int tempindex = 0;

                while ( tempindex < percent.size() ) {
                     tempindex ++;

                     if (percent.get(tempindex) + distanceAngle <= 180f) {
                            return percent.get(tempindex);
                     }
                }

                return percent.get(0);



            }


        }


    }

    //得到相对中心位置的绝对距离
    private float getDistanceToCenter(float angle, int type) {

        switch (type) {
            case LEFT:
                if (90f <= angle && angle <= 270f) {
                    //90-270
                    return 270f - angle;
                }

                if (angle <= 360f) {
                    //270-360
                    return 270f + (angle - 270f);
                } else {
                    //0 - 90
                    return 180f + (90f - angle);
                }

            case RIGHT:
                if (90f <= angle && angle <= 270f) {
                    //90-270
                    return angle + 90f;
                }

                if (angle <= 360f) {
                    //270-360
                    return angle - 270f;
                } else {
                    //0 - 90
                    return angle + 90f;
                }
        }

        return 0f;

    }

    class MyCompare implements Comparator<Float> {

        @Override
        public int compare(Float o1, Float o2) {
            return o1 > o2 ? -1 : (o1 == o2 ? 0 : 1);
        }


    }
}
