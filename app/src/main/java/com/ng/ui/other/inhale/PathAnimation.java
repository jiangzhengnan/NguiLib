package com.ng.ui.other.inhale;

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
    private boolean mReverse;
    private IAnimationUpdateListener mListener;

    PathAnimation(int fromIndex, int endIndex, boolean reverse, IAnimationUpdateListener listener) {
        mFromIndex = fromIndex;
        mEndIndex = endIndex;
        mReverse = reverse;
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

        if (mReverse) {
            interpolatedTime = 1.0f - interpolatedTime;
        }

        curIndex = (int) (mFromIndex + (mEndIndex - mFromIndex) * interpolatedTime);

        if (null != mListener) {
            mListener.onAnimUpdate(curIndex);
        }
    }
}