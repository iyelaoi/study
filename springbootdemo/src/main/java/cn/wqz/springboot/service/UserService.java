package cn.wqz.springboot.service;

import cn.wqz.springboot.entity.User;
import cn.wqz.springboot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User select(int id){
        return userMapper.selectById(id);
    }
}
