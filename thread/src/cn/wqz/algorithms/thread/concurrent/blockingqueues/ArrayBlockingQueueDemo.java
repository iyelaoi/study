package cn.wqz.algorithms.thread.concurrent.blockingqueues;

import cn.wqz.algorithms.thread.model.StringEnum;

import java.util.concurrent.ArrayBlockingQueue;

public class ArrayBlockingQueueDemo {



    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);

        // 插溢出报错
        // IllegalStateException("Queue full")
        blockingQueue.add(StringEnum.V1.getVar());
        blockingQueue.add(StringEnum.V2.getVar());
        blockingQueue.add(StringEnum.V3.getVar());

        // 非删除溢出异常
        // NoSuchElementException
        System.out.println(blockingQueue.element());
        System.out.println(blockingQueue.element());
        System.out.println(blockingQueue.element());

        // 删溢出报错
        // NoSuchElementException
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());

        // 非阻塞，溢出不报错
        System.out.println(blockingQueue.offer("v1"));
        System.out.println(blockingQueue.offer("v2"));
        System.out.println(blockingQueue.offer("v3"));
        System.out.println(blockingQueue.offer("v4"));
        System.out.println("ok?");

        // 非阻塞，不报错
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());

        boolean flag = true;
        Thread thread = new Thread(()->{
            while(!Thread.interrupted()){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                try {
                    // 阻塞获取
                    System.out.println(Thread.currentThread().getName() + "take " + blockingQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("take over");
        });
        thread.start();

        // 阻塞加入
        System.out.println("================  put  =================");
        blockingQueue.put("v1");
        blockingQueue.put("v2");
        blockingQueue.put("v3");
        blockingQueue.put("v4");
        System.out.println("ok");
        thread.interrupt();
        System.out.println("interrupt");

    }

}
