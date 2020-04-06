package cn.wqz.thinking.thread;

import java.util.concurrent.PriorityBlockingQueue;

public class PriorityBlockingQueueDemo {
    static class MyPriorityTask implements Comparable<MyPriorityTask>{
        private final int priority;
        MyPriorityTask(int priority){
            this.priority = priority;
        }
        @Override
        public int compareTo(MyPriorityTask o) {
            return this.priority - o.priority;
        }

        void doWork(){
            System.out.println("my priority task " + priority);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        PriorityBlockingQueue<MyPriorityTask> priorityTasks = new PriorityBlockingQueue<>();
        priorityTasks.add(new MyPriorityTask(10));
        priorityTasks.add(new MyPriorityTask(20));
        priorityTasks.add(new MyPriorityTask(15));
        System.out.println("start");
        priorityTasks.take().doWork();
        priorityTasks.take().doWork();
        priorityTasks.take().doWork();
        priorityTasks.take().doWork();
    }
}
