package com.ng.ui.main;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-04-11
 */

import com.ng.ui.R;

import java.util.ArrayList;


public class ItemInfo {
    public String name;
    public String description;
    private static String[] names = {"item1", "item2", "item3", "item4"};
    private static String[] descriptions = {"This is  item1's description", "This is  item2's description", "This is  item3's description", "This is  item4's description"};

    public ItemInfo() {
        name = "";
        description = "";
    }

    public ItemInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static ArrayList<ItemInfo> getDefaultList() {
        ArrayList<ItemInfo> itemInfos = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            itemInfos.add(new ItemInfo(names[i], descriptions[i]));
        }
        return itemInfos;
    }
}
