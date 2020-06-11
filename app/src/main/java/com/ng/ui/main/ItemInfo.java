package com.ng.ui.main;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-04-11
 */

import androidx.fragment.app.Fragment;

import com.ng.ui.R;

import java.util.ArrayList;


public class ItemInfo {
    public String name;
    public Fragment fragment;

    public ItemInfo(String name, Fragment fragment) {
        this.name = name;
        this.fragment = fragment;
    }
}
