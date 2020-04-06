package cn.wqz.jvm.params;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestVMParams {
    static List<byte[]> list = new LinkedList<>();
    public static void main(String[] args) throws InterruptedException {
        TimeUnit.SECONDS.sleep(30);

        System.out.println(TestVMParams.class.getName() + "main running");
        for(int i =0; i < 100; i++){
            TimeUnit.SECONDS.sleep(1);
            create();
            list.add(create1());
        }
        System.out.println(TestVMParams.class.getName() + "main end");
    }

    public static void create(){
        byte[] bytes10= new byte[1024*100];
    }

    public static byte[] create1(){
        return new byte[1024*100];
    }
}
