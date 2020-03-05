package com.ng.ui.tranning;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ProjectName: NGUI
 * @Package: com.ng.ui.tranning
 * @Description:
 *
 * 看不懂
 * @Author: Pumpkin
 * @CreateDate: 2020/3/5
 */
public class t_3_5_smallerNumbersThanCurrent {

    public static void main(String[] args) {
        smallerNumbersThanCurrent(new int[]{8, 1, 2, 2, 3});
    }

    public static int[] smallerNumbersThanCurrent(int[] nums) {
        // 8, 1, 2, 2, 3
        int len = nums.length;
        Map<Integer, Set<Integer>> valueIndex = new HashMap<>(len); // 预存每个值与索引对应
        for (int i = 0; i < len; i++) {
            if (!valueIndex.containsKey(nums[i])) valueIndex.put(nums[i], new HashSet<>());
            valueIndex.get(nums[i]).add(i);
        }

        System.out.println(valueIndex
        .toString());
        //{8=[0], 1=[1], 2=[2, 3], 3=[4]}

        int[] sortedArr = Arrays.copyOf(nums, len),
                res = new int[len];
        Arrays.sort(sortedArr); // 1, 2, 2, 3, 8
        for (int si = len - 1; si >= 0; si--) {//43210
            System.out.println(valueIndex.get(sortedArr[si])   +" " + si);
            for (int i : valueIndex.get(sortedArr[si])) {
                res[i] = si;
            }
            ; // 同值的所有索引都更新
        }
        printArray(res);
        return res;
    }

    private static void printArray(int[] res) {
        String result = "";
        for (int i : res) {
            result += i +" ";
        }
        System.out.println(result);
    }
}
