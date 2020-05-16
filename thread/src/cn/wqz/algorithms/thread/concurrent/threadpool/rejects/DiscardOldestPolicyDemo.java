package cn.wqz.algorithms.thread.concurrent.threadpool.rejects;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 队列满了，新任务加不进去，将任务队列队首抛弃
 */
public class DiscardOldestPolicyDemo {
    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            4,
            8,
            10,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.DiscardOldestPolicy()
    );

    public static void main(String[] args) {
        for(int i = 0; i < 100; i++){
            final int x = i;
            threadPoolExecutor.execute(()->{
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " -->> " + x);
            });
        }
        threadPoolExecutor.shutdown();
    }
}
