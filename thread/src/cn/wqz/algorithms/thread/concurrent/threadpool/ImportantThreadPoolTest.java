package cn.wqz.algorithms.thread.concurrent.threadpool;

import java.util.concurrent.*;

/**
 * 已经理解了基本的线程池原理
 *
 * 基本原理：线程池中的线程就在那，没有销毁（当然也有销毁的情况），
 * 线程池中的每个线程的run方法都在读取任务队列中的任务来执行
 *
 *
 * 目标：测试使用线程池时的异常情况
 * 异常情况分为两个方面：
 *   一：主线程异常，即线程池本身异常：添加任务错误，线程调度错误，会出现怎样的情况
 *       （添加空的任务，线程的多次start, 队列异常，等）
 *   二：线程池中的线程异常：会不会影响后续任务的执行，会不会使池子的属性发生变化（如活线程-1）
 *       线程池会不会进行新的现成的创建，使其恢复到未产生异常时的线程调度情况（核心线程数。。。）
 */
public class ImportantThreadPoolTest {

    static class MyTask implements Runnable{
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " my task start");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //throw  new RuntimeException("ex");
        }
    }

    static ExecutorService executorService = new ThreadPoolExecutor(5, 10,2, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));


    public static void main(String[] args) throws InterruptedException {

        MyTask myTask = new MyTask();
        int i = 100;
        while(i-->0){
            executorService.execute(myTask);
            executorService.execute(myTask);
            executorService.execute(myTask);
            executorService.execute(myTask);
            executorService.execute(myTask);
            executorService.execute(myTask);
            executorService.execute(myTask);
            executorService.execute(myTask);
            executorService.execute(myTask);
            executorService.execute(myTask);
            executorService.execute(myTask);
            executorService.execute(myTask);
        }
        Thread.sleep(3000);

        i = 100;
        System.out.println("===================================");
        while(i-->0){
            executorService.execute(myTask);
            executorService.execute(myTask);
            executorService.execute(myTask);
            executorService.execute(myTask);
            executorService.execute(myTask);
            executorService.execute(myTask);
            executorService.execute(myTask);
            executorService.execute(myTask);
            executorService.execute(myTask);
            executorService.execute(myTask);
            executorService.execute(myTask);
            executorService.execute(myTask);
        }


    }

}
