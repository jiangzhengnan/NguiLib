package com.ng.ui.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import com.ng.ui.R;
import com.ng.ui.other.path.NgTouchPathView;

/**
 * @author : jiangzhengnan.jzn@alibaba-inc.com
 * @creation : 2022/08/04
 * @description :
 */
public class TestActivity extends Activity {
    private NgTouchPathView ngTouchPathView;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ngTouchPathView = findViewById(R.id.ngTouchPathView);
        ngTouchPathView.setPathCallBack(new NgTouchPathView.OnTouchPathCallBack() {
            @Override
            public void onClick() {
                Log.d("nangua","点击");
            }

            @Override
            public void onPathClick() {
                Log.d("nangua","滑倒路径点击");
            }
        });
    }

}
