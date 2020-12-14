package com.ng.ui.test;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ng.nguilib.utils.DensityUtil;
import com.ng.nguilib.view.BoundlessSeekBar;
import com.ng.ui.R;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-01-08
 */
public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        DensityUtil.INSTANCE.setCustomDensity(this,getApplication());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        init();
    }

    BoundlessSeekBar mBr;

    private void init() {
        mBr = findViewById(R.id.test_bar_a);

        mBr.setLastPriceInit(100f,0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


}
