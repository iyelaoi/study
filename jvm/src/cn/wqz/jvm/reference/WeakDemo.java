package cn.wqz.jvm.reference;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/**
 * 很不错的一个示例
 * 考察对象的内存分配，常量池，字符串对象的创建细节
 * 弱引用，异常的处理逻辑
 */
public class WeakDemo {
    public static void main(String[] args) {
        System.out.println(get());

    }

    public static String get(){
        // 使用下面 两种字符串创建的方式，得出的结果不同
        //String a = new String("a"); // 1
        String a = "a"; // 2
        WeakReference<String> b = new WeakReference<>(a);
        WeakHashMap<String, Integer> weakHashMap = new WeakHashMap<>();
        weakHashMap.put(b.get(), 1);
        a = null;
        System.gc();
        String c = "";
        try{
            c = b.get().replace("a", "b");
            return c;
        }catch (Exception e){
            c = "c";
            return c;
        }finally {
            c += "d";
            return c + "e";
        }
    }
}
