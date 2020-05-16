package cn.wqz.algorithms.jvm.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.sql.SQLOutput;

public class PhantomReferenceDemo {
    public static void main(String[] args) throws InterruptedException {
        ReferenceQueue<String> referenceQueue = new ReferenceQueue<>();
        String s = new String("wqz");
        // 必须使用队列
        PhantomReference<String> phantomReference = new PhantomReference<>(s, referenceQueue);
        System.out.println(phantomReference.get());

        s = null;

        System.out.println(referenceQueue.poll());
        System.gc();

        Thread.sleep(1000);
        System.out.println(referenceQueue.poll());

    }
}
