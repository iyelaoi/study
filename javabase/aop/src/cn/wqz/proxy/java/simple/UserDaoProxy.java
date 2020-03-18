package cn.wqz.proxy.java.simple;

import cn.wqz.proxy.IUserDao;

public class UserDaoProxy implements IUserDao {

    private IUserDao userDao;

    public UserDaoProxy(IUserDao userDao){
        this.userDao = userDao;
    }

    @Override
    public void save() {
        System.out.println("proxy save");
        userDao.save();
        System.out.println("proxy save completed");
    }
}
