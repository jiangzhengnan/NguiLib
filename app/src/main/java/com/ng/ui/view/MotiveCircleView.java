package com.ng.ui.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.ng.ui.R;


/**
 * Description:
 * -
 */
public class MotiveCircleView extends View {
    private int color;
    private int textColor;
    private float progress;
    private float thick;    //厚度
    private float textSize;
    private float shadowRadius = 5;

    private Paint paint;
    private Typeface typeface;
    private Path path;
    private Path clipPath;
    private float xOffset = 0;
    private ValueAnimator xOffsetAnimator;
    private ValueAnimator progressAnimator;
    private PorterDuffXfermode xfermode;

    public MotiveCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MotiveCircleView);
        color = array.getColor(R.styleable.MotiveCircleView_MotiveCircleView_color, Color.YELLOW);
        textColor = array.getColor(R.styleable.MotiveCircleView_MotiveCircleView_textColor,Color.YELLOW);
        progress = array.getInt(R.styleable.MotiveCircleView_MotiveCircleView_progress,50);
        thick = array.getDimension(R.styleable.MotiveCircleView_MotiveCircleView_thick,10);
        textSize = array.getDimension(R.styleable.MotiveCircleView_MotiveCircleView_textSize,60);
        array.recycle();

        paint = new Paint();
        typeface = Typeface.create(Typeface.MONOSPACE,Typeface.BOLD);
        path = new Path();
        clipPath = new Path();
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.XOR);

        setLayerType(LAYER_TYPE_SOFTWARE,null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        float cx = width / 2f;
        float cy = height / 2f;
        float radius = cx - thick / 2 - shadowRadius;
        float left;
        float top;
        float right;
        float bottom;

        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(thick);
        paint.setAntiAlias(true);
        paint.setShadowLayer(shadowRadius,1f,1f,Color.parseColor("#410400"));

        int layerId = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);

        canvas.drawCircle(cx,cy,radius,paint);

        float dx = width / 4f;
        float dy = height / 8f;
        float yOffset = (100 - progress) / 100f * height;
        float[] p1 = {xOffset,yOffset};
        float[] p2 = {dx,-dy};
        float[] p3 = {2 * dx,0};
        float[] p4 = {dx,dy};
        float[] p5 = {2 * dx,0};
        float[] p6 = {dx,-dy};
        float[] p7 = {2 * dx,0};
        float[] p8 = {dx,dy};
        float[] p9 = {2 * dx,0};

        paint.setStyle(Paint.Style.FILL);

        path.reset();
        path.moveTo(p1[0],p1[1]);
        path.rQuadTo(p2[0],p2[1],p3[0],p3[1]);
        path.rQuadTo(p4[0],p4[1],p5[0],p5[1]);
        path.rQuadTo(p6[0],p6[1],p7[0],p7[1]);
        path.rQuadTo(p8[0],p8[1],p9[0],p9[1]);
        path.lineTo(width,height);
        path.lineTo(0,height);
        path.close();

        clipPath.reset();
        left = thick - 1 + shadowRadius;
        top = thick - 1  + shadowRadius;
        right = width + 1 - thick - shadowRadius;
        bottom = height + 1 - thick  - shadowRadius;
        clipPath.addArc(left,top,right,bottom,0,360);

        canvas.clipPath(clipPath);
        canvas.drawPath(path,paint);

        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setXfermode(xfermode);
        paint.setTypeface(typeface);
        paint.setTextAlign(Paint.Align.CENTER);

        String text = (int)progress + "%";

        Paint.FontMetrics metrics = paint.getFontMetrics();
        float baselineY = height / 2f - metrics.top / 2f - metrics.bottom / 2f;
        canvas.drawText(text,width / 2f,baselineY,paint);
        paint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }

    public void startAnima(){
        if(xOffsetAnimator == null){
            xOffsetAnimator = ValueAnimator.ofInt(0,getWidth());
            xOffsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    xOffset = -value;
                    postInvalidate();
                }
            });
            xOffsetAnimator.setInterpolator(new LinearInterpolator());
            xOffsetAnimator.setDuration(1500);
            xOffsetAnimator.setRepeatCount(ValueAnimator.INFINITE);
        }
        xOffsetAnimator.start();
    }

    public void cancelAnima(){
        if(xOffsetAnimator != null && xOffsetAnimator.isRunning()){
            xOffsetAnimator.cancel();
        }
        if(progressAnimator != null && progressAnimator.isRunning()){
            progressAnimator.cancel();
        }
    }

    public int getProgress(){
        return (int) progress;
    }

    public void setProgress(int progress){
        if(this.progress == progress) return;
        progress = progress > 100 ? 0 : progress;
        if(progressAnimator == null){
            progressAnimator = ValueAnimator.ofFloat(this.progress,progress);
            progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    MotiveCircleView.this.progress = value;
                }
            });
            progressAnimator.setInterpolator(new DecelerateInterpolator());
            progressAnimator.setDuration(500);
        }
        if(progressAnimator.isRunning()){
            progressAnimator.cancel();
        }
        progressAnimator.setFloatValues(this.progress,progress);
        progressAnimator.start();
    }
}
