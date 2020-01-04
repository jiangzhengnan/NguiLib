package com.ng.ui.testxiru.other;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-01-03
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.ng.ui.R;


public class InhaleAnimationActivity extends Activity {

    RelativeLayout mTipContainerView;
    private static final boolean DEBUG_MODE = false;
    private SampleView mSampleView = null;

    private int mTradeIntroduceAnimViewWidth;
    private int mTradeIntroduceAnimViewHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_inhale);
        mTipContainerView = findViewById(R.id.mTipContainerView);


        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            boolean mReverse = false;

            @Override
            public void onClick(View v) {
                if (mSampleView.startAnimation(mReverse)) {
                    mReverse = !mReverse;
                }
            }
        });

        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_test);

        mBitmap = resizeImage(mBitmap, 700, 1100);

        showTradeIntroduceAnimView(mBitmap);

    }


    private void showTradeIntroduceAnimView(Bitmap bitmap) {
        mSampleView = new SampleView(this);
        mSampleView.setIsDebug(true);
        mSampleView.setmBitmap(bitmap);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);



        mTipContainerView.addView(mSampleView, lp);
     }

    public Bitmap resizeImage(Bitmap bitmap, int width, int height) {
        int bmpWidth = bitmap.getWidth();
        int bmpHeight = bitmap.getHeight();

        float scaleWidth = ((float) width) / bmpWidth;
        float scaleHeight = ((float) height) / bmpHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, true);
    }

}