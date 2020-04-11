package com.ng.ui.main;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-04-11
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.ng.ui.R;

import java.util.ArrayList;

public class MyViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<View> views  = new ArrayList<>();//这是PagerView下面挂的View的集合
    private ArrayList<ItemInfo> itemInfos = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public MyViewPagerAdapter(Context context, ArrayList<ItemInfo> itemInfos) {
        //传入的设备上下文主要用于获取布局文件
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.itemInfos = itemInfos;
        for(int i = 0; i < itemInfos.size(); i++) {
            //获取布局文件
            View view = layoutInflater.inflate(R.layout.item_vp, null);
            TextView textView = view.findViewById(R.id.tv_description);
            textView.setText(itemInfos.get(i).description);
            views.add(view);
        }
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    //销毁指定的页面
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(views.get(position));
    }

    @NonNull
    @Override
    //实例化指定位置页面 并将其添加到容器中
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    @Nullable
    @Override
    //获取指定页面的标题
    public CharSequence getPageTitle(int position) {
        return itemInfos.get(position).name;
    }
}
