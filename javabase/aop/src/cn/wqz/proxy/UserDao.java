package cn.wqz.proxy;

public class UserDao implements IUserDao {
    @Override
    public void save() {
        System.out.println("UserDao save");
    }
}
