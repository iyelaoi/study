package cn.wqz.algorithms.jvm.reference;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class WeakHashMapDemo {

    /**
     * common HashMap
     */
    public static void fun1(){
        HashMap<Integer, String> hashMap = new HashMap<>();
        Integer i = 1;
        hashMap.put(i, "ss");
        System.out.println(hashMap);

        i = null;
        System.out.println(hashMap);

        System.gc();
        System.out.println(hashMap);
    }

    /**
     * WeakHashMap
     */
    public static void fun2(){
        Map<Integer, String> hashMap = new WeakHashMap();
        // Integer i = 2; // 使用这个满足不了需求，注意区别!!!!!
        Integer i = new Integer(2);
        hashMap.put(i, "ss");
        System.out.println(hashMap);

        i = null;
        System.out.println(hashMap);

        System.gc();
        System.out.println(hashMap);
    }

    public static void main(String[] args) {
        fun1();

        fun2();
    }
}
