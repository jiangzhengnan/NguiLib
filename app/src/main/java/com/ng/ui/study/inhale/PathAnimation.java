package com.ng.ui.study.inhale;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-01-03
 */

import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;

public class PathAnimation extends Animation {
    public interface IAnimationUpdateListener {
        void onAnimUpdate(int index);
    }

    private int mFromIndex;
    private int mEndIndex;
    private IAnimationUpdateListener mListener;

    public PathAnimation(int fromIndex, int endIndex, IAnimationUpdateListener listener) {
        mFromIndex = fromIndex;
        mEndIndex = endIndex;
        mListener = listener;
    }

    public boolean getTransformation(long currentTime, Transformation outTransformation) {
        boolean more = super.getTransformation(currentTime, outTransformation);
        return more;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int curIndex = 0;
        Interpolator interpolator = this.getInterpolator();
        if (null != interpolator) {
            float value = interpolator.getInterpolation(interpolatedTime);
            interpolatedTime = value;
        }

        curIndex = (int) (mFromIndex + (mEndIndex - mFromIndex) * interpolatedTime);

        if (null != mListener) {
            mListener.onAnimUpdate(curIndex);
        }
    }
}