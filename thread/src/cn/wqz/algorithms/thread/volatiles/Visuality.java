package cn.wqz.algorithms.thread.volatiles;

class A{
    int i = 0;
    // volatile int i = 0;
    public void setI(int i){
        this.i = i;
    }
}



/**
 * 可见性验证
 */
public class Visuality {
    public static void main(String[] args) throws InterruptedException {
        A a = new A();
        int i = 0;
        while(i++ < 10){
            System.out.println(a.i);
            Thread.sleep(1000);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while(i++ < 10){
                    a.i++;
                    System.out.println("++" + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }
}
