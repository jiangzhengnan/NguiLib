package com.ng.ui.study.anim;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ng.nguilib.utils.LogUtils;
import com.ng.ui.R;

import java.util.List;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-04-24
 */
public class MyAnimStudyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_study);


        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTest();
            }
        });
    }

    private void startTest() {

        List<PieAnimShape> pieAnimShapes = PieResolver.getInstance().readShapesFromAssert(this, "check_cross.xml");
        LogUtils.INSTANCE.d("result:" + pieAnimShapes.toString());
    }


}
