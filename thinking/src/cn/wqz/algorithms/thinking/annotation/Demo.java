package cn.wqz.algorithms.thinking.annotation;

import java.lang.annotation.*;
import java.lang.reflect.Method;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@interface A{
    String value() default "";
}

/**
 * 获取方法参数上的注解
 */
public class Demo {

    public void f(@A("name") String name, int a, @A("password") String password){
        System.out.println(name + a + password);
    }

    public static void main(String[] args) throws NoSuchMethodException {
        Class clz = Demo.class;
        Method method = clz.getDeclaredMethod("f", String.class, int.class, String.class);
        Annotation[][] annotations = method.getParameterAnnotations(); // 返回每个参数的所有annotation，没有的长度为0
        System.out.println("anns.length " + annotations.length);
        for (Annotation[] annotation : annotations){
            System.out.println("ann.length " + annotation.length);
            for(Annotation a : annotation){
                System.out.println("==" + ((A)a).value());
            }
        }
    }
}
