package com.ng.ui;

public class TestClass {

    public static void main(String[] args) {
        int area3DHight = 1000;
        int area2DHeight = 788;

        float bilv = ((float) (area3DHight - area2DHeight) )/ ((float)area2DHeight);
        System.out.println(bilv);

    }


}
