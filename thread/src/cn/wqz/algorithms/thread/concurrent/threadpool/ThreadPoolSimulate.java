package cn.wqz.algorithms.thread.concurrent.threadpool;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 模拟线程池
 * 不提供管理功能
 */
public class ThreadPoolSimulate {
    public static void main(String[] args) {

        int coreSize = 10; // 核心线程数
        ArrayBlockingQueue<Runnable> arrayBlockingQueue = new ArrayBlockingQueue<>(100);

        for (int i = 0; i < coreSize; i++) {
            new Thread(){ // 假设创建10个非销毁线程
                @Override
                public void run() {
                    try {
                        Runnable runnable = null;
                        // 该线程一直不死，每执行完一个任务，继续从队列中去任务执行
                        while((runnable = arrayBlockingQueue.take()) != null){  // 从阻塞队列中去任务
                            runnable.run();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }


    }
}
