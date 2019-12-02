package com.itcast.mapper;

import com.itcast.pojo.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/2 10:03
 * @description: 菜单
 */
public interface TreeMenuMapper {
    /**查询一级菜单
     * @return
     */
    ArrayList<Menu> findFirstMenus();

    /**查询子级菜单
     * @param id
     * @return
     */
    ArrayList<Menu> findChildrensMenus(@Param("id") Integer id);
}
