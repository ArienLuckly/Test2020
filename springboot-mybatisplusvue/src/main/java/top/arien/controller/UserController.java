package top.arien.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.arien.common.lang.Result;
import top.arien.dao.UserDAO;
import top.arien.entity.User;

/**
 * @author Arien-天柱
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserDAO userDAO;

    @RequiresAuthentication
    @GetMapping("/index")
    public Result index(){
        User user = userDAO.selectById(1);
        return Result.success(user);
    }

    @RequiresAuthentication
    @PostMapping("/save")
    public Result save(@Validated @RequestBody User user){
        System.out.println("注册用户！");
        userDAO.insert(user);
        return Result.success(user);
    }

}
