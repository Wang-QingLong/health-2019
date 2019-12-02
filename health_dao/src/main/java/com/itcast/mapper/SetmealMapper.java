package com.itcast.mapper;

import com.github.pagehelper.Page;
import com.itcast.pojo.CheckGroup;
import com.itcast.pojo.CheckItem;
import com.itcast.pojo.HotSetmeal;
import com.itcast.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/21 22:41
 * @description: 套餐持久层接口
 */
public interface SetmealMapper {


    /**
     * 分页查询
     *
     * @param queryString
     * @return
     */
    Page<Setmeal> findPage(@Param("queryString") String queryString);

    /**
     * 添加数据到t_setmeal表当中
     *
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     * 添加数据到中间表t_setmeal_checkgroup，建立套餐与检查组之间的关系
     *
     * @param maps
     */
    void setCheckGroupAndCheckItem(@Param("maps") List<Map> maps);

    /**
     * 查询表单套餐数据
     *
     * @param id
     * @return
     */
    Setmeal findSetmealById(@Param("id") Integer id);

    /**
     * 查询指定套餐Id对应的检查组Id
     *
     * @param id
     * @return
     */
    List<Integer> findCheckGroupIdsById(@Param("id") Integer id);

    /**
     * 根据套餐Id删除关联
     *
     * @param id
     */
    void deleteAssociation(@Param("id") Integer id);

    /**
     * 更新套餐表格数据
     *
     * @param setmeal
     */
    void Update(Setmeal setmeal);

    /**
     * 查询中间表引用关系
     *
     * @param id
     * @return
     */
    Integer findCountById(@Param("id") Integer id);

    /**
     * 逻辑删除
     *
     * @param id
     */
    void delete(@Param("id") Integer id);

    /**
     * 查询所有
     *
     * @return
     */
    List<Setmeal> findAll();


    /**
     * 根据套餐Id查询所有的检查组
     *
     * @param id
     * @return
     */
    List<CheckGroup> findCheckGroupsBySetmealId(@Param("id") Integer id);

    /**
     * 根据检查组的Ids查询检查项的所有数据
     *
     * @param ids
     * @return
     */
    List<CheckItem> findCheckItemsByCheckGroupIds(@Param("ids") List<Integer> ids);

    /**获取热门套餐
     * @return
     */
    List<HotSetmeal> findHotSetmeal();
}
