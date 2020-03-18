package cn.wqz.cglib;

import java.lang.reflect.Method;
import java.util.Arrays;

public class Application {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        UserDao userDaoProxy = (UserDao) new ProxyFactory(userDao).getProxyInstance();
        userDaoProxy.save();
        Class cls = userDaoProxy.getClass();
        System.out.println(cls.getName());
        Method[] methods = cls.getMethods();
        System.out.println("=============================");
        for (Method method : methods) {
            System.out.println(method.getName());
        }
        System.out.println("=============================");
        System.out.println(cls.getSuperclass().getName());
        System.out.println(Arrays.toString(cls.getInterfaces()));
    }
}
