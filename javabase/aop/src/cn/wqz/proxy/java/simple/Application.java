package cn.wqz.proxy.java.simple;

import cn.wqz.proxy.IUserDao;
import cn.wqz.proxy.UserDao;
import cn.wqz.proxy.java.simple.UserDaoProxy;

public class Application {
    public static void main(String[] args) {
        IUserDao iUserDao = new UserDao();
        IUserDao userDaoProxy = new UserDaoProxy(iUserDao);
        userDaoProxy.save();
    }
}
