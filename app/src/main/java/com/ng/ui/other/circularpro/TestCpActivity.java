package com.ng.ui.other.circularpro;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.ng.ui.R;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-01-06
 */
public class TestCpActivity extends AppCompatActivity {

//    CircularProgressView mProgressView;

    CircularAnimProgressView mCircularAnimProgressView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_cp);
//        mProgressView = findViewById(R.id.progress_circular);
//        mProgressView.setValue(0.3F, 0.5F, 0.8F);

        mCircularAnimProgressView = findViewById(R.id.progress_circular_anim);
        mCircularAnimProgressView.setData("已学习计划", "0个");

    }

}
