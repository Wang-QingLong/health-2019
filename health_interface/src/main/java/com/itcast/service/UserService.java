package com.itcast.service;


import com.itcast.pojo.User;


/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/30 21:10
 * @description: 用户接口
 */

public interface UserService {
    /**
     * 老版
     * 根据用户名查询用户的所有信息
     *
     * @param username
     * @return
     */
    User findByUsername(String username);


    /**
     * 优化版
     * Mybatis新特性一套查查询数据
     *
     * @param username
     * @return
     */
    User findByUserNameByMyBatis(String username);
}
