package com.ng.ui.test;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;

/**
 * @ProjectName: NguiLib
 * @Package: com.ng.ui.test
 * @Description:
 * @Author: Eden
 * @CreateDate: 2019/6/15 14:03
 */
public class TestDrawable extends Drawable implements Animatable, Runnable{
    private boolean mRunning = false;
    @Override
    public void draw(Canvas canvas) {
        canvas.drawARGB(128, (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }
    @Override
    public void setAlpha(int alpha) {
    }
    @Override
    public void setColorFilter(ColorFilter cf) {
    }
    @SuppressLint("WrongConstant")
    @Override
    public int getOpacity() {
        return 0;
    }
    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        boolean changed = super.setVisible(visible, restart);
        if (visible) {
            if (changed || restart) {
                nextFrame();
            }
        } else {
            unscheduleSelf(this);
        }
        return changed;
    }
    @Override
    public void start() {
        if (!mRunning) {
            mRunning = true;
            nextFrame();
        }
    }
    @Override
    public void stop() {
        unscheduleSelf(this);
        mRunning = false;
    }
    @Override
    public boolean isRunning() {
        return mRunning;
    }
    @Override
    public void run() {
        invalidateSelf();
        nextFrame();
    }
    private void nextFrame() {
        unscheduleSelf(this);
        scheduleSelf(this, SystemClock.uptimeMillis() + 500);
    }
}
