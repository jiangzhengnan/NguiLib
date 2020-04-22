package com.ng.ui.study.inhale;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ng.ui.R;

/**
 * 描述:inhale学习aty
 *
 * @author Jzn
 * @date 2020-04-20
 */
public class InhaleStudyActivity extends AppCompatActivity implements View.OnClickListener {
    private MyInhaleView mih_test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inhale_study);
        findViewById(R.id.btn_test).setOnClickListener(this);

        mih_test = findViewById(R.id.mih_test);


        //准备bitmap
        Bitmap mBitmap = resizeBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.test), 500, 500);
        mih_test.setBitmap(mBitmap);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_test:
                mih_test.startAnimation();

                break;
        }
    }

    public Bitmap resizeBitmap(Bitmap bitmap, int width, int height) {
        int bmpWidth = bitmap.getWidth();
        int bmpHeight = bitmap.getHeight();
        float scaleWidth = ((float) width) / bmpWidth;
        float scaleHeight = ((float) height) / bmpHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, true);
    }
}
