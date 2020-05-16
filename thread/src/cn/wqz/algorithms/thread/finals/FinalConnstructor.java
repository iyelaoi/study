package cn.wqz.algorithms.thread.finals;


class A{
    private static A a;
    int i = 0;
    private A(){
        System.out.println(Thread.currentThread().getName() + " enter");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        i = 10;
    }

    public static A getInstance(){
        if(a == null){
            a = new A();
        }
        return a;
    }

    public int getI() {
        return i;
    }
}


/**
 * 安全发布对象
 */
public class FinalConnstructor {

    static int x = 0;
    public static void main(String[] args) {

        for (int i = 0; i < 10; i++){
            new Thread(()->{
                try {
                    Thread.sleep(x * 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " i = " + A.getInstance().getI());
            }).start();
        }
    }
}
