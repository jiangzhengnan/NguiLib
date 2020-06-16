package com.ng.ui.study.rotate.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import com.ng.ui.R;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-06-04
 */
public class RotateTestView extends View {
    private Paint bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap bitmap;
    private RectF rectF;
    private AnimatorSet animatorSet = new AnimatorSet();
    private Camera camera = new Camera();
    //裁切部分Path
    private Path path;

    //旋转部分
    private int rotate = 270;
    //第一部分图片旋转程度
    private int degree1;
    //第二部分图片旋转程度
    private int degree2;

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_icon);

        path=new Path();


        ObjectAnimator animator1 = ObjectAnimator.ofInt(this, "degree1", 0, 45).setDuration(2000);
        ObjectAnimator animator2 = ObjectAnimator.ofInt(this, "rotate", 270, 0).setDuration(2000);
        animator2.setInterpolator(new DecelerateInterpolator());
        ObjectAnimator animator3 = ObjectAnimator.ofInt(this, "degree2", 0, 45).setDuration(2000);
        animatorSet.playSequentially(animator1,animator2,animator3);
    }

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

    public RotateTestView(Context context) {
        super(context);
    }

    public RotateTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RotateTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        int x = centerX - bitmap.getWidth() / 2;
        int y = centerY - bitmap.getHeight() / 2;

        if (rectF == null){
            //计算圆形裁切部分半径
            int l = centerX > centerY ? centerX : centerY;
            double r = Math.sqrt(2) * l + 1;
            rectF = new RectF((float)(centerX-r),(float)(centerY-r),(float)(centerX+r),(float)(centerY+r));
        }

        /*
         * 第一部分图片绘制
         */
        canvas.save();
        camera.save();

        //第一部分图片裁切层
        path.reset();
        path.addArc(rectF,rotate+180,180);
        canvas.clipPath(path);

        //第一部分图片旋转部分
        canvas.translate(centerX,centerY);
        camera.rotateX(-degree2);
        camera.applyToCanvas(canvas);
        canvas.translate(-centerX,-centerY);

        canvas.drawBitmap(bitmap,x,y,bitmapPaint);
        camera.restore();
        canvas.restore();

        /*
         * 第二部分图片绘制
         */
        canvas.save();
        camera.save();

        //第二部分图片裁切层
        path.reset();
        path.addArc(rectF,rotate,180);
        canvas.clipPath(path);

        //第二部分图片旋转部分
        canvas.translate(centerX,centerY);
        canvas.rotate(rotate-90);
        camera.rotateY(degree1);
        camera.applyToCanvas(canvas);
        canvas.rotate(90-rotate);
        canvas.translate(-centerX,-centerY);

        canvas.drawBitmap(bitmap,x,y,bitmapPaint);
        camera.restore();
        canvas.restore();
    }

    public void setDegree2(int degree2) {
        this.degree2 = degree2;
        invalidate();
    }

    public void setDegree1(int degree1) {
        this.degree1 = degree1;
        invalidate();
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
        invalidate();
    }

    public void startAnim(){
        animatorSet.end();
        rotate = 270;
        degree1 = 0;
        degree2 = 0;
        invalidate();
        animatorSet.start();
    }
}
