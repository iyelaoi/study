package cn.wqz.algorithms.thinking.thread;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolExecutorDemo {
    static class MyScheduledTask implements Runnable{
        int i;
        @Override
        public void run()
        {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("okok " + i++);
        }
    }

    public static void main(String[] args) {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(10);
        scheduledThreadPoolExecutor.schedule(new MyScheduledTask(), 3000, TimeUnit.MILLISECONDS);
        /**
         * 注意两个方法：
         * 一个方法
         */
        // 从上一次开始开始
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new MyScheduledTask(),2000, 3000,TimeUnit.MILLISECONDS);

        // 从上一次结束开始
        // scheduledThreadPoolExecutor.scheduleWithFixedDelay(new MyScheduledTask(),2000, 3000,TimeUnit.MILLISECONDS);

    }
}
