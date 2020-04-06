package cn.wqz.thread.cas;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 比较方法一与方法二的区别
 */
public class AtomicIntegerDemo {

    static void demo1(){
        AtomicInteger atomicInteger = new AtomicInteger(100);
        atomicInteger.compareAndSet(100, 200);
        atomicInteger.compareAndSet(200, 400);
        System.out.println(atomicInteger.get());
    }

    /**
     * 需要仔细了解此例输出
     */
    static void demo2(){
        AtomicReference<Integer> atomicReference = new AtomicReference<>(100);
        atomicReference.compareAndSet(100, 200);
        atomicReference.compareAndSet(200, 400);
        System.out.println(atomicReference.get());
    }

    public static void main(String[] args) {
        demo1();
        demo2();
    }
}
