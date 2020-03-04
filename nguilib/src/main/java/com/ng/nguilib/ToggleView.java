package com.ng.nguilib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.ng.nguilib.utils.LogUtils;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-02-28
 */
public class ToggleView extends View {

    public enum TYPE {
        APPER, DISAPPER, NORMAL
    }

    private long DURATION = 500;

    private boolean isPositive = true;

    private Context mContext;

    private float mHeight;
    private float mWidth;
    private float mEdge;

    private int mPositiveImgId = 0;
    private int mReverseImgId = 0;

    private int mPostiveColor = Color.WHITE;
    private int mReverseColor = Color.BLACK;

    private int mCircleColor = Color.BLACK;
    private float mCircleWidth = 15;

    private Paint mBitmapPaint;

    public ToggleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        //todo 从资源文件获取
        mPositiveImgId = R.drawable.ic_add;
        mReverseImgId = R.drawable.ic_remove;

        //init paint
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);


    }

    public ToggleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startAnim();
                break;
        }
        return super.onTouchEvent(event);
    }

    private ValueAnimator mAnimator;
    private boolean isAnimRunning = false;
    private float thickness = 0f;

    private AnimatorSet mAnimatorSet;

    //start anim
    private void startAnim() {
        if (isAnimRunning) {
            return;
        }
        isAnimRunning = true;
        LogUtils.INSTANCE.d("startAnim : " + isPositive);
        mAnimator = ValueAnimator.ofFloat(0, 1f);
        mAnimator.setDuration(DURATION);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                thickness = (float) animation.getAnimatedValue();
                LogUtils.INSTANCE.d("animing: " + thickness);
                postInvalidate();
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimRunning = false;
                isPositive = !isPositive;
                thickness = 0f;
            }
        });
        mAnimator.start();

        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.8f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.8f, 1f);
        ObjectAnimator animTor = ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY);
        animTor.setDuration(DURATION);
        animTor.start();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        mEdge = Math.min(mHeight, mWidth);
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setLayerType(LAYER_TYPE_HARDWARE, null);

        //  int sc = canvas.saveLayer(allRectF, mBitmapPaint, Canvas.ALL_SAVE_FLAG);

        Bitmap bmInside = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvasInside = new Canvas(bmInside);
        LogUtils.INSTANCE.d("onDraw: " + isPositive);
        if (isPositive) {
            drawBitmap(canvasInside, false, mPostiveColor, mPositiveImgId);
            drawBitmap(canvasInside, true, mReverseColor, mReverseImgId);

        } else {
            drawBitmap(canvas, false, mReverseColor, mReverseImgId);
            drawBitmap(canvas, true, mPostiveColor, mPositiveImgId);

        }


        Bitmap bmOutside = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvasOutside = new Canvas(bmOutside);
        drawMask(canvasOutside);

        //SRC
        canvas.drawBitmap(bmInside, 0, 0, mBitmapPaint);
        mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        //DST
        canvas.drawBitmap(bmOutside, 0, 0, mBitmapPaint);
        mBitmapPaint.setXfermode(null);

    }

    private void drawBitmap(Canvas canvas, boolean isRun, int color, int imgId) {
        mBitmapPaint.setColor(color);
        mBitmapPaint.setStyle(Paint.Style.FILL);

        float centerX = 0;
        if (isRun) {
            centerX = mEdge * thickness - mEdge / 2;
        } else {
            centerX = mEdge / 2;
        }

        canvas.drawCircle(centerX, mEdge / 2, mEdge / 2, mBitmapPaint);

        Rect mRect = new Rect((int) (centerX - mEdge / 2),
                0,
                (int) (centerX + mEdge / 2),
                (int) mEdge);
        Bitmap bitmap = getBitmapFromVectorDrawable(mContext, imgId);
        canvas.drawBitmap(bitmap, null, mRect, mBitmapPaint);

        mBitmapPaint.setColor(mCircleColor);
        mBitmapPaint.setStyle(Paint.Style.STROKE);
        mBitmapPaint.setStrokeWidth(mCircleWidth);
        canvas.drawCircle(mEdge / 2, mEdge / 2, mEdge / 2 - mCircleWidth / 2, mBitmapPaint);

    }

    private void drawMask(Canvas canvas) {
        mBitmapPaint.setColor(Color.RED);
        mBitmapPaint.setStyle(Paint.Style.FILL);
        mBitmapPaint.setStrokeWidth(0);
        canvas.drawCircle(mEdge / 2, mEdge / 2, mEdge / 2, mBitmapPaint);
    }

    public Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
