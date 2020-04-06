package cn.wqz.thinking.thread;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolExecutorDemo {
    static class MyScheduledTask implements Runnable{
        int i;
        @Override
        public void run() {
            System.out.println("okok " + i++);
        }
    }

    public static void main(String[] args) {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(10);
        scheduledThreadPoolExecutor.schedule(new MyScheduledTask(), 3000, TimeUnit.MILLISECONDS);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new MyScheduledTask(),2000, 3000,TimeUnit.MILLISECONDS);

    }
}
