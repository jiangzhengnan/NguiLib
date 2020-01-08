package com.ng.ui.test;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.ng.ui.R;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-01-08
 */
public class TestActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

}
