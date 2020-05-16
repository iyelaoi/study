package cn.wqz.algorithms.thread.concurrent.threadpool.rejects;

import java.util.concurrent.*;


/**
 * 自定义拒绝策略
 */
public class MyRejectedExecutionHandler implements RejectedExecutionHandler {

    public MyRejectedExecutionHandler(){}

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println("自定义拒绝策略： " + executor + " 的任务 " + r);
    }

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                4,
                8,
                10,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10),
                Executors.defaultThreadFactory(),
                new MyRejectedExecutionHandler()
        );

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
