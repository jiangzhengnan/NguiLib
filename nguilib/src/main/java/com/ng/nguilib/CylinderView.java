package com.ng.nguilib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @ProjectName: NGUI
 * @Package: com.ng.nguilib.ball
 * @Description:
 * @Author: Pumpkin
 * @CreateDate: 2019/11/24
 */
public class CylinderView extends View {


    //区域百分比
    private int percent[];

    //饼图的paint
    private Paint mainPaint;
    //饼图的宽高
    private int areaWidth = 360;
    private int areaHight = 450;

    //点击事件的计算
    private float centerX;
    private float centerY;

    //颜色数组
    private int[] colors = new int[]{
            Color.rgb(54, 217, 169), Color.rgb(0, 171, 255), Color.rgb(80, 195, 252), Color.rgb(13, 142, 207),
            Color.rgb(2, 211, 21), Color.rgb(176, 222, 9), Color.rgb(248, 255, 1), Color.rgb(252, 210, 2),
            Color.rgb(255, 159, 13), Color.rgb(255, 100, 0), Color.rgb(234, 14, 0),
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
        this.percent = new int[]{30, 120, 180, 30};

        mainPaint = new Paint();
        mainPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mainPaint.setAntiAlias(true);
    }

    int areaX = 1;
    int areaY = 200;
    private int thickness = 30;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //开画
        int tempAngle = 0;
        for (int j = 0; j < percent.length; j++) {
            mainPaint.setStyle(Paint.Style.FILL_AND_STROKE);

            //画笔的颜色
            mainPaint.setColor(shade_colors[j]);


            //坐标点 start end
            RectF rectF = new RectF(areaX, areaY - 1, areaX + areaWidth, areaHight - 1);
            if (j == percent.length-1) {
                for (int i = 0; i <= thickness; i++) {
                    rectF = new RectF(areaX, areaY -  i, areaX + areaWidth, areaHight -   i);
                    Log.d("nangua", "俺几次");

                    if (i == thickness) {
                        mainPaint.setStyle(Paint.Style.STROKE);
                        mainPaint.setColor( Color.rgb(255, 255, 255));

                    }

                    canvas.drawArc(rectF, tempAngle, percent[j], true, mainPaint);

                }

            } else {
                Log.d("nangua", "别的几次");
                rectF = new RectF(areaX, areaY , areaX + areaWidth, areaHight  );

                //画弧
                 canvas.drawArc(rectF, tempAngle, percent[j], true, mainPaint);

            }
            tempAngle += percent[j];
        }


    }
}
