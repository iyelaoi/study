package cn.wqz.thread.pool;

import java.util.concurrent.*;

class ThreadExceptionHandler implements RejectedExecutionHandler{
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println("任务： " + r + "被 " + executor + " 拒绝执行");
    }
}

public class ThreadPoolDemo {

    private static LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();

    public static void main(String[] args) {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                4,
                10,
                60,
                TimeUnit.SECONDS,
                linkedBlockingQueue

        );
    }
}
