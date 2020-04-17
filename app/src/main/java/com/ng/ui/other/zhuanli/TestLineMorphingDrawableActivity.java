package com.ng.ui.other.zhuanli;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.ng.ui.R;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-04-17
 */
public class TestLineMorphingDrawableActivity extends Activity implements View.OnClickListener {

    private FloatingActionButton btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fb);

        btn = findViewById(R.id.button_bt_float);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setLineMorphingState((btn.getLineMorphingState() + 1) % 2, true);

            }
        });
    }


    @Override
    public void onClick(View v) {

    }
}

