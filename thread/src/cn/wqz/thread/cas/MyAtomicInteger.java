package cn.wqz.thread.cas;

import sun.misc.Unsafe;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;

public class MyAtomicInteger extends Number implements Serializable {
    private static Unsafe unsafe;
    private static long valueOffset;
    private volatile int value;
    static{
        try {
            // 获取 Unsafe 内部的私有的实例化单例对象
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            // 无视权限
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
            // 获取某属性的偏移地址
            valueOffset = unsafe.objectFieldOffset(MyAtomicInteger.class.getDeclaredField("value"));
        } catch (NoSuchFieldException e) {
            throw new Error(e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    MyAtomicInteger(){
        value = 1;
    }

    @Override
    public int intValue() {
        return 0;
    }

    @Override
    public long longValue() {
        return 0;
    }

    @Override
    public float floatValue() {
        return 0;
    }

    @Override
    public double doubleValue() {
        return 0;
    }

    MyAtomicInteger(int v){
        value = v;
    }

    public int get(){
        return value;
    }

    public void set(int u){
        value = u;
    }

    public boolean compareAndSet(int e, int u){
        return unsafe.compareAndSwapInt(this, valueOffset, e, u);
    }

    public int increament(){
        return (unsafe.getAndAddInt(this, valueOffset, 1) + 1);
    }

    public static void main(String[] args) throws InterruptedException {
        MyAtomicInteger myAtomicInteger = new MyAtomicInteger(0);
        CountDownLatch countDownLatch = new CountDownLatch(20);
        for(int i = 0; i < 20; i++){
            new Thread(()->{
                int x = 10000;
                while(x-- > 0){
                    myAtomicInteger.increament();
                }
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        System.out.println(myAtomicInteger.get());

    }
}
