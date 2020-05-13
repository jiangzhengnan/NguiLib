package com.ng.ui.study.anim;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ng.ui.R;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-04-24
 */
public class MyAnimStudyActivity extends AppCompatActivity {
    private PieAnimView pie_test1;
    private PieAnimView pie_test2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_study);
        pie_test1 = findViewById(R.id.pie_test1);
        pie_test2 = findViewById(R.id.pie_test2);


        pie_test1.setAnimDate(PieResolver.getInstance().readShapesFromAssert(this, "test.json"));
        pie_test1.post(() -> pie_test1.showShape());

        pie_test2.setAnimDate(PieResolver.getInstance().readShapesFromAssert(this, "test1.json"));
        pie_test2.post(() -> pie_test2.showShape());

        findViewById(R.id.btn_test).setOnClickListener(v -> startTest());
    }

    private void startTest() {
        pie_test1.changeShape();
        pie_test2.changeShape();
    }


}
