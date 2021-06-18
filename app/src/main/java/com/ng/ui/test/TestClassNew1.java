package com.ng.ui.test;

/*
https://leetcode-cn.com/problems/single-number/
给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。

说明：

你的算法应该具有线性时间复杂度。 你可以不使用额外空间来实现吗？

示例 1:

输入: [2,2,1]
输出: 1
示例 2:

输入: [4,1,2,1,2]
输出: 4


5:101
3:011
4:100
两个二进制对应位相同时，结果为0，否则结果为1。

101
011 = 110

110
011 = 101

110
100 = 011

011
101 = 110

110
100 = 011

 */
public class TestClassNew1 {

    public static void main(String[] args) {

         LogUtils.print(singleNumber(new int[]{4, 1, 2, 1, 2,8,7,6,5,6,7,8,5}) + "");
    }

    public static int singleNumber(int[] nums) {
        int single = 0;
        for (int num : nums) {
            single ^= num;
            LogUtils.print(" " + single);
        }
        return single;
    }
}
