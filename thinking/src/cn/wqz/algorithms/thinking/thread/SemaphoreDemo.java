package cn.wqz.algorithms.thinking.thread;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo {
    public static void main(String[] args) {
        int n = 10;
        Semaphore semaphore = new Semaphore(10, true);
        for(int i = 0; i < 10; i++){
            new Thread(()->{
                try {
                    System.out.println(Thread.currentThread().getName() + "acquire...");
                    semaphore.acquire(2);
                    System.out.println(Thread.currentThread().getName() + "acquire ok");
                    Thread.sleep(500);
                    semaphore.release(2);
                    System.out.println(Thread.currentThread().getName() + " release ok");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
