package cn.wqz.jvm.reference;

import java.lang.ref.SoftReference;

class A{
    String msg = "";
}

/**
 * 基本的软引用使用方式
 */
public class SoftReferenceDemo {
    public static void main(String[] args) {
        A a = new A();
        a.msg = "sss";
        SoftReference<A> softReference = new SoftReference<A>(a);
        softReference.get().msg = "ddd";
        System.out.println(a.msg);
        System.out.println(softReference.get().msg);
    }
}
