package cn.wqz.algorithms.thread.concurrent.blockingqueues;

import java.util.concurrent.SynchronousQueue;

/**
 *
 */
public class SynchronicBlockingQueueDemo {

    public static void main(String[] args) {
        SynchronousQueue<String> synchronousQueue = new SynchronousQueue<>();
        new Thread(()->{
            int i = 0;
            while(i++ <100){
                try {
                    synchronousQueue.put("x" + i );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        new Thread(()->{
            while(true){
                try {
                    System.out.println(Thread.currentThread().getName() + " take " + synchronousQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        new Thread(()->{
            while(true){
                try {
                    System.out.println(Thread.currentThread().getName() + " take " + synchronousQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }
}
