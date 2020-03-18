package cn.wqz.websplit.service.impl;

import cn.wqz.websplit.dao.UserMapper;
import cn.wqz.websplit.model.User;
import cn.wqz.websplit.model.UserResult;
import cn.wqz.websplit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserResult login(User user) {

        UserResult userResult = new UserResult();

        User user1 = userMapper.selectByUsername(user.getUsername());
        if(user1.getPassword().equals(user.getPassword())){
            userResult.setCode(1);
            userResult.setMsg("password correct");
            userResult.setObj(user1);
        }
        return userResult;
    }

    @Override
    public User insert(User user) {
        int n = userMapper.insert(user);
        if(n > 0){
            return user;
        }
        return null;
    }
}
