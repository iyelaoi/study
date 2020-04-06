package cn.wqz.jvm.memory.ooms;

import java.util.ArrayList;
import java.util.List;

/**
 * GC overhead limit exceeded
 * 垃圾回收效果甚微，绝大部分时间用于回收，并且回收的空间很少
 *
 * Exception in thread thread_name: java.lang.OutOfMemoryError: GC Overhead limit exceeded Cause:
 * The detail message "GC overhead limit exceeded" indicates that the garbage collector is running
 * all the time and Java program is making very slow progress. After a garbage collection, if the
 * Java process is spending more than approximately 98% of its time doing garbage collection and
 * if it is recovering less than 2% of the heap and has been doing so far the last 5
 * (compile time constant) consecutive garbage collections, then a java.lang.OutOfMemoryError
 * is thrown. This exception is typically thrown because the amount of live data barely fits into
 * the Java heap having little free space for new allocations.
 * Action: Increase the heap size. The java.lang.OutOfMemoryError exception for GC Overhead limit
 * exceeded can be turned off with the command line flag -XX:-UseGCOverheadLimit.
 */
public class GCOverheadLimitExceeded {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        int i = 0;
        while (true){
            list.add(String.valueOf(i++).intern());
        }
    }
}
