package cn.wqz.jvm.reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class WeakReferenceDemo {

    public static void main(String[] args) throws InterruptedException {
        String s = new String("sss");
        // 回收队列
        ReferenceQueue<String> referenceQueue = new ReferenceQueue();
        WeakReference<String> weakReference = new WeakReference<>(s, referenceQueue);
        System.out.println(weakReference.get());
        s = null;
        System.out.println(weakReference.get());
        System.out.println(referenceQueue.poll());

        // 注意，垃圾回收线程使用应提高警惕，后台回收线程可能和当前线程为并发执行
        // 导致回收对象还未被放入队列就已经执行了poll（）；
        System.gc();
        // 加入延迟
        Thread.sleep(1000);
        System.out.println(weakReference.get());
        System.out.println(referenceQueue.poll());
    }
}
