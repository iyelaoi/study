package cn.wqz.algorithms.thinking.thread;


import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayQueueDemo {

    /**
     * 任务类需要实现Delayed接口
     * 实现接口中的compareTo方法，用于比较哪个元素在前在后
     * 并实现getDelay方法，返回还差多久
     */
    static class MyDelayTask implements Delayed{

        private final int delta;
        private final long trigger;

        MyDelayTask(int delayInMilliseconds){
            delta = delayInMilliseconds;
            trigger = System.nanoTime() + TimeUnit.NANOSECONDS.convert(delta, TimeUnit.MILLISECONDS);
        }


        @Override
        public int compareTo(Delayed o) {
            MyDelayTask that = (MyDelayTask)o;
            if(trigger < that.trigger) return -1;
            else if(trigger > that.trigger) return 1;
            return 0;
        }

        public void doWork() {
            System.out.println("My delay task " + Thread.currentThread().getName() + " " + delta);
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(trigger - System.nanoTime(), TimeUnit.NANOSECONDS);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        DelayQueue<MyDelayTask> delayeds = new DelayQueue<>();
        delayeds.add(new MyDelayTask(1000));
        delayeds.add(new MyDelayTask(2000));
        System.out.println("start...");
        delayeds.take().doWork();
        delayeds.take().doWork();
    }
}
