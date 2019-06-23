package com.ng.ui.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @ProjectName: NguiLib
 * @Package: com.ng.ui.test
 * @Description:
 * @Author: Eden
 * @CreateDate: 2019/6/15 11:59
 */
public class TestFbButton extends View {
    private int mPrevState;
    private int mCurState;
    private Drawable mIcon;
    private Drawable mPrevIcon;

    public TestFbButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        mIcon = new TestDrawable();
     }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mIcon.draw(canvas);
    }

    public void switchLineState(int state, boolean animation) {
        ((TestDrawable) mIcon).start();


    }

    public int getLineMorphingState(){
//        if(mIcon != null && mIcon instanceof LineMorphingDrawable)
//            return ((LineMorphingDrawable)mIcon).getLineState();

        return -1;
    }
}
