package com.ng.ui.other.pickerview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.DrawableRes;

import com.ng.ui.R;
import com.ng.ui.other.pickerview.WheelView.WheelPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: 高亮风格的数组选择器，支持步长选择
 * <p>
 * 上次选择的项 后面会带一个小旗子0.0
 *
 * @author Jzn
 * @date 2020-04-07
 */
public class HighLightRangePickerDialog extends Dialog {
    WheelPicker option_picker;
    private List<String> mData;

    public HighLightRangePickerDialog(Context context) {
        super(context, R.style.dialog_style);
    }


    //标记值
    private String tag;
    private int tagRes = 0;

    public void setTag(String tag, @DrawableRes int tagRes) {
        this.tag = tag;
        this.tagRes = tagRes;
    }

    private WheelPicker.OnItemSelectedListener mOnItemSelectedListener;

    public void setmOnItemSelectedListener(WheelPicker.OnItemSelectedListener mOnItemSelectedListener) {
        this.mOnItemSelectedListener = mOnItemSelectedListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_hight_light_range_picker);
        initView();

    }

    private void initView() {
        option_picker = findViewById(R.id.option_picker);
        option_picker.setData(mData);
        option_picker.setTag(tag, tagRes);
        option_picker.setOnItemSelectedListener(mOnItemSelectedListener);

    }

    public HighLightRangePickerDialog setData(List<String> data) {
        if (data != null && data.size() != 0) {
            this.mData = data;
        } else {
            this.mData = new ArrayList<>();
            mData.add("--");
        }
        return this;
    }

    public HighLightRangePickerDialog build() {
        return this;
    }
}
