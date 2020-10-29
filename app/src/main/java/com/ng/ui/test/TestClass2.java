package com.ng.ui.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/*
https://leetcode-cn.com/problems/yong-liang-ge-zhan-shi-xian-dui-lie-lcof/
用两个栈实现一个队列。队列的声明如下，请实现它的两个函数 appendTail 和 deleteHead ，
分别完成在队列尾部插入整数和在队列头部删除整数的功能。(若队列中没有元素，deleteHead 操作返回 -1 )

示例 1：

输入：
["CQueue","appendTail","deleteHead","deleteHead"]
[[],[3],[],[]]
输出：[null,null,3,-1]
示例 2：

输入：
["CQueue","deleteHead","appendTail","appendTail","deleteHead","deleteHead"]
[[],[],[5],[2],[],[]]
输出：[null,-1,null,null,5,2]


有更好的答案sad

 */
public class TestClass2 {

    public static void main(String[] args) {
        CQueue obj = new CQueue();
        obj.appendTail(3);
        int param_2 = obj.deleteHead();
        int param_3 = obj.deleteHead();
        LogUtils.print(param_2 + " " + param_3); //3 -1
    }

    static class CQueue {
        //5 2 先压入head 5 2  再压入bottom 2 5  取出就是 5 2
        Stack<Integer> head;
        Stack<Integer> bottom;
        public CQueue() {
            head = new Stack<>();
            bottom = new Stack<>();
        }

        public void appendTail(int value) {
            head.push(value);
        }

        public int deleteHead() {
            int result = 0;
            if (head.empty()) {
                return -1;
            }
            //A->B
            while (!head.empty()){
                bottom.push(head.pop());
            }
            result = bottom.pop();
            //B->A
            while (!bottom.empty()){
                head.push(bottom.pop());
            }
            return result;
        }
    }

/**
 * Your CQueue object will be instantiated and called as such:
 * CQueue obj = new CQueue();
 * obj.appendTail(value);
 * int param_2 = obj.deleteHead();
 */

}
