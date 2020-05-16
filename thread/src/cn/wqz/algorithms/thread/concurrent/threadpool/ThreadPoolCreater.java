package cn.wqz.algorithms.thread.concurrent.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建线程池
 */
public class ThreadPoolCreater {

    public static void main(String[] args) {

        /**
         * 固定的线程数
         * 任务队列LinkedBlockingQueue
         */
        //ExecutorService executorService = Executors.newFixedThreadPool(5);


        /**
         * 单线程线程池
         * 任务队列LinkedBlockingQueue
         */
        //ExecutorService executorService = Executors.newSingleThreadExecutor();

        /**
         * 随着任务的增加而增加线程数
         * 任务队列SynchronousQueue
         */
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            for (int i = 0; i < 100000000; i++) {
                final int x = i;

                executorService.execute(()->{
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " -->> " + x);
                });
            }
        }catch(Exception e){

        }finally {
            executorService.shutdown();
        }

    }
}
