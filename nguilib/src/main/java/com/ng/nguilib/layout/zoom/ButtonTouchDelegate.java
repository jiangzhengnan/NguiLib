package com.ng.nguilib.layout.zoom;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020/11/7
 */

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * <b>Project:</b> eventdemo<br>
 * <b>Create Date:</b> 2017/4/24<br>
 * <b>Author:</b> qiwenming<br>
 * <b>Description:</b> <br>
 */
public class ButtonTouchDelegate extends TouchDelegate {
    private final Rect mBounds;
    private final int mSlop;
    private final Rect mSlopBounds;
    private final View mDelegateView;
    private boolean mDelegateTargeted;

    public ButtonTouchDelegate(Rect bounds, View delegateView) {
        super(bounds, delegateView);
        mBounds = bounds;

        mSlop = ViewConfiguration.get(delegateView.getContext()).getScaledTouchSlop();
        mSlopBounds = new Rect(bounds);
        mSlopBounds.inset(-mSlop, -mSlop);
        mDelegateView = delegateView;
    }


    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        boolean sendToDelegate = false;
        boolean hit = true;
        boolean handled = false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Rect bounds = mBounds;

                if (bounds.contains(x, y)) {
                    mDelegateTargeted = true;
                    sendToDelegate = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_MOVE:
                sendToDelegate = mDelegateTargeted;
                if (sendToDelegate) {
                    Rect slopBounds = mSlopBounds;
                    if (!slopBounds.contains(x, y)) {
                        hit = false;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                sendToDelegate = mDelegateTargeted;
                mDelegateTargeted = false;
                break;
        }
        if (sendToDelegate) {
            final View delegateView = mDelegateView;
            if (hit) {
                // Offset event coordinates to be inside the target view
                event.setLocation(delegateView.getWidth() / 2, delegateView.getHeight() / 2);
                handled = delegateView.dispatchTouchEvent(event);
            }
        }
        return handled;
    }
}
