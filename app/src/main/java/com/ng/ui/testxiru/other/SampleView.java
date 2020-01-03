package com.ng.ui.testxiru.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;

import com.ng.nguilib.utils.LogUtils;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-01-03
 */
public class SampleView extends View {

    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;

    private Bitmap mBitmap;

    private boolean mIsDebug = false;
    private Paint mPaint = new Paint();
    private float[] mInhalePt = new float[]{0, 0};
    private InhaleMesh mInhaleMesh = null;

    public SampleView(Context context) {
        super(context);
        setFocusable(true);

     }

    public void setmBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;

        float bmpW = mBitmap.getWidth();
        float bmpH = mBitmap.getHeight();

        mInhaleMesh = new InhaleMesh(WIDTH, HEIGHT);


        buildMesh(bmpW, bmpH);

        mInhaleMesh.setBitmapSize(mBitmap.getWidth(), mBitmap.getHeight());
        mInhaleMesh.setInhaleDir(InhaleMesh.InhaleDir.DOWN);


        mInhaleMesh.buildMeshes(1);

        postInvalidate();

    }


    public void setIsDebug(boolean isDebug) {
        mIsDebug = isDebug;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //mMatrix.setTranslate(10, 10);
        //mMatrix.setTranslate(10, 10);

        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);

        LogUtils.INSTANCE.d("屏幕宽高: " + w + "  " + h);

        buildPaths(w, h);
     }

    public boolean startAnimation(boolean reverse) {
        Animation anim = this.getAnimation();
        if (null != anim && !anim.hasEnded()) {
            return false;
        }

        PathAnimation animation = new PathAnimation(0, HEIGHT + 1, reverse,
                new PathAnimation.IAnimationUpdateListener() {
                    @Override
                    public void onAnimUpdate(int index) {
                        LogUtils.INSTANCE.d("a: " + index);
                        mInhaleMesh.buildMeshes(index);
                        invalidate();
                    }
                });

        if (null != animation) {
            animation.setDuration(1000);
            this.startAnimation(animation);
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null) {
            return;
        }



        canvas.drawBitmapMesh(mBitmap,
                mInhaleMesh.getWidth(),
                mInhaleMesh.getHeight(),
                mInhaleMesh.getVertices(),
                0, null, 0, mPaint);

        // ===========================================
        // Draw the target point.
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mInhalePt[0], mInhalePt[1], 5, mPaint);

        if (mIsDebug) {
            // ===========================================
            // Draw the mesh vertices.
            canvas.drawPoints(mInhaleMesh.getVertices(), mPaint);

            // ===========================================
            // Draw the paths
            mPaint.setColor(Color.BLUE);
            mPaint.setStyle(Paint.Style.STROKE);
            Path[] paths = mInhaleMesh.getPaths();
            for (Path path : paths) {
                canvas.drawPath(path, mPaint);
            }
        }
    }

    private void buildMesh(float w, float h) {
        mInhaleMesh.buildMeshes(w, h);
    }

    private void buildPaths(float endX, float endY) {
        mInhalePt[0] = endX;
        mInhalePt[1] = endY;
        mInhaleMesh.buildPaths(endX, endY);
    }

    int mLastWarpX = 0;
    int mLastWarpY = 0;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        float[] pt = {event.getX(), event.getY()};
//        mInverse.mapPoints(pt);
//
//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            int x = (int) pt[0];
//            int y = (int) pt[1];
//            if (mLastWarpX != x || mLastWarpY != y) {
//                mLastWarpX = x;
//                mLastWarpY = y;
//                buildPaths(pt[0], pt[1]);
//                invalidate();
//            }
//        }
        return true;
    }

//    public void setInhaleDir(InhaleMesh.InhaleDir dir) {
//        mInhaleMesh.setInhaleDir(dir);
//
//        float w = mBitmap.getWidth();
//        float h = mBitmap.getHeight();
//        float endX = 0;
//        float endY = 0;
//        float dx = 10;
//        float dy = 10;
//        mMatrix.reset();
//
//        switch (dir) {
//            case DOWN:
//                endX = w / 2;
//                endY = getHeight() - 20;
//                break;
//
//            case UP:
//                dy = getHeight() - h - 20;
//                endX = w / 2;
//                endY = -dy + 10;
//                break;
//
//            case LEFT:
//                dx = getWidth() - w - 20;
//                endX = -dx + 10;
//                endY = h / 2;
//                break;
//
//            case RIGHT:
//                endX = getWidth() - 20;
//                endY = h / 2;
//                break;
//        }
//
//        mMatrix.setTranslate(dx, dy);
//        mMatrix.invert(mInverse);
//        buildPaths(endX, endY);
//        buildMesh(w, h);
//        invalidate();
//    }


}