package com.ng.ui.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.ng.ui.R;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-01-08
 */
public class TestActivity extends Activity implements View.OnClickListener {
    LiziView test_lizi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        test_lizi = findViewById(R.id.test_lizi);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_pause).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                test_lizi.startAnim();
                break;
            case R.id.btn_pause:
                test_lizi.stopAnim();
                break;
        }
    }
}
