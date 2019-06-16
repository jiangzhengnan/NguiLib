package com.ng.ui.test;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

/**
 * @ProjectName: NguiLib
 * @Package: com.ng.ui.test
 * @Description:
 * @Author: Eden
 * @CreateDate: 2019/6/15 12:13
 */
public class LineMorphingDrawable  extends Drawable implements Animatable {
    @Override
    public void start() {
        
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void draw( Canvas canvas) {

    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(  ColorFilter colorFilter) {

    }

    @SuppressLint("WrongConstant")
    @Override
    public int getOpacity() {
        return 0;
    }
}
