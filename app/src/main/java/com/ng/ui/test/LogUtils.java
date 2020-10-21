package com.ng.ui.test;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020/10/21
 */
public class LogUtils {
    public static void print(String str) {
        System.out.println(str);
    }

    public static void printList(int[] list) {
        StringBuilder listStr = new StringBuilder();
        for (int temp : list) {
            listStr.append(temp + " ");
        }
        System.out.println(listStr.toString());
    }
}
