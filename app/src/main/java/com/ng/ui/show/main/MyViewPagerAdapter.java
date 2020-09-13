package com.ng.ui.show.main;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-04-11
 */

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    private List<ItemInfo> infoLost;

    public void setInfoLost(List<ItemInfo> infoLost) {
        this.infoLost = infoLost;
    }

    public MyViewPagerAdapter(FragmentManager fm, List<ItemInfo> infoLost){
        super(fm);
        this.infoLost = infoLost;
    }

    @Override
    public long getItemId(int position) {
        return infoLost.get(position).hashCode();
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    /**
     * 得到每个页面
     */
    @Override
    public Fragment getItem(int arg0) {
        return (infoLost == null || infoLost.size() == 0) ? null : infoLost.get(arg0).fragment;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    /**
     * 每个页面的title
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return (infoLost.size() > position) ? infoLost.get(position).name : "";
    }

    /**
     * 页面的总个数
     */
    @Override
    public int getCount() {
        return infoLost == null ? 0 : infoLost.size();
    }
}
