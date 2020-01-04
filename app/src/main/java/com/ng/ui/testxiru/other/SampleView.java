package com.ng.ui.testxiru.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import android.view.animation.Animation;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-01-03
 */
public class SampleView extends View {

    private static final int WIDTH = 80;
    private static final int HEIGHT = 80;

    private Bitmap mBitmap;

    private boolean mIsDebug = false;
    private Paint mPaint = new Paint();
    private float[] mInhalePt = new float[]{0, 0};
    private InhaleMesh mInhaleMesh = null;

    private float mTargetX;
    private float mTargetY;

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
        mInhaleMesh.setInhaleDir(InhaleMesh.InhaleDir.UP);


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



    }

    public void setTargetPosition(float x, float y,float width,float height) {
        mTargetX = x;
        mTargetY = y;


        buildPaths(x, y,width,height);
        mInhaleMesh.buildMeshes(0);
        this.postInvalidate();
    }

    public boolean startAnimation(boolean reverse) {
        Animation anim = this.getAnimation();
        if (null != anim && !anim.hasEnded()) {
            return false;
        }
        PathAnimation animation = new PathAnimation(0, HEIGHT + 1, reverse,
                index -> {
                    mInhaleMesh.buildMeshes(index);
                    invalidate();
                });
        animation.setDuration(400);
        this.startAnimation(animation);
        return true;
    }

    public void buildMeshes(int index) {
        mInhaleMesh.buildMeshes(index);
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

    private void buildPaths(float x, float y, float endX, float endY) {
        mInhalePt[0] = endX;
        mInhalePt[1] = endY;
        mInhaleMesh.buildPaths(x,y,endX, endY);
    }

}