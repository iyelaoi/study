package cn.wqz.algorithms.jvm.memory;

import java.util.LinkedList;

class A{
    private byte[] bytes = new byte[1024*1024];
}

class B{
    private byte[] bytes = new byte[1024*1024];
}

/**
 * 每三秒钟创建一个有引用的对象和一个没有引用的对象
 */
public class OOMMonitor1 {

    public static void main(String[] args) throws InterruptedException {
        LinkedList<A> linkedList = new LinkedList<>();
        int i = 0;
        for(;;){
            linkedList.add(new A()); // 活对象
            new B(); // 非活对象
            Thread.sleep(3000);
            System.out.println(++i);
        }
    }
}
