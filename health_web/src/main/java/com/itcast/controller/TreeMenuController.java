package com.itcast.controller;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.itcast.entity.Result;
import com.itcast.pojo.Menu;
import com.itcast.service.TreeMenuService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/2 9:52
 * @description:
 */
@RestController
@RequestMapping("treemenu")
public class TreeMenuController {

    @Reference
    private TreeMenuService treeMenuService;

    /**查询菜单信息
     * @return
     */
    @RequestMapping("findMenu")
    public Result findMenu() {

       List<Menu> menuList =treeMenuService.findMenu();
        if (CollUtil.isEmpty(menuList)) {
            return  Result.error("查询信息有误");
        }

        return Result.success(menuList,"查询菜单成功");
    }
}
