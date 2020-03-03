package com.ng.nguilib;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-02-28
 */
public class ToggleView extends View {

    public ToggleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ToggleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        canvas.drawCircle(0,0,50,new pain
//                );
    }
}
