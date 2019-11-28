package com.itcast.mapper;

import com.github.pagehelper.Page;
import com.itcast.pojo.CheckGroup;
import com.itcast.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/20 20:34
 * @description: 检查组持久层接口
 */
public interface CheckGroupMapper {

    /**
     * 分页查询
     *
     * @param queryString
     * @return
     */
    Page<CheckGroup> findPage(@Param("queryString") String queryString);

    /**
     * 新增检查组数据
     *
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    /**
     * 关联检查项和检查组的
     *
     * @param maps
     */
    void setCheckGroupAndCheckItem(@Param("maps") List<Map> maps);

    /**
     * Id查询
     *
     * @param id
     * @return
     */
    CheckGroup findById(@Param("id") Integer id);

    /**
     * 通过检查组Id查询检查项数据ID
     *
     * @param id
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(@Param("id") Integer id);

    /**
     * 删除检查组与检查项之间的关联关系
     *
     * @param id
     */
    void deleteAssociation(@Param("id") Integer id);

    /**
     * 更新检查组数据
     *
     * @param checkGroup
     */
    void Update(CheckGroup checkGroup);

    /**
     * 查询中间表
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
     * 查询所有的检查组
     *
     * @return
     */
    List<CheckGroup> findAll();

}
