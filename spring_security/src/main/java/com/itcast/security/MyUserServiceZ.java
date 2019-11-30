package com.itcast.security;

import com.itcast.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/30 13:38
 * @description: 如果我们要从数据库动态查询用户信息，
 * 就必须按照spring security框架的要求提供一个实现UserDetailsService
 * 接口的实现类，并按照框架的要求进行配置即可。框架会自动调用实现类中的方法并自动进行密码校验。
 */
public class MyUserServiceZ implements UserDetailsService {

    /*使用密文加密 :使用set方法注入*/
    private  BCryptPasswordEncoder passwordEncoder;

    public BCryptPasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    //模拟数据库中的用户数据 调用pojo里面的实体类，创建几个对象模仿一下效果
    public static Map<String, User> map = new HashMap<>();

    public void initData(){
        User user1 = new User();
        user1.setUsername("admin");
        user1.setPassword(passwordEncoder.encode("admin"));

        User user2 = new User();
        user2.setUsername("xiaoming");
        user2.setPassword(passwordEncoder.encode("xiaoming"));

        map.put(user1.getUsername(), user1);
        map.put(user2.getUsername(), user2);
    }

    /**
     * 根据用户名加载用户信息
     *
     * @param username 就是我们登录时输入的用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        initData();


      //根据用户名查询用户密码,id

        //这里模仿根据用户名查询数据库
        User user = map.get(username);
        if (user == null) {
            //根据用户名没有查询到数据
            return null;
        }
        //根据用户名查询到了数据

        //授权，后期需要改为查询数据库动态获取用户拥有的权限和角色
//根据用户id查询用户的权限和集合,这里简单模拟自己写

        ArrayList<GrantedAuthority> list = new ArrayList<>();
        //如果是admin,就给他del的权限
        if (username.equals("admin")) {

            list.add(new SimpleGrantedAuthority("del"));
        }
        //不管是谁都给他访问add的页面
        list.add(new SimpleGrantedAuthority("add"));

        //创建一个spring_sercurity安全框架自带的User对象
        // 去封装需要返回的数据：用户名，密码，以及权限集合
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);


        /*要让spring容器认识这个类要么bean标签要么加注解component*/
    }
}
