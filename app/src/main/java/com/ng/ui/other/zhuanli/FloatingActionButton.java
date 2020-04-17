package com.ng.ui.other.zhuanli;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.ng.ui.R;

public class FloatingActionButton extends View {

    private Drawable mIcon;
    private Drawable mPrevIcon;
    private int mAnimDuration = -1;
    private Interpolator mInterpolator;
    private SwitchIconAnimator mSwitchIconAnimator;
    private int mIconSize = -1;

    protected int mStyleId;

    public static FloatingActionButton make(Context context, int resId) {
        return new FloatingActionButton(context, null, resId);
    }

    public FloatingActionButton(Context context) {
        super(context);

        init(context, null, 0, 0);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, 0, 0);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr, 0);
    }

    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        setClickable(true);
        mSwitchIconAnimator = new SwitchIconAnimator();
        applyStyle(context, attrs, defStyleAttr, defStyleRes);

    }

    protected void applyStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionButton, defStyleAttr, defStyleRes);

        int radius = -1;
        int elevation = -1;
        ColorStateList bgColor = null;
        int bgAnimDuration = -1;
        int iconSrc = 0;
        int iconLineMorphing = 0;

        for (int i = 0, count = a.getIndexCount(); i < count; i++) {
            int attr = a.getIndex(i);

            if (attr == R.styleable.FloatingActionButton_fab_radius)
                radius = a.getDimensionPixelSize(attr, 0);
            else if (attr == R.styleable.FloatingActionButton_fab_elevation)
                elevation = a.getDimensionPixelSize(attr, 0);
            else if (attr == R.styleable.FloatingActionButton_fab_backgroundColor)
                bgColor = a.getColorStateList(attr);
            else if (attr == R.styleable.FloatingActionButton_fab_backgroundAnimDuration)
                bgAnimDuration = a.getInteger(attr, 0);
            else if (attr == R.styleable.FloatingActionButton_fab_iconSrc)
                iconSrc = a.getResourceId(attr, 0);
            else if (attr == R.styleable.FloatingActionButton_fab_iconLineMorphing)
                iconLineMorphing = a.getResourceId(attr, R.style.LightFloatingActionButtonIcon);
            else if (attr == R.styleable.FloatingActionButton_fab_iconSize)
                mIconSize = a.getDimensionPixelSize(attr, 0);
            else if (attr == R.styleable.FloatingActionButton_fab_animDuration)
                mAnimDuration = a.getInteger(attr, 0);
            else if (attr == R.styleable.FloatingActionButton_fab_interpolator) {
                int resId = a.getResourceId(R.styleable.FloatingActionButton_fab_interpolator, 0);
                if (resId != 0)
                    mInterpolator = AnimationUtils.loadInterpolator(context, resId);
            }
        }

        a.recycle();

        if (mIconSize <= 0)
            mIconSize = ThemeUtil.dpToPx(context, 24);

        if (mAnimDuration < 0)
            mAnimDuration = context.getResources().getInteger(android.R.integer.config_mediumAnimTime);

        if (mInterpolator == null)
            mInterpolator = new DecelerateInterpolator();

        if (radius < 0)
            radius = ThemeUtil.dpToPx(context, 28);

        if (elevation < 0)
            elevation = ThemeUtil.dpToPx(context, 4);

        if (bgColor == null)
            bgColor = ColorStateList.valueOf(ThemeUtil.colorAccent(context, 0));

        if (bgAnimDuration < 0)
            bgAnimDuration = 0;


        setIcon(new LineMorphingDrawable.Builder(context, iconLineMorphing).build(), false);
    }


    /**
     * @return The line state of LineMorphingDrawable that is used as this button's icon.
     */
    public int getLineMorphingState() {
        if (mIcon != null && mIcon instanceof LineMorphingDrawable)
            return ((LineMorphingDrawable) mIcon).getLineState();

        return -1;
    }

    /**
     * Set the line state of LineMorphingDrawable that is used as this button's icon.
     *
     * @param state     The line state.
     * @param animation Indicate should show animation when switch line state or not.
     */
    public void setLineMorphingState(int state, boolean animation) {
        if (mIcon != null && mIcon instanceof LineMorphingDrawable)
            ((LineMorphingDrawable) mIcon).switchLineState(state, animation);
    }


    /**
     * @return The drawable is used as this button's icon.
     */
    public Drawable getIcon() {
        return mIcon;
    }

    /**
     * Set the drawable that is used as this button's icon.
     *
     * @param icon      The drawable.
     * @param animation Indicate should show animation when switch drawable or not.
     */
    public void setIcon(Drawable icon, boolean animation) {
        if (icon == null)
            return;

        if (animation) {
            mSwitchIconAnimator.startAnimation(icon);
            invalidate();
        } else {
            if (mIcon != null) {
                mIcon.setCallback(null);
                unscheduleDrawable(mIcon);
            }

            mIcon = icon;
            float half = mIconSize / 2f;
            mIcon.setBounds(0, 0, (int) half, (int) half);
            mIcon.setCallback(this);
            invalidate();
        }
    }


    /**
     * Show this button at the specific location. If this button isn't attached to any parent view yet,
     * it will be add to activity's root view. If not, it will just update the location.
     *
     * @param activity The activity that this button will be attached to.
     * @param x        The x value of anchor point.
     * @param y        The y value of anchor point.
     * @param gravity  The gravity apply with this button.
     * @see Gravity
     */
    public void show(Activity activity, int x, int y, int gravity) {
        if (getParent() == null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(getMeasuredWidth(), getMeasuredHeight());

            activity.getWindow().addContentView(this, params);
        } else
            updateLocation(x, y, gravity);
    }

    /**
     * Show this button at the specific location. If this button isn't attached to any parent view yet,
     * it will be add to activity's root view. If not, it will just update the location.
     *
     * @param parent  The parent view. Should be {@link FrameLayout} or
     * @param x       The x value of anchor point.
     * @param y       The y value of anchor point.
     * @param gravity The gravity apply with this button.
     * @see Gravity
     */
    public void show(ViewGroup parent, int x, int y, int gravity) {
        if (getParent() == null) {
            ViewGroup.LayoutParams params = parent.generateLayoutParams(null);
            params.width = getMeasuredWidth();
            params.height = getMeasuredHeight();

            parent.addView(this, params);
        } else
            updateLocation(x, y, gravity);
    }

    /**
     * Update the location of this button. This method only work if it's already attached to a parent view.
     *
     * @param x       The x value of anchor point.
     * @param y       The y value of anchor point.
     * @param gravity The gravity apply with this button.
     * @see Gravity
     */
    public void updateLocation(int x, int y, int gravity) {
        if (getParent() != null)
        {}
        else
            Log.v(FloatingActionButton.class.getSimpleName(), "updateLocation() is called without parent");
    }


    private void setLeftMargin(ViewGroup.LayoutParams params, int value) {
        if (params instanceof ViewGroup.MarginLayoutParams)
            ((ViewGroup.MarginLayoutParams) params).leftMargin = value;
        else
            Log.v(FloatingActionButton.class.getSimpleName(), "cannot recognize LayoutParams: " + params);
    }

    private void setTopMargin(ViewGroup.LayoutParams params, int value) {
        if (params instanceof ViewGroup.MarginLayoutParams)
            ((ViewGroup.MarginLayoutParams) params).topMargin = value;
        else
            Log.v(FloatingActionButton.class.getSimpleName(), "cannot recognize LayoutParams: " + params);
    }

    /**
     * Remove this button from parent view.
     */
    public void dismiss() {
        if (getParent() != null)
            ((ViewGroup) getParent()).removeView(this);
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) ||  mIcon == who || mPrevIcon == who;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mIcon != null)
            mIcon.setState(getDrawableState());
        if (mPrevIcon != null)
            mPrevIcon.setState(getDrawableState());
    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        if (mPrevIcon != null)
            mPrevIcon.draw(canvas);
        if (mIcon != null)
            mIcon.draw(canvas);
    }



    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);

        ss.state = getLineMorphingState();
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;

        super.onRestoreInstanceState(ss.getSuperState());
        if (ss.state >= 0)
            setLineMorphingState(ss.state, false);
        requestLayout();
    }

    static class SavedState extends BaseSavedState {
        int state;

        /**
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            state = in.readInt();
        }

        @Override
        public void writeToParcel(@NonNull Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(state);
        }

        @Override
        public String toString() {
            return "FloatingActionButton.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " state=" + state + "}";
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    class SwitchIconAnimator implements Runnable {

        boolean mRunning = false;
        long mStartTime;

        public void resetAnimation() {
            mStartTime = SystemClock.uptimeMillis();
            mIcon.setAlpha(0);
            mPrevIcon.setAlpha(255);
        }

        public boolean startAnimation(Drawable icon) {
            if (mIcon == icon)
                return false;

            mPrevIcon = mIcon;
            mIcon = icon;
            float half = mIconSize / 2f;
            mIcon.setBounds(0, 0, (int) half, (int) half);
            mIcon.setCallback(FloatingActionButton.this);

            if (getHandler() != null) {
                resetAnimation();
                mRunning = true;
                getHandler().postAtTime(this, SystemClock.uptimeMillis() + 1000/60);
            } else {
                mPrevIcon.setCallback(null);
                unscheduleDrawable(mPrevIcon);
                mPrevIcon = null;
            }

            invalidate();
            return true;
        }

        public void stopAnimation() {
            mRunning = false;
            mPrevIcon.setCallback(null);
            unscheduleDrawable(mPrevIcon);
            mPrevIcon = null;
            mIcon.setAlpha(255);
            if (getHandler() != null)
                getHandler().removeCallbacks(this);
            invalidate();
        }

        @Override
        public void run() {
            long curTime = SystemClock.uptimeMillis();
            float progress = Math.min(1f, (float) (curTime - mStartTime) / mAnimDuration);
            float value = mInterpolator.getInterpolation(progress);

            mIcon.setAlpha(Math.round(255 * value));
            mPrevIcon.setAlpha(Math.round(255 * (1f - value)));

            if (progress == 1f)
                stopAnimation();

            if (mRunning) {
                if (getHandler() != null)
                    getHandler().postAtTime(this, SystemClock.uptimeMillis() + 1000/60);
                else
                    stopAnimation();
            }

            invalidate();
        }

    }
}
