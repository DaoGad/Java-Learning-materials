package com.atguigu.interview.study.thread;

import java.util.concurrent.TimeUnit;

class MyData {
    volatile int number = 0;

    public void addTo60() {
        this.number = 60;
    }

    // 请注意，number 前面是加了关键字 volatile 关键字，volatile 不保证原子性
    public void addPlusPlus() {
        number++;
    }
}

/**
 * 1、验证volatile 的可见性
 *      1.1 假如 int number = 0; number变量之前根本没有添加 volatile关键字修饰
 *      1.2 添加了 volatile 可以解决可见性问题。
 * 2、验证 volatile 不保证原子性
 *      2.1 原子性指的是什么意思？
 *          不可分割，完整性，也即某个线程正在做某个具体业务时，中间不可以被加塞或者被分割，需要整体完整
 *          要么同时成功，要么同时失败。
 *      2.2 volatile 不保证原子性
 *      2.3 why
 * @author gcq
 * @Create 2020-10-14
 */
public class VolatileDemo {
    public static void main(String[] args) { // 一切方法运行的入口

        MyData myData = new MyData();
        for (int i = 1; i <=20; i++) {
            new Thread(() -> {
                for(int j = 1; j <=1000; j++) {
                    myData.addPlusPlus();
                }
            },String.valueOf(i)).start();
        }
        // 需要等待上面二十个全部都计算完成，再用 main 线程取得最终的结果值看是多少?
        while(Thread.activeCount() > 2) {
            Thread.yield();
        }

        System.out.println(Thread.currentThread().getName() + "\t finally number value:" + myData.number);
    }

    // volatile 可以保证可见性，及时通知其他线程，主物理内存的值已经被修改
    public static void seeOkByVolatile() {
        MyData myData = new MyData();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+"\t come on");
            // 暂停一会线程
            try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
            myData.addTo60();
            System.out.println(Thread.currentThread().getName()+"\t updated number value:" + myData.number);
        },"AAA").start();

        // 第 2 个线程就是我们的 main 线程
        while(myData.number == 0) {
            // main 线程就一直在这里等待循环，直到 number 值不在等于零
        }

        System.out.println(Thread.currentThread().getName()+"\t mission is over get number value:" + myData.number);
    }
}