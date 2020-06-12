package com.ng.ui.show.main;

import android.content.Context;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-04-11
 */
public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener
{
    //第一个参数表示当前页面的序号；第二个参数表示当前页面偏移的百分比，取值为0到1；第三个参数表示当前页面的偏移距离
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        //Toast.makeText(context, name, Toast.LENGTH_SHORT).show();

    }

    //position代表当前选中的页面
    public void onPageSelected(Context context, int position,String name) {
        Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
    }

    // 翻页状态改变时触发。state取值说明为：0表示静止，1表示正在滑动，2表示滑动完毕
    public void onPageScrollStateChanged(int state) {
    }
}
