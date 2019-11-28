package com.itcast.mapper;

import com.itcast.pojo.Order;
import com.itcast.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/26 1:10
 * @description: 检预约信息以及会员信息
 */
public interface OrderMapper {
    /**
     * 查询order表当中是否存在该数据
     *
     * @param order
     * @return
     */
    List<Order> findByCondition(Order order);

    /**
     * 新增预约信息
     *
     * @param order
     */
    void add(Order order);

    /**
     * 根据order表查询套餐的Id
     *
     * @param id
     * @return
     */
    Setmeal findSetmealIdById(@Param("id") Integer id);

    /**
     * 根据Id查询数据
     *
     * @param id
     * @return
     */
    Order findOrderById(@Param("id") Integer id);

    /**根据Order_Id查询t_member里面的name
     * @param id
     * @return
     */
    String findNameById(@Param("id") Integer id);
}

