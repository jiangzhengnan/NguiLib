package com.ng.ui.other.inhale;

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
    private InhaleView mInhaleView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_inhale);
        mTipContainerView = findViewById(R.id.mTipContainerView);


        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            boolean mReverse = false;

            @Override
            public void onClick(View v) {
                if (mInhaleView.startAnimation(mReverse)) {
                    mReverse = !mReverse;
                }
            }
        });


        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_test);

        mBitmap = resizeImage(mBitmap, 800, 500);

        showTradeIntroduceAnimView(mBitmap);

    }


    private void showTradeIntroduceAnimView(Bitmap bitmap) {
        mInhaleView = new InhaleView(this);


        mInhaleView.setIsDebug(true);
        mInhaleView.setmBitmap(bitmap);
        //target: 980  1982
        mInhaleView.setTargetPosition(800,100 , 1080 , 1982);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);



        mTipContainerView.addView(mInhaleView, lp);
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

    /**
     * 输入：arr = [1,4,2,5,3]
     * 输出：58
     * 解释：所有奇数长度子数组和它们的和为：
     * [1] = 1
     * [4] = 4
     * [2] = 2
     * [5] = 5
     * [3] = 3
     * [1,4,2] = 7
     * [4,2,5] = 11
     * [2,5,3] = 10
     * [1,4,2,5,3] = 15
     * 我们将所有值求和得到 1 + 4 + 2 + 5 + 3 + 7 + 11 + 10 + 15 = 58
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/sum-of-all-odd-length-subarrays
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     */

    class Solution {
        public int sumOddLengthSubarrays(int[] arr) {

            return 0;
        }
    }
}