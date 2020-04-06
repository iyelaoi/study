package cn.wqz.jvm.stack;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 创建阻塞条件
 * waiting on condition
 */
public class WaitingOnConditionDemo {

    public static void main(String[] args) {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(1);
        blockingQueue.add("123");
        try {
            //阻塞的添加
            blockingQueue.put("123");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
