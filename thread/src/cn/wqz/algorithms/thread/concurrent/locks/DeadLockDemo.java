package cn.wqz.algorithms.thread.concurrent.locks;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 模拟死锁程序
 *
 * 检测，
 * 1. 找到进程编号
 *    linux： ps -ef | grep ***
 *    jps
 * 2. 找到死锁
 *    jstack命令
 *    jconsole
 *    jvisualvm
 */
public class DeadLockDemo {

    static ReentrantLock lock = new ReentrantLock();
    static ReentrantLock lock2 = new ReentrantLock();

    public static void main(String[] args) {

        lock.lock();
        System.out.println("main have get lock");
        try {
            new Thread(()->{
                lock2.lock();
                try{
                    System.out.println("thread have get lock2");
                    Thread.sleep(1000);
                    lock.lock();
                    try{
                        System.out.println("thread have get lock");
                    }finally {
                        lock.unlock();
                        System.out.println("thread have unlock lock");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock2.unlock();
                    System.out.println("thread lock2 have unlock");
                }
            }).start();
            Thread.sleep(2000);
            lock2.lock();
            try{
                System.out.println("main have get lock2");
            }finally {
                lock2.unlock();
                System.out.println("main have unlock lock2");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            System.out.println("main have unlock lock");
        }

    }

}
