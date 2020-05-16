package cn.wqz.algorithms.jvm.memory.ooms;

import sun.misc.VM;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

/**
 * Exception in thread "main" java.lang.OutOfMemoryError: Direct buffer memory
 */
public class DirectBufferMemory {
    public static void main(String[] args) {
        System.out.println("最大直接内存：" + VM.maxDirectMemory()/1024/1024 + " M");
        int i = 0;
        List<ByteBuffer> list = new LinkedList<>();
        while(true){
            list.add(ByteBuffer.allocateDirect(1024 * 1024));
            System.out.println(i++);
        }
    }
}
