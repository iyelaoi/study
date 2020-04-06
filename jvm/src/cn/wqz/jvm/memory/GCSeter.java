package cn.wqz.jvm.memory;


import java.util.LinkedList;
import java.util.List;

/**
 * 用于配置垃圾回收器
 *
 * -XX:+UseSerialGC -XX:+PrintGCDetials
 * [DefNew: 38573K->4096K(39296K), 0.0143125 secs][Tenured: 97901K->97901K(98732K), 0.0054548 secs]
 * 单线程GC，当使用此参数，老年代默认使用SerialOldGC
 *
 *
 * -XX:+UseParNewGC -XX:+PrintGCDetails
 * [ParNew: 272514K->29903K(273280K), 0.0521447 secs][Tenured: 700206K->700233K(700712K), 0.1496787 secs]
 * Java HotSpot(TM) 64-Bit Server VM warning: Using the ParNew young collector with
 * the Serial old collector is deprecated and will likely be removed in a future release
 * 多线程收集器，当使用此回收器时，老年代默认使用SerialOldGC
 * 默认回收线程数和系统核心数一样
 * 可以使用 -XX:ParallelGCThreads 参数限制回收线程数
 *
 *
 * -XX:+UseParallelGC -XX:+PrintGCDetails
 * [PSYoungGen: 647157K->629784K(652288K)] [ParOldGen: 1381352K->1381352K(1381888K)]
 *
 * 吞吐量优先（用于执行用户任务的时间片较多——用户任务可以使用相对更多的CPU资源）的垃圾回收器
 * -XX:MaxGCPauseMillis, 内存回收时间尽量不超过此值的毫秒数
 * -XX:GCTimeRatio, 0 ~ 100 的整数，如 =19， 最大垃圾回收时间占总时间的1/（1+19）
 * -XX:+AdaptiveSizePolicy, 自适应策略
 *
 * -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails
 * 1. CMS-initial-mark
 * 2. CMS-concurrent-mark
 * 3. CMS Final Remark
 * 4. CMS-concurrent-sweep
 * 并发标记清除垃圾回收器：支持和用户线程一起执行，并发低停顿收集器
 *   首先，处理器资源敏感，（处理器核数+3）/4，四核以上不超过25%，低于四核，注意资源紧张
 *   其次，浮动垃圾（Floating Garbage），并发的用户线程创建的新的对象在该次无法回收，导致内存不够用
 *         解决方案： -XX:CMSInitiatingOccupancyFraction 设置空间占用比率为多少时提前进行回收
 *                    当在预留空间还不够的情况下，临时启用SerialOldGC，重新进行老年代回收
 *   最后，空间碎片
 *         解决方案：-XX：+UseCMS-CompactAtFullCollection开关参数（默认是开启的，此参数从 JDK 9开始废弃），
 *                   用于在CMS收集器不得不进行Full GC时开启内存碎片的合并整理过程。
 *                   -XX：CMSFullGCsBefore- Compaction（此参数从JDK 9开始废弃），在执行过若干次不整理空间的Full GC之后，
 *                   下一次进入Full GC前会先进行碎片整理（默认值为0，表示每次进入Full GC时都进行碎片整理）。
 */
public class GCSeter {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("等待被观察");
        List<byte[]> list = new LinkedList<>();
        while(true){
            byte[] bytes = new byte[1024*1024];
            list.add(new byte[1024*1024]);
        }
    }
}
