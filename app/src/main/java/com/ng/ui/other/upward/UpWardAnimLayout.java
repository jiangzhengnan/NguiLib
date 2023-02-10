package com.ng.ui.other.upward;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ng.ui.R;

/**
 * @author : jiangzhengnan.jzn@alibaba-inc.com
 * @creation : 2023/02/10
 * @description :
 */
public class UpWardAnimLayout extends RelativeLayout {

    @NonNull
    private ImageView mUpWardIv1;
    @NonNull
    private ImageView mUpWardIv2;
    @Nullable
    private ObjectAnimator mAnimator1;
    @Nullable
    private ObjectAnimator mAnimator2;

    private int mIconSize = 64;
    private int mAnimDuration = 1500;
    private int mHeight;
    private int mWidth;

    public UpWardAnimLayout(final Context context) {
        super(context);
        init();
    }

    public UpWardAnimLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UpWardAnimLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mUpWardIv1 = new ImageView(getContext());
        mUpWardIv2 = new ImageView(getContext());
        mUpWardIv1.setImageResource(R.drawable.ic_upward);
        mUpWardIv2.setImageResource(R.drawable.ic_upward);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mIconSize, mIconSize);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(mUpWardIv1, lp);
        addView(mUpWardIv2, lp);
        mUpWardIv1.setAlpha(0f);
        mUpWardIv2.setAlpha(0f);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
    }

    public void startAnim() {
        if (mAnimator1 == null || mAnimator2 == null) {
            Log.d("nangua", "height:" + mHeight);
            int length = mIconSize / 2;
            PropertyValuesHolder tsy = PropertyValuesHolder.ofFloat("translationY", length, -length);
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0f, 0f, 1f, 0f, 0f);
            mAnimator1 = ObjectAnimator.ofPropertyValuesHolder(mUpWardIv1, tsy, alpha);
            mAnimator1.setRepeatCount(-1);
            mAnimator1.setDuration(mAnimDuration);

            mAnimator2 = ObjectAnimator.ofPropertyValuesHolder(mUpWardIv2, tsy, alpha);
            mAnimator2.setRepeatCount(-1);
            mAnimator2.setDuration(mAnimDuration);
            mAnimator2.setStartDelay(250);
        }
        mAnimator1.start();
        mAnimator2.start();
    }

    private void stopAnim(ObjectAnimator animator) {
        if (animator != null) {
            animator.cancel();
            animator.end();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnim(mAnimator1);
        stopAnim(mAnimator2);
    }

}
