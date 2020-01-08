package com.ng.ui.other.gesture;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.ng.nguilib.utils.LogUtils;
import com.ng.ui.R;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2019-12-23
 */
public class TestGuochangAty extends AppCompatActivity {
    RelativeLayout mTipContainerView;

    Button btn_test;
    TextView btn_target;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testsajdasjdals);


        btn_test = findViewById(R.id.btn_test);
        btn_target = findViewById(R.id.btn_target);
        mTipContainerView = findViewById(R.id.mTipContainerView);

        ViewCompat.setTransitionName(btn_test, "btn_test");


        btn_test.setOnClickListener(v -> {
            startTradeIntroduceAnim(BitmapFactory.decodeResource(getResources(), R.drawable.ic_test));
        });

    }

    private ImageView mTradeIntroduceAnimView;

    private void startTradeIntroduceAnim(Bitmap bitmap) {
        if (mTradeIntroduceAnimView == null) {
            mTradeIntroduceAnimView = new ImageView(this);
        }
        mTradeIntroduceAnimView.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.width = bitmap.getWidth();
        lp.height = bitmap.getHeight();
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        mTradeIntroduceAnimView.setImageBitmap(bitmap);
        mTipContainerView.removeView(mTradeIntroduceAnimView);
        mTipContainerView.addView(mTradeIntroduceAnimView, lp);
        int[] endLocation = getTradeHelpBtnLocation();

        Point startPoint = new Point();
        startPoint.x = ScreenUtils.getWidth(this) / 2;
        startPoint.y = ScreenUtils.getHeight(this) / 2;

        Point endPoint = new Point();
        endPoint.x = endLocation[0];
        endPoint.y = endLocation[1];


        //中心点
        Point controlPoint = new Point();
        controlPoint.x = (startPoint.x + endPoint.x) / 2;
        controlPoint.y = -ScreenUtils.dip2px(this, 100);
        //路径动画
        ValueAnimator pathAnim = ValueAnimator.ofObject(new PointEvaluator(controlPoint), startPoint, endPoint);
        pathAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Point resultPoint = (Point) valueAnimator.getAnimatedValue();
                LogUtils.INSTANCE.d(resultPoint.toString());

                mTradeIntroduceAnimView.setX(resultPoint.x - bitmap.getWidth() / 2);
                mTradeIntroduceAnimView.setY(resultPoint.y - bitmap.getHeight() / 2);
            }
        });

        //图片动画
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f);
        ObjectAnimator imgAnim = ObjectAnimator.ofPropertyValuesHolder(mTradeIntroduceAnimView, scaleX, scaleY);
         imgAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mTradeIntroduceAnimView.setVisibility(View.GONE);
            }

        });
        //集合调用
        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.playTogether(pathAnim, imgAnim);
        animatorSet2.setDuration(2000);
        animatorSet2.start();
    }

    private int[] getTradeHelpBtnLocation() {
        int[] location = new int[2];
        if (btn_target != null) {
            btn_target.getLocationOnScreen(location);
            location[0] += btn_target.getWidth() / 2;
            location[1] -= btn_target.getHeight() / 2;
        }
        return location;
    }
}
