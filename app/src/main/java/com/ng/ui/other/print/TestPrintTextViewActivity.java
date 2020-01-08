package com.ng.ui.other.print;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ng.ui.R;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-01-06
 */
public class TestPrintTextViewActivity extends Activity {
    PrinterTextView ptv_test1;
    PrinterTextView ptv_test2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_print);

        ptv_test1 = findViewById(R.id.ptv_test1);
        ptv_test2 = findViewById(R.id.ptv_test2);

        ptv_test1.setPrintText(getString(R.string.guide_robot_str_1));
        ptv_test1.setWidth((int) getTextViewLength(ptv_test1,getString(R.string.guide_robot_str_1)));
        ptv_test2.setPrintText(getString(R.string.guide_robot_str_2));
        ptv_test2.setWidth((int) getTextViewLength(ptv_test2,getString(R.string.guide_robot_str_2)));


        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ptv_test1.startPrint(() -> {
                    ptv_test2.startPrint(() -> {

                    });
                });
            }
        });

    }

    // 计算出该TextView中文字的长度(像素)
    public static float getTextViewLength(TextView textView, String text) {
        TextPaint paint = textView.getPaint();
        // 得到使用该paint写上text的时候,像素为多少
        return paint.measureText(text);
    }
}
