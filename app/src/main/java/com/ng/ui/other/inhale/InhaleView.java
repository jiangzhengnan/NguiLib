package com.ng.ui.other.inhale;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;

import com.ng.nguilib.utils.LogUtils;

import java.util.Arrays;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-01-03
 */
public class InhaleView extends View {

    private static final int WIDTH = 80;
    private static final int HEIGHT = 80;

    private Bitmap mBitmap;

    private boolean mIsDebug = false;
    private Paint mPaint = new Paint();
    private Paint mBgPaint = new Paint();

    private float[] mInhalePt = new float[]{0, 0};
    private InhaleMesh mInhaleMesh = null;

    private float mTargetX;
    private float mTargetY;

    public InhaleView(Context context) {
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

        mBgPaint.setStyle(Paint.Style.FILL);


    }

    public void setTargetPosition(float x, float y, float width, float height) {
        mTargetX = x;
        mTargetY = y;


        buildPaths(x, y, width, height);
        mInhaleMesh.buildMeshes(0);
        this.postInvalidate();
    }

    private int mAnimIndex = 0;

    public boolean startAnimation(boolean reverse) {
        Animation anim = this.getAnimation();
        if (null != anim && !anim.hasEnded()) {
            return false;
        }
        PathAnimation animation = new PathAnimation(0, HEIGHT + 1, reverse,
                index -> {
                    mAnimIndex = index;


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


    public static void setWindowAlpha(Context context, float alpha) {
        Activity activity = getActivity(context);
        if (activity != null) {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.alpha = alpha;
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            activity.getWindow().setAttributes(lp);
        }
    }

    String padLeft(String s, int length) {
        byte[] bs = new byte[length];
        byte[] ss = s.getBytes();
        Arrays.fill(bs, (byte) (48 & 0xff));
        System.arraycopy(ss, 0, bs, length - ss.length, ss.length);
        return new String(bs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null) {
            return;
        }

        /**
         *    //(0.76f - 1f)   (0 - HEIGHT+1)
         *                     float alpha = 0.76f + (1f - 0.76f) * (index / (HEIGHT + 1f));
         *                     LogUtils.INSTANCE.d("alpha: " + alpha);
         *                     setWindowAlpha(getContext(), alpha);
         */

        //(0.24f - 0f)   (0 - HEIGHT+1)
        int colorAlpha = (int) (255f * 0.24f * (1f - mAnimIndex / (HEIGHT + 1f)));
        LogUtils.INSTANCE.d("颜色: " + colorAlpha);

        canvas.drawColor(Color.argb(colorAlpha, 0, 0, 0));

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
        mInhaleMesh.buildPaths(x, y, endX, endY);
    }

    public static Activity getActivity(Context context) {
        // Gross way of unwrapping the Activity so we can get the FragmentManager
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        throw new IllegalStateException("The MediaRouteButton's Context is not an Activity.");
    }

}