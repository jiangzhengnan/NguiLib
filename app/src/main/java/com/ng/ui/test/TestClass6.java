package com.ng.ui.test;

import org.w3c.dom.Node;

import java.util.List;

/*
https://leetcode-cn.com/problems/n-ary-tree-postorder-traversal/
给定一个 N 叉树，返回其节点值的后序遍历。
例如，给定一个 3叉树 :
返回其后序遍历: [5,6,3,2,4,1].
说明: 递归法很简单，你可以使用迭代法完成此题吗?
 */
public class TestClass6 {

    public static void main(String[] args) {
    }

    class Node {
        public int val;
        public List<Node> children;

        public Node() {
        }

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, List<Node> _children) {
            val = _val;
            children = _children;
        }
    }

    ;

    public List<Integer> postorder(Node root) {

        return null;
    }
}
