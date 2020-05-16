package cn.wqz.algorithms.jvm.memory.ooms;

/**
 * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
 * -XX:+PrintGCDetails -Xms1M -Xmx1M
 */
public class JavaHeapSpace {
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(10000);
        String s = "sss";
        int i = 0;
        while(true){
            s += new String("ssssssssssssssssssssssssssssss" + i) + new Integer(222);
        }
    }
}
