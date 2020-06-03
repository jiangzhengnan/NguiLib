package com.ng.ui.study.region;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.graphics.PathParser;

import com.ng.nguilib.utils.LogUtils;
import com.ng.ui.R;

import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * 描述:  包含点
 *
 * @author Jzn
 * @date 2020-06-03
 */
public class RegionTestView extends View {
    public RegionTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private Path mStarsPath;

    private Path mTouchPath;

    private Paint mPaint;


    private void init() {
        try {
            String pathName = "android:pathData";
            NodeList nodeList = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(getContext().getResources().openRawResource(R.raw.starts))
                    .getElementsByTagName("path");
            mStarsPath = PathParser.createPathFromPathData(nodeList.item(0).getAttributes().getNamedItem(pathName).getNodeValue());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5f);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mTouchPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(mStarsPath, mPaint);
        canvas.drawPath(mTouchPath, mPaint);
    }

    private float mPosX, mPosY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        Region region = new Region();
        RectF rectF = new RectF();
        mStarsPath.computeBounds(rectF, true);
        region.setPath(mStarsPath, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        if (!region.contains((int) x, (int) y))
            return true;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mTouchPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                mTouchPath.quadTo(mPosX, mPosY, x, y);
                break;
            case MotionEvent.ACTION_UP:
                //mTouchPath.reset();
                break;
        }
        //记录当前触摸点得当前得坐标
        mPosX = x;
        mPosY = y;

        postInvalidate();
        return true;
    }


}
