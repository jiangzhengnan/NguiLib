package com.ng.ui.test;

import java.util.HashSet;
import java.util.Iterator;
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

执行用时有点长。。:


 */
public class TestClass3 {

    public static void main(String[] args) {
        intersection(new int[]{4, 9, 5},
                new int[]{9, 4, 9, 8, 9}
        );
    }

    public static int[] intersection(int[] nums1, int[] nums2) {
        Set<Integer> result = new HashSet<>();
        for (int temp : nums1) {
            result.add(temp);
        }
        for (int temp : nums2) {
            result.add(temp);
        }
        Iterator<Integer> iterator = result.iterator();
        while (iterator.hasNext()) {
            int temp = iterator.next();
            if (!contains(nums1, temp) || !contains(nums2, temp)) {
                iterator.remove();
            }
        }

        int[] a = new int[result.size()];
        int index = 0;
        iterator = result.iterator();
        while (iterator.hasNext()) {
            a[index] = iterator.next();
            index++;
        }
        LogUtils.printList(a);
        return a;
    }

    public static boolean contains(int[] temp, int target) {
        for (int i : temp) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }

}
