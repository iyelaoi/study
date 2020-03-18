package cn.wqz.proxy.java.dynamic;

import cn.wqz.proxy.IUserDao;
import cn.wqz.proxy.UserDao;

import java.lang.reflect.Method;

public class Application {
    public static void main(String[] args) throws InterruptedException {
        UserDao userDao = new UserDao();
        ProxyFactory proxyFactory = new ProxyFactory(userDao);
        IUserDao iUserDao = (IUserDao) proxyFactory.getProxyInstance();
        iUserDao.save();
        Class cls = iUserDao.getClass();
        System.out.println(cls.getName());
        Method[] methods = cls.getMethods();

        System.out.println();
    }
}
