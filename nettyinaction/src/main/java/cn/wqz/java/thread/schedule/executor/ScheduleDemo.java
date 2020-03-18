package cn.wqz.java.thread.schedule.executor;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 任务调度demo
 */
public class ScheduleDemo {

    public static void main(String[] args) {
        // 线程池缺点：
        // 大量的任务，紧凑的调度（每个耗时事件较短），适得其反
        // 导致性能瓶颈
        ScheduledExecutorService executorService =
                Executors.newScheduledThreadPool(10);

        ScheduledFuture<?> future = executorService.schedule(()->{
                    System.out.println("3 Seconds later!!!");
                },
                3, TimeUnit.SECONDS);
        executorService.shutdown();
    }

}
