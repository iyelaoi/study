package cn.wqz.algorithms.thread.concurrent.futures;


import java.util.concurrent.FutureTask;

/**
 * Future接口定义了一些线程管控的方法
 * FutureRunnable接口同时继承了Runnable接口与Future接口
 * 说明此接口，既是任务，又能提供任务管理
 * FutureTask实现了FutureRunnable接口，内部定义了Callable属性，当执行FutureTask的run方法时，会去调用callable的call方法
 * FutureTask也能够接收Runnable的实例，通过class RunnableAdapter<T> implements Callable<T>接收Runnable，封装为Callable
 * 当调用callable的call方法时，call方法内部去调用Runnable的run方法
 */
public class FutureDemo {

    public static void main(String[] args) throws InterruptedException {
        FutureTask<Void> futureTask = new FutureTask<>(()->{
            int i = 0;
            while(true){
                i++;
            }
        }, null);
        new Thread(futureTask).start();
        Thread.sleep(1000);
        System.out.println(futureTask.cancel(true));
        System.out.println(futureTask.isCancelled());
        System.out.println("over");
    }
}
