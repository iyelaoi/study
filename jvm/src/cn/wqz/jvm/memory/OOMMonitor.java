package cn.wqz.jvm.memory;



/**
 * start the main function and check jvm memory state by some tools in jdk
 *
 * 默认 GC
 * jinfo -flags
 *
 */
public class OOMMonitor {

    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        for (;;){
            byte[] bs = new byte[1024 * 1024];
            System.out.println("" + ++i + " M");
            Thread.sleep(5000);
        }
    }
}
