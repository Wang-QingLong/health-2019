package com.itcast.service;

import com.itcast.pojo.Menu;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/2 10:01
 * @description: 菜单接口
 */
public interface TreeMenuService {

    /**
     * 查询菜单
     *
     * @return
     */
    List<Menu> findMenu();
}
