package com.itcast.mapper;

import com.itcast.pojo.Permission;
import com.itcast.pojo.Role;
import com.itcast.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/30 23:05
 * @description:
 */
public interface UserMapper {
    /**
     * 根据用户名查询数据
     *
     * @param username
     * @return
     */
    User findByUserName(@Param("username") String username);

    /**
     * 根据用户名查询用户角色集合
     *
     * @param username
     * @return
     */
    List<Role> findRolesByUserName(@Param("username") String username);

    /**
     * 根据角色的Id查询权限
     *
     * @param roleIds
     * @return
     */
    List<Permission> findPermissionByRoles(@Param("roleIds") ArrayList<Integer> roleIds);

    /**
     * 使用Mybatis新特性一套查询
     *
     * @param username
     * @return
     */
    User findByUserNameByMyBatis(@Param("username") String username);
}
