package com.ng.ui.test;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ng.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-01-08
 */
public class TestActivity extends Activity {
    private RecyclerView mRv;
    private TestAdapter mAdapter;
    private List<TestData> mDatas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mDatas = new ArrayList<>();
        mAdapter = new TestAdapter(this,mDatas);
        for (int i = 0; i < 9; i++) {
            mDatas.add(new TestData("" + i, TestData.TYPE_ITEM));
        }
        mDatas.add(new TestData("标题..", TestData.TYPE_TITLE));
        for (int i = 0; i < 9; i++) {
            mDatas.add(new TestData("" + i, TestData.TYPE_ITEM));
        }
        mRv = findViewById(R.id.test_rv);
        mRv.setAdapter(mAdapter);

    }

}
