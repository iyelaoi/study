package cn.wqz.websplit.controller;


import cn.wqz.websplit.model.User;
import cn.wqz.websplit.model.UserResult;
import cn.wqz.websplit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @PostMapping("/login")
    public UserResult login(@RequestBody User user){
        System.out.println(user);

        return userService.login(user);
    }

    @RequestMapping("/insert")
    public User insert(@RequestBody User user){
        return userService.insert(user);
    }
}
