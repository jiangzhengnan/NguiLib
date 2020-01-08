package com.ng.ui.textview;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-01-06
 */


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UpDownTextView extends RelativeLayout {
    private Context mContext;
    private TextView textViews[] = new TextView[2];

    private String curText = null;

    /***
     * 动画时间
     */
    private int mAnimTime = 500;

    /**
     * 停留时间
     */
    private int mStillTime = 0;

    /***
     * 轮播的string
     */
    private List<String> mTextList = new ArrayList<>();

    /***
     * 当前轮播的索引
     */
    private int currentIndex = 0;

    /***
     * 动画模式
     */
    private int animMode = 0;// 默认向上 0--向上，1--向下

    /***
     * 是否正在自动滚动
     */
    private boolean isRunning = false;

    public final static int ANIM_MODE_UP = 0;
    public final static int ANIM_MODE_DOWN = 1;

    private TranslateAnimation animationDown, animationUp;

    public UpDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews();
    }

    private void initViews() {
        textViews[0] = addText();
        textViews[1] = addText();

        textViews[1].setVisibility(INVISIBLE);
    }

    /***
     * 当界面销毁时
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoScroll();
        // 防止内存泄漏的操作
//        LogUitls.print("滚动文本","界面销毁移动handler");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!isInEditMode()) {
            post(new Runnable() {//7.0以后，需要界面可见时，设置宽高才有效果
                @Override
                public void run() {
                    setViewsHeight();
                }
            });
        }
    }

    /***
     * 重新设置VIEW的高度
     */
    private void setViewsHeight() {
        for (TextView tv : textViews) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tv.getLayoutParams();
            lp.height = getHeight();
            lp.width = getWidth();
            tv.setLayoutParams(lp);
        }

//        RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) this.getLayoutParams();
//        lp2.height = getHeight();
        //lp2.setMargins(0, -getHeight(), 0, 0);// 使向上偏移一定的高度，用padding,scrollTo都分有问题
       // this.setLayoutParams(lp2);
    }

    // /////////////////////以下是一些基本的方法textView要用到///////////////////////////////////

    public void setGravity(int graty) {
        for (TextView tv : textViews) {
            tv.setGravity(graty);
        }
    }

    public void setTextSize(int dpSize) {
        for (TextView tv : textViews) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dpSize);
        }
    }

    public void setTextColor(int color) {
        for (TextView tv : textViews) {
            tv.setTextColor(color);
        }
    }

    private TextView addText() {
        TextView tv = new TextView(mContext);
        tv.setGravity(Gravity.CENTER);
        this.addView(tv);
        return tv;
    }

    /***
     * 设置初始的字
     *  todo  设置size，style，颜色的属性化
     */
    public void setText(String str) {
        if (curText == null) {
            this.curText = str;
            textViews[0].setText(str);
        } else {
            //change
            textViews[1].setText(str);
            startUpFromBottomAnim(textViews[1]);

        }


    }

    /**
     * 由底上升的anim
     * @param textView
     */
    private void startUpFromBottomAnim(TextView textView) {


    }

    public void changeTxt(String txt) {

    }

    /***
     * 开始自动滚动
     */
    public void startAutoScroll() {
        if (!isRunning) {
            isRunning = true;
            if (mTextList == null || mTextList.size() == 0) {
                stopAutoScroll();
                return;
            }
            this.postDelayed(runnable, mStillTime + mAnimTime);// 可用runnable来代替hander或者 timer
        }
    }

    /***
     * 停止自动滚动
     */
    public void stopAutoScroll() {
        if (isRunning) {
            isRunning = false;
            this.removeCallbacks(runnable);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            currentIndex++;
            currentIndex = (currentIndex) % mTextList.size();
            switch (animMode) {
                case ANIM_MODE_UP:
                    setTextUpAnim(mTextList.get(currentIndex));
                    break;
                case ANIM_MODE_DOWN:
                    setTextDownAnim(mTextList.get(currentIndex));
                    break;
            }
            UpDownTextView.this.postDelayed(runnable, mStillTime + mAnimTime);
            if (onTextScrollListener != null) {
                onTextScrollListener.onTextScroll();
            }
//            LogUitls.print("滚动文本", "界面滚动");
        }
    };

    /***
     * 向上弹动画
     *
     * @param curText
     */
    public void setTextUpAnim(String curText) {
        this.curText = curText;
        textViews[2].setText(curText);
        up();// 向上的动画
    }

    public void setTextDownAnim(String text) {
        this.curText = text;
        textViews[0].setText(text);
        down();// 向上的动画
    }

    public void setDuring(int during) {
        this.mAnimTime = during;
    }

    /***
     * 向上动画
     */
    private void up() {
//        rlayout.clearAnimation();
//        if (animationUp == null)
//            animationUp = new TranslateAnimation(0, 0, 0, -getHeight());
//        animationUp.setDuration(mAnimTime);
//        rlayout.startAnimation(animationUp);
//        animationUp.setAnimationListener(listener);
    }

    /***
     * 向下动画
     */
    public void down() {
//        rlayout.clearAnimation();
//        if (animationDown == null)
//            animationDown = new TranslateAnimation(0, 0, 0, getHeight());
//        animationDown.setDuration(mAnimTime);
//        rlayout.startAnimation(animationDown);
//        animationDown.setAnimationListener(listener);
    }

    /***
     * 动画监听，动画完成后，动画恢复，设置文本
     */
    private AnimationListener listener = new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation arg0) {
            setText(curText);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    public int getAnimTime() {
        return mAnimTime;
    }

    public void setAnimTime(int mAnimTime) {
        this.mAnimTime = mAnimTime;
    }

    public int getStillTime() {
        return mStillTime;
    }

    public void setStillTime(int mStillTime) {
        this.mStillTime = mStillTime;
    }

    public List<String> getTextList() {
        return mTextList;
    }

    public void setTextList(String str) {
        this.mTextList.add(str);

        setText(mTextList.get(0));
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getAnimMode() {
        return animMode;
    }

    public void setAnimMode(int animMode) {
        this.animMode = animMode;
    }

    public void setSingleLine() {
        for (TextView tv : textViews) {
            tv.setSingleLine();
            tv.setEllipsize(TextUtils.TruncateAt.END);//尾部打省略号
        }
    }

    OnTextScrollListener onTextScrollListener;

    public void setOnTextScrollListener(OnTextScrollListener onTextScrollListener) {
        this.onTextScrollListener = onTextScrollListener;
    }

    public interface OnTextScrollListener {
        void onTextScroll();
    }

}
