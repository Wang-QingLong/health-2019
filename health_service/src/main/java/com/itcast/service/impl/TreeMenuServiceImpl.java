package com.itcast.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itcast.mapper.TreeMenuMapper;
import com.itcast.pojo.Menu;
import com.itcast.service.TreeMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/2 10:01
 * @description: 菜单实现类
 */
@Service(interfaceClass = TreeMenuService.class)
@Transactional
public class TreeMenuServiceImpl implements TreeMenuService {
    @Autowired
    private TreeMenuMapper treeMenuMapper;

    /**
     * 菜单查询
     *
     * @return
     */
    @Override
    public List<Menu> findMenu() {
        //创建一个集合封装一级menu菜单
        ArrayList<Menu> firstMenus = treeMenuMapper.findFirstMenus();
       //查询子级菜单，封装到里面去
        for (Menu menu : firstMenus) {
            ArrayList<Menu> children=treeMenuMapper.findChildrensMenus(menu.getId());
            menu.setChildren(children);
        }

        return firstMenus;
    }
}
