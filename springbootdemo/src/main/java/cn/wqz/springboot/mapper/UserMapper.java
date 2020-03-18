package cn.wqz.springboot.mapper;

import cn.wqz.springboot.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    User selectById(int id);
}
