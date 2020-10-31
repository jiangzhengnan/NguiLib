package com.ng.ui.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/*
https://leetcode-cn.com/problems/intersection-of-two-arrays/
给定两个数组，编写一个函数来计算它们的交集。

 

示例 1：

输入：nums1 = [1,2,2,1], nums2 = [2,2]
输出：[2]
示例 2：

输入：nums1 = [4,9,5], nums2 = [9,4,9,8,4]
输出：[9,4]
 

说明：

输出结果中的每个元素一定是唯一的。
我们可以不考虑输出结果的顺序。
 */
public class TestClass3 {

    public static void main(String[] args) {
        intersection(new int[]{4, 9, 5},
                new int[]{9, 4, 9, 8, 9}
        );
    }

    public static int[] intersection(int[] nums1, int[] nums2) {
        ArrayList<Integer> result = new ArrayList<>();

        Set<Integer> map = new HashSet();
        for (int temp : nums1) {
            map.add(temp);
        }
        for (int temp : nums2) {
            map.add(temp);
        }

        LogUtils.print(map.toString());


        int[] min = nums1.length > nums2.length ? nums2 : nums1;
        for (int temp : min) {
            if (map.contains(temp) && !result.contains(temp)) {
                result.add(temp);
            }
        }

        LogUtils.print(result.toString());


        int[] resultList = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            resultList[i] = result.get(i);
        }

        return resultList;
    }

}
