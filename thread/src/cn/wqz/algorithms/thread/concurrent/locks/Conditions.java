package cn.wqz.algorithms.thread.concurrent.locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Resouce{
    private Lock lock = new ReentrantLock();
    private int flag = 0;
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();

    public void print(int n, Condition condition, Condition condition2){
        lock.lock();
        try{
            // 得到锁的线程，必须得有直接执行的条件
            // 得到锁的线程，必须有唤醒其他线程的逻辑
            if(flag != 0){
                // 在此等待之前已经进行了该信号的唤醒
                condition.await();
            }else{
                if(condition != c1){
                    condition.await();
                }
                flag++;
            }

            for (int i = 0; i < n; i++) {
                System.out.println(Thread.currentThread().getName() + " : " + i);
                Thread.sleep(1000);
            }

            condition2.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public Condition getC1() {
        return c1;
    }

    public void setC1(Condition c1) {
        this.c1 = c1;
    }

    public Condition getC2() {
        return c2;
    }

    public void setC2(Condition c2) {
        this.c2 = c2;
    }

    public Condition getC3() {
        return c3;
    }

    public void setC3(Condition c3) {
        this.c3 = c3;
    }
}


/**
 * 典型的信号丢失问题
 */
public class Conditions {
    static Resouce resouce = new Resouce();
    static Condition condition = resouce.getC1();
    static Condition condition2 = resouce.getC2();
    static Condition condition3 = resouce.getC3();

    public static void main(String[] args) throws InterruptedException {
        new Thread(){
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    resouce.print(5, condition,condition2);
                }
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    resouce.print(10, condition2, condition3);
                }
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    resouce.print(15,condition3,condition);
                }
            }
        }.start();
}
}
