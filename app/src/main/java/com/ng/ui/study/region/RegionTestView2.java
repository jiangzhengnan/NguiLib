package com.ng.ui.study.region;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.graphics.PathParser;

import com.ng.ui.R;

import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * 描述:  绘制圆角矩形
 *
 * @author Jzn
 * @date 2020-06-03
 */
public class RegionTestView2 extends View {
    public RegionTestView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private Paint mPaint;


    private void init() {

        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(4f);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.STROKE);

        canvas.translate(50,50);

        //绘制矩形轨迹
        Rect rOne = new Rect(0, 0, 400, 200);

        Rect rTwo = new Rect(0, 0, 400, 200);

        //20半径的圆角
        Path pTwo = new Path();
        pTwo.moveTo(0,20);
        pTwo.quadTo(0,0,20,0);
        pTwo.lineTo(380,0);
        pTwo.quadTo(400,0,400,20);
        pTwo.lineTo(400,180);
        pTwo.quadTo(400,200,380,200);
        pTwo.lineTo(20,200);
        pTwo.quadTo(0,200,0,180);
        pTwo.lineTo(0,20);




        //canvas.drawRect(rOne, mPaint);
        //canvas.drawRoundRect(new RectF(rTwo.left, rTwo.top, rTwo.right, rTwo.bottom),50f,50f, mPaint);

        Region regionOne = new Region(rOne);
        Region regionTwo = new Region(rTwo);
        regionTwo.setPath(pTwo,regionTwo);

        regionTwo.op(regionOne, Region.Op.INTERSECT);//交集

        mPaint.setStyle(Paint.Style.FILL);

        drawRegion(canvas, regionTwo, mPaint);


    }

    //RegionIterator 是Region中所有矩阵的迭代器，我们可以通过遍历region中的矩阵，并绘制出来，来绘制region。
    private void drawRegion(Canvas canvas, Region rgn, Paint paint) {
        RegionIterator iter = new RegionIterator(rgn);
        Rect r = new Rect();
        while (iter.next(r)) {
            canvas.drawRect(r, paint);
        }
    }

}
