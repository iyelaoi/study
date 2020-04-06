package cn.wqz.jvm.stack;

/**
 * 线程wait情况
 */
public class ObjectWaitDemo {
    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread-0 sleep...");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized ("123"){
                    System.out.println("thread-0 run");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("thread-0 notify");
                    "123".notify();
                }
            }
        }).start();

        synchronized ("123"){
            System.out.println("main before wait");
            "123".wait(); // 不唤则不醒
            System.out.println("main after wait");
        }
    }
}
