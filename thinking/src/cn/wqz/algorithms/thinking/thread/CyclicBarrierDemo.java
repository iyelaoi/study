package cn.wqz.algorithms.thinking.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10, ()->{
            System.out.println("open");
        });
        for(int i = 0; i < 10; i++){
            new Thread(()->{
                int j = 0;
                while(j < 10){
                    System.out.println(Thread.currentThread().getName() + " await... " + j++);
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
        System.out.println("main over");
    }
}
