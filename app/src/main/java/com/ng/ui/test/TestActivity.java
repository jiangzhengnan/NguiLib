package com.ng.ui.test;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ng.nguilib.utils.LogUtils;
import com.ng.ui.MainActivity;
import com.ng.ui.R;
import com.ng.ui.other.lizi.LiziView;
import com.ng.ui.other.pickerview.HighLightRangePickerDialog;
import com.ng.ui.other.pickerview.PickerView;
import com.ng.ui.other.pickerview.WheelView.WheelPicker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-01-08
 */
public class TestActivity extends Activity implements View.OnClickListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        showDialog();
    }

    private void showDialog() {
        List<String> a = new ArrayList();
        for (int i = 0; i < 100; i++) {
            a.add("183." + i);
        }

        HighLightRangePickerDialog build = new HighLightRangePickerDialog(this)
                .setData(a)
                .build();
        build.setTag("183.0", R.mipmap.ic_icon);
        build.setmOnItemSelectedListener((picker, data, position) -> LogUtils.INSTANCE.d(position + "data:  " + data));
        build.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


}
