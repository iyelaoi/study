package cn.wqz.jvm.memory.ooms;

public class Matespace {

    private static byte[] bytes = new byte[10*1024*1024];
    private static byte[] bytes2 = new byte[10*1024*1024];
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(20000);
        System.out.println(bytes);
        Thread.sleep(20000);
        System.out.println(bytes2);
        Thread.sleep(1000000);
    }
}
