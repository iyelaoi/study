package cn.wqz.algorithms.thinking.thread;

class A implements Runnable{

    @Override
    public void run() {
        int i = 0;
        while(Thread.interrupted()){ // 线程中断检测,当在外部进行该线程的中断后，知识标志位被设置，但线程仍继续执行
            i++;
        }
        System.out.println("thread interrupted = " + Thread.interrupted());// 中断检测会复位中断标志
    }
}

/**
 * 中断与中断检测
 */
public class InterruptTest {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new A());
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
        System.out.println("inter...");
    }
}
