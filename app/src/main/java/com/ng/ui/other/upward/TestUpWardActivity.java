package com.ng.ui.other.upward;


import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.ng.ui.R;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-01-06
 */
public class TestUpWardActivity extends AppCompatActivity {
    UpWardAnimLayout mUpWardAnimLayout;
    Runnable runnable = new Runnable() {

        public void run() {
            mUpWardAnimLayout.startAnim();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_upward);
        mUpWardAnimLayout = findViewById(R.id.test_upward);
        Handler handler = new Handler();

        handler.postDelayed(runnable, 1000);

    }

}
