package com.ng.ui.other.textview;

import android.os.Bundle;
import android.view.View;

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
    UpChangeTextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_txt);

        textView = findViewById(R.id.textView1);

        findViewById(R.id.test_btn1).setOnClickListener(arg0 -> {
            int ranDom = new Random().nextInt(100);
            textView.setText("额" + ranDom + "啊");
        });

        findViewById(R.id.test_btn2).setOnClickListener(new View.OnClickListener() {
            boolean isApper = false;

            @Override
            public void onClick(View v) {
                if (isApper) {
                    textView.startAppearAnim();
                } else {
                    textView.startDisAppearAnim();
                }
                isApper = !isApper;
            }
        });
    }

}
