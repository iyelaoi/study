package cn.wqz.thread.concurrent.locks;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockDemo1 {

    static Lock lock = new ReentrantLock();

    public static void fun(){
        lock.tryLock();

    }


}
