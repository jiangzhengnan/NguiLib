package com.ng.ui.study.rotate.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.ng.ui.R;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-06-04
 */
public class RotateTestXyzView extends View {
    private Paint bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap bitmap;
    private RectF rectF;
    private AnimatorSet animatorSet = new AnimatorSet();
    private Camera camera = new Camera();
    private Paint mPaint = new Paint();
    //裁切部分Path
    private Path path;


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animatorSet.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animatorSet.end();
    }

    public RotateTestXyzView(Context context) {
        super(context);
    }

    public RotateTestXyzView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @SuppressLint("ObjectAnimatorBinding")
    private void init() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_icon);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(4f);
        mPaint.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0));
        mPaint.setStyle(Paint.Style.STROKE);
        path = new Path();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        //绘制位置
        int x = centerX - bitmap.getWidth() / 2;
        int y = centerY - bitmap.getHeight() / 2;

        if (rectF == null) {
            //计算圆形裁切部分半径
            int l = centerX > centerY ? centerX : centerY;
            double r = Math.sqrt(2) * l;
            rectF = new RectF((float) (centerX - r), (float) (centerY - r), (float) (centerX + r), (float) (centerY + r));
        }

        canvas.save();
        camera.save();

        //先裁切
        path.reset();
        path.addArc(rectF, mQ, 180);  //90~
        canvas.clipPath(path);  //裁剪左半部分

        //移动坐标轴到中点
        canvas.translate(centerX, centerY);
        //旋转坐标轴
        canvas.rotate(mR);

        //操作camera并映射画布
        camera.rotate(mX, mY, mZ);
        camera.applyToCanvas(canvas);

        canvas.rotate(-mR);

        canvas.translate(-centerX, -centerY);

        canvas.drawBitmap(bitmap, x, y, bitmapPaint);

        camera.restore();
        canvas.restore();


        //绘制x轴和y轴的辅助线
        canvas.save();
        camera.save();
        //第二部分图片裁切层
        path.reset();
        path.addArc(rectF, mQ + 180, 180);
        canvas.clipPath(path);//裁剪右半部分
        //绘制第二部分
        canvas.drawBitmap(bitmap, x, y, bitmapPaint);
        canvas.restore();
        canvas.save();

        canvas.translate(centerX, centerY);
        canvas.rotate(mR);
        mPaint.setColor(Color.YELLOW);
        canvas.drawLine(-getWidth(), 0, getWidth(), 0, mPaint);
        mPaint.setColor(Color.GREEN);
        canvas.drawLine(0, -getHeight(), 0, getHeight(), mPaint);
        canvas.rotate(-mR);
        canvas.translate(-centerX, -centerY);

        camera.restore();
        canvas.restore();


    }


    int mX, mY, mZ, mR, mQ;

    public void setRX(int x) {
        this.mX = x;
        invalidate();
    }

    public void setRY(int y) {
        this.mY = y;
        invalidate();
    }

    public void setRZ(int z) {
        this.mZ = z;
        invalidate();
    }

    public void setRR(int r) {
        this.mR = r;
        invalidate();
    }

    public void setRQ(int q) {
        this.mQ = q;
        invalidate();
    }

    public int getmX() {
        return mX;
    }

    public int getmY() {
        return mY;
    }

    public int getmZ() {
        return mZ;
    }

    public int getmR() {
        return mR;
    }

    public int getmQ() {
        return mQ;
    }
}
