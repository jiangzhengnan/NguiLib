package com.ng.ui.other.parrot;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ng.ui.R;

import java.util.ArrayList;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-05-22
 */
public class NgTestAty extends AppCompatActivity {


    private ParrotView ptv_test;

    private String[] citys = {"California", "Texas", "Florida", "New York", "llinos", "Georgia", "Michigan", "New Jersey", "Pennsylvania", "Virginana",
            "Obhio", "U.S. Virgin Islands", "North Arolina", "South Carolina", "Maryland", "Colorado", "Minnersota", "Arizona", "Northern Marianas", "WuGangShi"};

    private String[] citys3 = {"California", "Texas", "Florida", "palama", "llinos", "Georgia", "Michigan", "New Jersey", "Pennsylvania", "Virginana",
            "Obhio", "U.S. Virgin Islands", "North Arolina", "South Carolina", "Maryland", "Colorado", "Minnersota", "Arizona", "Northern Marianas", "WuGangShi"};


    private float[] value1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};

    private float[] value2 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 20, 19, 18};

    private float[] value3 = {1, 2, 3, 14, 5, 6, 7, 11, 9, 10, 8, 12, 13, 4, 15, 20, 19, 18, 17, 16f};

    private float[] value4 = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};


    private ArrayList<ParrotPillar> mParrotPillars1;
    private ArrayList<ParrotPillar> mParrotPillars2;
    private ArrayList<ParrotPillar> mParrotPillars3;
    private ArrayList<ParrotPillar> mParrotPillars4;


    private void init() {
        Log.d("nangua", "NgTestAty:init");
        getFakeData();

        ptv_test = findViewById(R.id.ptv_test);


        findViewById(R.id.btn_test1).setOnClickListener(v -> {
            getFakeData();
            refreshDateNormal();
        });

        //更新数据,分为两种情况，第一种排序不变值更新，第二种排序和值都更新
        findViewById(R.id.btn_test2).setOnClickListener(v -> {
            getFakeData();
            refreshDateSwtich();
        });

        findViewById(R.id.btn_test3).setOnClickListener(v -> {
            getFakeData();
            refreshDateValue();
        });
        findViewById(R.id.btn_test4).setOnClickListener(v -> {
            getFakeData();
            refreshDateNumber();
        });
    }


    private void getFakeData() {
        mParrotPillars1 = new ArrayList<>();
        for (int i = 0; i < citys.length; i++) {
            mParrotPillars1.add(new ParrotPillar(citys[i], value1[i]));
        }

        mParrotPillars2 = new ArrayList<>();
        for (int i = 0; i < citys.length; i++) {
            mParrotPillars2.add(new ParrotPillar(citys[i], value2[i]));
        }

        mParrotPillars3 = new ArrayList<>();
        for (int i = 0; i < citys.length; i++) {
            mParrotPillars3.add(new ParrotPillar(citys3[i], value3[i]));
        }
        mParrotPillars4 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mParrotPillars4.add(new ParrotPillar(citys[i], value4[i]));
        }
    }

    private void refreshDateNormal() {
        ptv_test.setData(mParrotPillars1);
    }


    private void refreshDateSwtich() {
        ptv_test.setData(mParrotPillars2);
    }

    private void refreshDateValue() {
        ptv_test.setData(mParrotPillars3);
    }


    private void refreshDateNumber() {
        ptv_test.setData(mParrotPillars4);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_parrot);
        init();
    }
}
