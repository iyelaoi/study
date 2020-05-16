package cn.wqz.algorithms.thread.base;

class A implements Runnable{

    @Override
    public void run() {
        System.out.println("AAAAAAAAAAAAAAAAA");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("BBBBBBBBBBBBBBBBBB");
    }
}

public class JoinTest {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new A());
        thread.start();
        System.out.println("aaaaaaaaaaaa");
        thread.join(); // 加入
        System.out.println("bbbbbbbbbbbb");

    }
}
