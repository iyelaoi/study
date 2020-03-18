package cn.wqz.websplit.service;

import cn.wqz.websplit.model.User;
import cn.wqz.websplit.model.UserResult;

public interface UserService {
    UserResult login(User user);
    User insert(User user);
}
