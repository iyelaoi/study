package cn.wqz.algorithms.jvm.stack;

/**
 * 线程等待锁的情况
 * 使用jstack 查看
 *  哪个线程正在等待锁
 *  线程正在等待哪个锁
 *  等待的锁由哪个线程占有
 */
public class BlockDemo {
    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("thread-0 sleep 1000ms");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized ("123"){
                    System.out.println("thread-0 run...");
                }
            }
        }).start();

        // main线程占有锁后睡眠
        synchronized ("123"){
            System.out.println("main sleep...");
            Thread.sleep(50000);
        }
        System.out.println("main over");
    }
}
