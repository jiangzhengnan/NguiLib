package com.ng.ui;

public class TestClass {

    public static void main(String[] args) {
        int[] nums = new int[]{2, 7, 11, 15};

        System.out.println( twoSum(nums, 9)[0] +" " + twoSum(nums, 9)[1]);
    }

    public static int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];
        int a = 0;
        int b = 0;
        for (int i = 0; i < nums.length; i++) {

            for (int j = 0; j < i; j++) {
                System.out.println((nums[i]  )  +" " + i);
                System.out.println((nums[j]  ) + " " + j);

                System.out.println((nums[i] + nums[j]));
                if ((nums[i] + nums[j]) == target) {
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return result;
    }

}
