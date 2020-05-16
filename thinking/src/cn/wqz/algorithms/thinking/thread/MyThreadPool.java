package cn.wqz.algorithms.thinking.thread;


import java.util.concurrent.*;


/**
 * 线程中未捕获异常的处理器
 */
class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("MyUncaughtException : " + t + "[" + e + "]");
    }
}

/**
 * 线程创建工厂
 */
class MyThreadFactory implements ThreadFactory{

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        return thread;
    }
}

/**
 * 执行器
 */
class MyThreadPoolExecutor extends ThreadPoolExecutor{

    public MyThreadPoolExecutor(){
        super(
                20,
                50,
                60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new MyThreadFactory()
        );

    }
}

/**
 * 线程池中的相关初始化类
 */
public class MyThreadPool {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyThreadPoolExecutor myThreadPoolExecutor = new MyThreadPoolExecutor();
        Future<?> future = myThreadPoolExecutor.submit(()->{
            for(int i = 0; i < 10; i++){
                System.out.println(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        if(future.get() != null){
            System.out.println("over");
        }
        myThreadPoolExecutor.shutdown();

    }

}
