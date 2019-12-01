package com.itcast.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.itcast.mapper.UserMapper;
import com.itcast.pojo.Permission;
import com.itcast.pojo.Role;
import com.itcast.pojo.User;
import com.itcast.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/30 22:14
 * @description: 用户实现类
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    /**
     * 根据用户名查询用户的所有信息
     *
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        User user = userMapper.findByUserName(username);
        if (user == null) {
            return null;
        }
        //根据用户名查询所有的角色信息
        List<Role> roles = userMapper.findRolesByUserName(username);
        //如果为空
        if (CollUtil.isEmpty(roles)) {
            return null;
        }
        //不为空
        ArrayList<Integer> roleIds = new ArrayList<>();
        for (Role role : roles) {
            roleIds.add(role.getId());
        }
        //根据角色的Id查询角色的权限信息
        List<Permission> permissions = userMapper.findPermissionByRoles(roleIds);

        //如果为空
        if (CollUtil.isEmpty(permissions)) {
            return null;
        }
        //不为空

        //分组:
        //创建一个map集合,用于封装一个角色Id对应的权限表

        Map<Integer, List<Permission>> map = new HashMap<>();

        map = permissions.stream().collect(Collectors.groupingBy(Permission::getRoleId));

        for (Role role : roles) {
            role.setPermissionslist(map.get(role.getId()));
        }
        //封装
        user.setRoleslist(roles);
        //返回
        return user;
    }


    /**
     * 使用Mybatis新特性一套查询
     *
     * @param username
     * @return
     */
    @Override
    public User findByUserNameByMyBatis(String username) {
        return userMapper.findByUserNameByMyBatis(username);
    }
}
