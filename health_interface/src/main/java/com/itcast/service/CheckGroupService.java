package com.itcast.service;

import com.itcast.entity.PageResult;
import com.itcast.pojo.CheckGroup;
import com.itcast.pojo.CheckItem;

import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/20 20:26
 * @description: 检查组服务接口
 */
public interface CheckGroupService {


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
     * @param checkGroup
     * @param checkitemIds
     */
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * Id查询
     *
     * @param id
     * @return
     */
    CheckGroup findById(Integer id);

    /**
     * 通过检查组Id查询检查项数据
     *
     * @param id
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    /**
     * 需要查询checkGroup ,checkitmes checkItemIds
     *
     * @param id
     * @return
     */
    Map findDialogFormVisible4Edit(Integer id);

    /**
     * 更新检查组
     *
     * @param checkGroup
     * @param checkitemIds
     */
    void Update(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 查询中间表数据
     *
     * @param id
     * @return
     */
    Integer findCountById(Integer id);

    /**
     * 逻辑删除
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 查询所有的检查组数据
     *
     * @return
     */
    List<CheckGroup> findAll();
}
