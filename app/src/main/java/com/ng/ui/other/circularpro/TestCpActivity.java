package com.ng.ui.other.circularpro;


import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.ng.ui.R;
import com.ng.ui.other.upward.UpWardAnimLayout;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-01-06
 */
public class TestCpActivity extends AppCompatActivity {

    CircularProgressView mProgressView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_cp);
        mProgressView = findViewById(R.id.progress_circular);
        mProgressView.setValue(0.3F, 0.5F, 0.8F);

    }

}
