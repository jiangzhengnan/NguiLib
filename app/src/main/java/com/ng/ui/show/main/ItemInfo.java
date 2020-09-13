package com.ng.ui.show.main;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-04-11
 */

import androidx.fragment.app.Fragment;


public class ItemInfo {
    public String name;
    public Fragment fragment;

    @Override
    public String toString() {
        return "ItemInfo{" +
                "name='" + name + '\'' +
                '}';
    }

    public ItemInfo(String name, Fragment fragment) {
        this.name = name;
        this.fragment = fragment;
    }
}
