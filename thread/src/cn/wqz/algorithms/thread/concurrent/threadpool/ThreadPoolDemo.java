package cn.wqz.algorithms.thread.concurrent.threadpool;

import java.util.concurrent.*;


/**
 * 被拒绝
 */
class MyThreadExceptionHandler implements RejectedExecutionHandler{
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println("任务： " + r + "被 " + executor + " 拒绝执行");
    }
}


public class ThreadPoolDemo {

    private static LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();

    public static void main(String[] args) {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                4, // 核心线程数
                10, // 最大线程数，当任务队列满了，将线程扩充
                60, // 线程空闲时间
                TimeUnit.SECONDS,
                linkedBlockingQueue, // 等待执行的任务队列
                new MyThreadExceptionHandler() // 拒绝策略:队列满了，线程在最大线程数，进行拒绝策略
        );
    }
}
