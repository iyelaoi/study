package cn.wqz.java.thread.schedule.timer;

import java.util.Timer;

/**
 * 调度器
 */
public class MyTimer {
    public static void main(String[] args) throws InterruptedException {
        // 创建调度器
        Timer timer = new Timer();

        // 创建任务
        MyTimeTask myTimeTask = new MyTimeTask("no.1",3);

        // 配置任务调度方式： 延迟2s后执行，每3s执行一次
        // 从上一次结束时计时
        timer.schedule(myTimeTask, 2000, 3000);

        // 从上一次开始时计时
        // timer.scheduleAtFixedRate(myTimeTask, 2000, 3000);
        Thread.sleep(10000);
        timer.purge(); // 从此计时器的任务队列中移除所有已取消的任务
    }
}
