package com.ng.ui.test;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-02-29
 */
public class TestData {
    public final static int TYPE_TITLE = 0x1;
    public final static int TYPE_ITEM = 0x2;


    public String num;

    public int type;


    public TestData(String num, int type) {
        this.num = num;
        this.type = type;
    }
}
