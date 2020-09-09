package com.ng.ui.other.textview;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ng.nguilib.utils.MLog;
import com.ng.ui.R;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-01-06
 */
public class UpChangeTextView extends RelativeLayout {
    private Context mContext;
    private TextView mTvs[] = new TextView[2];
    private String curText = null;
    private int mChangeAnimTime = 200;
    private int mAppearAnimTime = 200;
    private boolean isRunning = false;
    private boolean isDetaching = false;
    private AnimatorSet mAnimatorSet;

    //txt param
    private ColorStateList mTextColor;
    private int mTextSize;

    public ColorStateList getmTextColor() {
        return mTextColor;
    }

    public void setmTextColor(ColorStateList mTextColor) {
        this.mTextColor = mTextColor;
        refreshView();
    }

    public int getmTextSize() {
        return mTextSize;
    }

    public void setmTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
        refreshView();
    }

    private void refreshView() {
        this.removeAllViews();
        mTvs[0] = addText();
        mTvs[1] = addText();
        mTvs[1].setVisibility(INVISIBLE);
        if (!TextUtils.isEmpty(curText)) {
            mTvs[0].setText(curText);
        }
    }

    public UpChangeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews(attrs);
    }

    private void initViews(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.UpChangeTextView);
            curText = t.getString(R.styleable.UpChangeTextView_up_change_tv_text);
            mTextColor = t.getColorStateList(R.styleable.UpChangeTextView_up_change_tv_textColor);
            mTextSize = t.getDimensionPixelSize(R.styleable.UpChangeTextView_up_change_tv_textSize, 10);
            t.recycle();
        }
         refreshView();
    }

    private TextView addText() {
        TextView tv = new TextView(mContext);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        if (mTextColor != null) {
            tv.setTextColor(mTextColor);
        }
        tv.setTextSize(mTextSize);
        this.addView(tv);
        return tv;
    }


    // 防止内存泄漏的操作
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isDetaching = true;
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
            mAnimatorSet.end();
            mAnimatorSet = null;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!isInEditMode()) {
            post(this::setViewsHeight);
        }
    }

    /***
     * 重新设置VIEW的高度
     */
    private void setViewsHeight() {
        for (TextView tv : mTvs) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tv.getLayoutParams();
            lp.height = getHeight();
            lp.width = getWidth();
            tv.setLayoutParams(lp);
        }
    }


    //出现
    public void startAppearAnim() {
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
        alphaAnimation.setDuration(mAppearAnimTime);
        alphaAnimation.start();
    }

    //消失
    public void startDisAppearAnim() {
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
        alphaAnimation.setDuration(mAppearAnimTime);
        alphaAnimation.start();
    }


    public void setText(String str) {
        MLog.INSTANCE.d("str: " + str);
        if (curText == null) {
            this.curText = str;
            mTvs[0].setText(str);
        } else {
            curText = str;
            //change
            mTvs[1].setText(str);
            mTvs[1].setVisibility(VISIBLE);

            //0  ⬆
            PropertyValuesHolder tsy1 = PropertyValuesHolder.ofFloat("translationY", 0, -getHeight() / 3);
            PropertyValuesHolder scaleY1 = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.5f);
            PropertyValuesHolder scaleX1 = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.5f);
            PropertyValuesHolder alpha1 = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
            ObjectAnimator anim1 = ObjectAnimator.ofPropertyValuesHolder(mTvs[0], tsy1, scaleY1, scaleX1, alpha1);

            //1  ⬆
            PropertyValuesHolder tsY0 = PropertyValuesHolder.ofFloat("translationY", getHeight() / 3, 0);
            PropertyValuesHolder scaleY0 = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1f);
            PropertyValuesHolder scaleX0 = PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1f);
            PropertyValuesHolder alpha0 = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
            ObjectAnimator anim0 = ObjectAnimator.ofPropertyValuesHolder(mTvs[1], tsY0, scaleY0, scaleX0, alpha0);
            //集合调用
            mAnimatorSet = new AnimatorSet();
            mAnimatorSet.playTogether(anim0, anim1);
            mAnimatorSet.setDuration(mChangeAnimTime);
            mAnimatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    isRunning = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (!isDetaching) {
                        mTvs[0].setText(str);
                    }
                    isRunning = false;
                }
            });
            if (!isRunning && !isDetaching)
                mAnimatorSet.start();
        }
    }


}
