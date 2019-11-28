package com.itcast.mapper;

import com.github.pagehelper.Page;
import com.itcast.entity.PageResult;
import com.itcast.entity.Result;
import com.itcast.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/18 18:51
 * @description: 检查项持久层dao接口
 */
public interface CheckItemMapper {
    /**
     * 新增检查项
     *
     * @param checkItem
     */
    void add(CheckItem checkItem);


    /**
     * 分页查询
     *
     * @param queryString
     * @return
     */
    Page<CheckItem> findPage(@Param("queryString") String queryString);

    /**
     * Id查询
     *
     * @param row
     * @return
     */
    CheckItem findById(@Param("row") int row);

    /**
     * 编辑
     *
     * @param checkItem
     */
    void edit(CheckItem checkItem);


    /**
     * 删除
     *
     * @param id_
     */
    void delete(@Param("id_") Integer id_);

    /**
     * 查询所有
     *
     * @return
     */
    List<CheckItem> findAll();
}
