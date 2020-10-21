package com.ng.ui.test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
https://leetcode-cn.com/problems/cells-with-odd-values-in-a-matrix/
给你一个 n 行 m 列的矩阵，最开始的时候，每个单元格中的值都是 0。

另有一个索引数组 indices，indices[i] = [ri, ci] 中的 ri 和 ci 分别表示指定的行和列（从 0 开始编号）。

你需要将每对 [ri, ci] 指定的行和列上的所有单元格的值加 1。

请你在执行完所有 indices 指定的增量操作后，返回矩阵中 「奇数值单元格」 的数目。
 */
public class TestClass {

    public static void main(String[] args) {
        int[][] indices = new int[][]{
                new int[]{0, 1},
                new int[]{1, 1},
        };

        LogUtils.print(oddCells(2, 3, indices)+"");
    }

    public static int oddCells(int n, int m, int[][] indices) {
        int result = 0;
        int[] hanglist = new int[n];
        int[] lielist = new int[m];
        for (int[] temp : indices) {
            hanglist[temp[0]]++;
            lielist[temp[1]]++;
        }
        //遍历统计奇数
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                LogUtils.print( i + " " + j + "   | " +(hanglist[i] + lielist[j]) ) ;
                if ((hanglist[i] + lielist[j]) % 2 !=0) {
                    result++;
                }
            }
        }


        return result;
    }


}
