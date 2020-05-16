package cn.wqz.algorithms.thinking.thread;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        new Thread(()->{
            try {
                System.out.println("await...");
                countDownLatch.await();
                System.out.println("ok");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        for(int i = 0; i < 10; i++){
            System.out.println(i);
            countDownLatch.countDown();  // 减10次
            Thread.sleep(500);
        }
        System.out.println("main over");
    }


}
