package com.itcast.service;

import com.itcast.entity.PageResult;
import com.itcast.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/21 22:28
 * @description: 套餐服务接口
 */
public interface SetmealService {


    /**
     * 分页查询
     *
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 新增
     *
     * @param setmeal
     * @param checkgroupIds
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 查询回显数据查询:套餐表单数据,所有检查组数据,被勾选的检查组数据
     *
     * @param id
     * @return
     */
    Map dialogFormVisible4Edit(Integer id);

    /**
     * 修改编辑数据并提交(更新数据)
     *
     * @param setmeal
     * @param checkgroupIds
     */
    void Update(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 查询中间表引用关系
     *
     * @param id
     * @return
     */
    Integer findCountById(Integer id);

    /**
     * 逻辑删除套餐数据
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 查询所有的套餐数据
     *
     * @return
     */
    List<Setmeal> findAll();

    /**
     * 根据套餐Id查询套餐所有数据:检查组和检查项
     *
     * @param id
     * @return
     */
    Setmeal findById(Integer id);
}

