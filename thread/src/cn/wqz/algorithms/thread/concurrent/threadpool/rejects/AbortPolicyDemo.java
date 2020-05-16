package cn.wqz.algorithms.thread.concurrent.threadpool.rejects;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 队列满了，新任务加不进去，报错java.util.concurrent.RejectedExecutionException
 * 异常在添加任务的线程中抛出，还没有进入线程池的线程中执行，所以会导致调用处的线程终止
 */
public class AbortPolicyDemo {

    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            4,
            8,
            10,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
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
