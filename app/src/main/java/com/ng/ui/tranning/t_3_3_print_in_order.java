package com.ng.ui.tranning;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 描述:https://leetcode-cn.com/problems/print-in-order/
 * 我们提供了一个类：
 * <p>
 * public class Foo {
 *   public void one() { print("one"); }
 *   public void two() { print("two"); }
 *   public void three() { print("three"); }
 * }
 * 三个不同的线程将会共用一个 Foo 实例。
 * <p>
 * 线程 A 将会调用 one() 方法
 * 线程 B 将会调用 two() 方法
 * 线程 C 将会调用 three() 方法
 * 请设计修改程序，以确保 two() 方法在 one() 方法之后被执行，three() 方法在 two() 方法之后被执行。
 * <p>
 *  
 * <p>
 * 示例 1:
 * <p>
 * 输入: [1,2,3]
 * 输出: "onetwothree"
 * 解释:
 * 有三个线程会被异步启动。
 * 输入 [1,2,3] 表示线程 A 将会调用 one() 方法，线程 B 将会调用 two() 方法，线程 C 将会调用 three() 方法。
 * 正确的输出是 "onetwothree"。
 * 示例 2:
 * <p>
 * 输入: [1,3,2]
 * 输出: "onetwothree"
 * 解释:
 * 输入 [1,3,2] 表示线程 A 将会调用 one() 方法，线程 B 将会调用 three() 方法，线程 C 将会调用 two() 方法。
 * 正确的输出是 "onetwothree"。
 *  
 * <p>
 * 注意:
 * <p>
 * 尽管输入中的数字似乎暗示了顺序，但是我们并不保证线程在操作系统中的调度顺序。
 * <p>
 * 你看到的输入格式主要是为了确保测试的全面性。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/print-in-order
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author Jzn
 * @date 2020-03-03
 */
public class t_3_3_print_in_order {

    public static void main(String[] args) {
        Foo foo = new Foo();
        int[] array = new int[]{1, 2, 3};

        try {
            testArray(array, foo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static synchronized void testArray(int[] array, Foo foo) throws InterruptedException {

        for (int i = 0; i < array.length; i++) {
            switch (array[i]) {
                case 1:
                    foo.first(() -> System.out.println("first"));
                    break;
                case 2:
                    foo.second(() -> System.out.println("second"));

                    break;
                case 3:
                    foo.third(() -> System.out.println("third"));

                    break;
            }
        }

    }

    static class Foo {


        private AtomicInteger firstJobDone = new AtomicInteger(0);
        private AtomicInteger secondJobDone = new AtomicInteger(0);

        public Foo() {}

        public void first(Runnable printFirst) throws InterruptedException {
            // printFirst.run() outputs "first".
            printFirst.run();
            // mark the first job as done, by increasing its count.
            firstJobDone.incrementAndGet();
        }

        public void second(Runnable printSecond) throws InterruptedException {
            while (firstJobDone.get() != 1) {
                // waiting for the first job to be done.
            }
            // printSecond.run() outputs "second".
            printSecond.run();
            // mark the second as done, by increasing its count.
            secondJobDone.incrementAndGet();
        }

        public void third(Runnable printThird) throws InterruptedException {
            while (secondJobDone.get() != 1) {
                // waiting for the second job to be done.
            }
            // printThird.run() outputs "third".
            printThird.run();
        }

    }
}
