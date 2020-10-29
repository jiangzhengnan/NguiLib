package com.ng.ui.test;

import java.util.ArrayList;
import java.util.List;

/*
https://leetcode-cn.com/problems/lucky-numbers-in-a-matrix/
给你一个 m * n 的矩阵，矩阵中的数字 各不相同 。请你按 任意 顺序返回矩阵中的所有幸运数。

幸运数是指矩阵中满足同时下列两个条件的元素：

在同一行的所有元素中最小
在同一列的所有元素中最大
 

示例 1：

输入：matrix = [[3,7,8],[9,11,13],[15,16,17]]
输出：[15]
解释：15 是唯一的幸运数，因为它是其所在行中的最小值，也是所在列中的最大值。
示例 2：

输入：matrix = [[1,10,4,2],[9,3,8,7],[15,16,17,12]]
输出：[12]
解释：12 是唯一的幸运数，因为它是其所在行中的最小值，也是所在列中的最大值。
示例 3：

输入：matrix = [[7,8],[1,2]]
输出：[7]
 

提示：

m == mat.length
n == mat[i].length
1 <= n, m <= 50
1 <= matrix[i][j] <= 10^5
矩阵中的所有元素都是不同的


执行用时过长啦

 */
public class TestClass3 {

    public static void main(String[] args) {
        luckyNumbers(new int[][]{{3, 7, 8}, {9, 11, 13}, {15, 16, 17}});
    }

    /**
     * 在同一行的所有元素中最小
     * 在同一列的所有元素中最大
     */
    public static List<Integer> luckyNumbers(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        if (matrix == null || matrix[0] == null) {
            return result;
        }
        int lieCount = matrix.length;
        int hangCount = matrix[0].length;
        for (int i = 0; i < lieCount; i++) {
            //列
            for (int j = 0; j < hangCount; j++) {
                //行
                boolean hang = true;
                boolean lie = true;

                int now = matrix[i][j];
                for (int index = 0; index < hangCount; index++) {
                    if (matrix[i][index] < now) {


                        hang = false;
                        break;
                    }
                }
                for (int index = 0; index < lieCount; index++) {


                    if (matrix[index][j] > now) {
                        lie = false;
                        break;
                    }
                }
                if (hang && lie) {
                    result.add(now);
                }

            }

        }

        return result;
    }


}
