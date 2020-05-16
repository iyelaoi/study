package cn.wqz.algorithms.jvm.memory.ooms;

/**
 * 此程序运行要小心
 * 搞不好，系统死机
 */
public class UnableToCreateNewNativeThread {
    public static void main(String[] args) {
        int i = 0;
        for (int j = 0; ; j++) {
            new Thread(){
                @Override
                public void run() {
                    try {
                        Thread.sleep(Integer.MAX_VALUE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            System.out.println(j);
        }
    }
}
