package com.ng.ui.test;

/*
https://leetcode-cn.com/problems/fan-zhuan-lian-biao-lcof/
反转链表
定义一个函数，输入一个链表的头节点，反转该链表并输出反转后链表的头节点。

 

示例:

输入: 1->2->3->4->5->NULL
输出: 5->4->3->2->1->NULL

限制：
0 <= 节点个数 <= 5000

  */
public class TestClass3 {

    public static void main(String[] args) {
    }

    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    // 1 -> 2 3 4 5
    //  1  <-  2  3  4 5


    public ListNode reverseList(ListNode head) {//1
        ListNode last = head.next;//2
        return last;
    }

    public ListNode reverse(ListNode one,ListNode two) {//1

        return null;
    }
}
