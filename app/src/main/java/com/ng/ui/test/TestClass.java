package com.ng.ui.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
https://leetcode-cn.com/problems/find-common-characters/
给定仅有小写字母组成的字符串数组 A，返回列表中的每个字符串中都显示的全部字符（包括重复字符）组成的列表。
例如，如果一个字符在每个字符串中出现 3 次，但不是 4 次，则需要在最终答案中包含该字符 3 次。
你可以按任意顺序返回答案。
示例 1：
输入：["bella","label","roller"]
输出：["e","l","l"]
示例 2：
输入：["cool","lock","cook"]
输出：["c","o"]

提示：
1 <= A.length <= 100
1 <= A[i].length <= 100
A[i][j] 是小写字母
 */
public class TestClass {

    public static void main(String[] args) {
        commonChars(new String[]{"bella", "label", "roller"});
    }

    public static List<String> commonChars(String[] A) {
        int[] fir = new int[26];
        Arrays.fill(fir, Integer.MAX_VALUE);

        for (String temp : A) {
            int[] tempfir = new int[26];
            for (char charTemp : temp.toCharArray()) {
                tempfir[charTemp - 'a']++;
            }

            //比较
            for (int i = 0; i < 26; i++) {
                fir[i] = Math.min(fir[i], tempfir[i]);
            }
        }
        List<String> result = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < fir[i]; j++) {
                char temp = (char) ('a' + i);
                result.add(String.valueOf(temp));
            }
        }
        LogUtils.print(result.toString());
        return result;
    }


}
