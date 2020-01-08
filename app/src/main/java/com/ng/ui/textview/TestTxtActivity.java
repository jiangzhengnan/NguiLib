package com.ng.ui.textview;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ng.ui.R;

import java.util.Random;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-01-06
 */
public class TestTxtActivity extends AppCompatActivity {
    UpDownTextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_txt);

        textView = findViewById(R.id.textView1);
        textView.setText("初始化");

        findViewById(R.id.test_btn).setOnClickListener(arg0 -> {
            int ranDom = new Random().nextInt(100);
            textView.setText("额" + ranDom + "啊");
        });
    }

}
