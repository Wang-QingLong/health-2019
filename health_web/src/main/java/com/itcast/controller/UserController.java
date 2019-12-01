package com.itcast.controller;

import com.itcast.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/1 20:13
 * @description:
 */
@RestController
@RequestMapping("user")
public class UserController {

    @RequestMapping("getUsername")
    public Result getUsername(){
        //从springsecurity上下文获取用户名
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return Result.success(user.getUsername(),"");
    }

}
