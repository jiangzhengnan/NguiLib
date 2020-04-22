package com.ng.ui.study.inhale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.Nullable;

import com.ng.nguilib.utils.LogUtils;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-04-21
 */
public class MyInhaleView extends View {
    private Bitmap mBitmap;
    private float mBmpW;
    private float mBmpH;

    //3行3列
    private static final int WIDTH = 3;
    private static final int HEIGHT = 3;
    private static final long DURATION = 3 * 1000;

    //矩阵数组
    private float[] mVerts;

    //绘制画笔
    private Paint mPaint = new Paint();


    //目标点
    private float[] mTargetPoint;
    //目标路径
    private Path[] paths;
    private Path mFirstPath = new Path();
    private Path mSecondPath = new Path();
    private PathMeasure mFirstPathMeasure = new PathMeasure();
    private PathMeasure mSecondPathMeasure = new PathMeasure();

    public MyInhaleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        initPaint();
    }

    private void initPaint() {
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);
    }

    private void init() {
        mVerts = new float[(WIDTH + 1) * (HEIGHT + 1) * 2];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap == null)
            return;

        //画bitmap
        canvas.drawBitmapMesh(mBitmap,
                WIDTH,
                HEIGHT,
                mVerts,
                0, null, 0, mPaint);


        //在目标点画个圈
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        if (mTargetPoint != null) {
            canvas.drawCircle(mTargetPoint[0], mTargetPoint[1], 20, mPaint);
        }

        //画分割线
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i + 1 < mVerts.length / 2; i++) {
            if ((i + WIDTH + 1) * 2 + 1 <= mVerts.length) {
                canvas.drawLine(
                        mVerts[i * 2],
                        mVerts[i * 2 + 1],
                        mVerts[(i + WIDTH + 1) * 2],
                        mVerts[(i + WIDTH + 1) * 2 + 1],
                        mPaint);
            }
            if (i != 0 && ((i + 1) % (WIDTH + 1) == 0)) {
                continue;
            }
            canvas.drawLine(
                    mVerts[i * 2],
                    mVerts[i * 2 + 1],
                    mVerts[i * 2 + 2],
                    mVerts[i * 2 + 3],
                    mPaint);
        }

        //画路径线
        if (paths != null) {
            mPaint.setColor(Color.BLUE);
            mPaint.setStyle(Paint.Style.STROKE);
            for (Path path : paths) {
                canvas.drawPath(path, mPaint);
            }
        }
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        mBmpW = mBitmap.getWidth();
        mBmpH = mBitmap.getHeight();

        buildMesh(mBmpW, mBmpH);

        postInvalidate();
    }

    private void buildMesh(float mBmpW, float mBmpH) {
        int index = 0;
        for (int y = 0; y <= HEIGHT; ++y) {
            float fy = y * mBmpH / HEIGHT;
            for (int x = 0; x <= WIDTH; ++x) {
                float fx = x * mBmpW / WIDTH;
                setXY(mVerts, index, fx, fy);
                index += 1;
            }
        }
    }

    private void setXY(float[] array, int index, float x, float y) {
        array[index * 2] = x;
        array[index * 2 + 1] = y;
    }


    public boolean startAnimation() {
        if (!isClickEd) {
            return false;
        }
        Animation anim = this.getAnimation();
        if (null != anim && !anim.hasEnded()) {
            return false;
        }
        PathAnimation animation = new PathAnimation(0, HEIGHT,
                index -> {
                    LogUtils.INSTANCE.d("进度:" + index);
                    buildMeshes(index);
                    invalidate();
                });
        animation.setDuration(DURATION);
        this.startAnimation(animation);
        return true;
    }


    /**
     * 动态计算绘制路径
     * 1.计算两条pathmeasure
     * 2.根据动画index 计算左右两边路径各自的第一个点和最后一个点坐标
     * 3.分别计算网格里的每个点位置
     *
     * @param timeIndex
     */
    private void buildMeshes(int timeIndex) {
        mFirstPathMeasure.setPath(mFirstPath, false);
        mSecondPathMeasure.setPath(mSecondPath, false);

        int index = 0;
        float[] pos1 = {0.0f, 0.0f};
        float[] pos2 = {0.0f, 0.0f};
        float firstLen = mFirstPathMeasure.getLength();
        float secondLen = mSecondPathMeasure.getLength();
        float len1 = firstLen / HEIGHT;
        float len2 = secondLen / HEIGHT;

        float firstPointDist = timeIndex * len1;    //左边第一个点长度
        float secondPointDist = timeIndex * len2;   //右边第一个点长度
        float height = mBmpH;   //图片高度

        mFirstPathMeasure.getPosTan(firstPointDist, pos1, null);
        mFirstPathMeasure.getPosTan(firstPointDist + height, pos2, null);   //得到第一个点坐标和最后一个点坐标

        float x1 = pos1[0];
        float x2 = pos2[0];
        float y1 = pos1[1];
        float y2 = pos2[1];
        float FIRST_DIST = (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        float FIRST_H = FIRST_DIST / HEIGHT;

        mSecondPathMeasure.getPosTan(secondPointDist, pos1, null);
        mSecondPathMeasure.getPosTan(secondPointDist + height, pos2, null);
        x1 = pos1[0];
        x2 = pos2[0];
        y1 = pos1[1];
        y2 = pos2[1];

        float SECOND_DIST = (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        float SECOND_H = SECOND_DIST / HEIGHT;


        for (int y = 0; y <= HEIGHT; ++y) {
            //得到每一个点的位置
            mFirstPathMeasure.getPosTan(y * FIRST_H + firstPointDist, pos1, null);
            mSecondPathMeasure.getPosTan(y * SECOND_H + secondPointDist, pos2, null);

            float w = pos2[0] - pos1[0];//横轴最左边到最右边的距离
            //左右两边的点的位置
            float fx1 = pos1[0];
            float fx2 = pos2[0];
            float fy1 = pos1[1];
            float fy2 = pos2[1];
            //左右两边点 x 和 y轴方向的差值
            float dy = fy2 - fy1;
            float dx = fx2 - fx1;

            for (int x = 0; x <= WIDTH; ++x) {
                // y = x * dy / dx
                float fx = x * w / WIDTH;
                //tanα = dy/dx = fy/fx
                float fy = fx * dy / dx;

                mVerts[index * 2 + 0] = fx + fx1;
                mVerts[index * 2 + 1] = fy + fy1;

                index += 1;
            }
        }
    }

    private boolean isClickEd = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                isClickEd = true;
                float x = event.getX();
                float y = event.getY();
                mTargetPoint = new float[]{x, y};
                mFirstPath.reset();
                mSecondPath.reset();

                mFirstPath.moveTo(0, 0);
                mFirstPath.lineTo(0, mBmpH);
                mFirstPath.lineTo(x, y);

                mSecondPath.moveTo(mBmpW, 0);
                mSecondPath.lineTo(mBmpW, mBmpH);
                mSecondPath.lineTo(x, y);

                paths = new Path[]{mFirstPath, mSecondPath};
                postInvalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

}
