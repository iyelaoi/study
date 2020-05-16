package cn.wqz.algorithms.thread.concurrent.futures;

import java.util.concurrent.*;

class MyCallable implements Callable<Integer>{

    @Override
    public Integer call() throws Exception {
        Thread.sleep(3000);
        return 1000;
    }
}

/**
 * {@link Future }接口定义了获取线程返回值、cancel 目标线程、访问线程状态
 * 使用此接口的实现类 {@link FutureTask}，该类接收{@link Callable}接口或者{@link Runnable}的实例
 * 能够访问并控制目标线程
 */
public class CallableDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<>(new MyCallable());
        new Thread(futureTask).start(); // 开启另一个线程
        /**
         * 在此处可以执行本线程的相关任务
         */
        Thread.sleep(10000); // 模拟本线程任务执行

        System.out.println("mycallable " + futureTask.get()); // 等本线程任务结束了再取取另一线程的结果
        System.out.println("main over");
    }


}
