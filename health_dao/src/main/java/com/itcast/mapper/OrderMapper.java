package com.itcast.mapper;

import com.itcast.pojo.Order;
import com.itcast.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    /**根据Id查询信息
     * @param orderId
     * @return
     */
    Map findById(@Param("orderId") Integer orderId);

    /**今日预约数
     * @param reportDate
     * @return
     */
    Integer findTodayOrderNumber(@Param("reportDate") String reportDate);

    /**今日到诊数
     * @param reportDate
     * @return
     */
    Integer findTodayVisitsNumber(@Param("reportDate") String reportDate);

    /**本周预约数
     * @param beginOfWeek
     * @param endOfWeek
     * @return
     */
    Integer findThisWeekOrderNumber(@Param("beginOfWeek") String beginOfWeek, @Param("endOfWeek") String endOfWeek);

    /**本周到诊数
     * @param beginOfWeek
     * @param reportDate
     * @return
     */
    Integer findThisWeekVisitsNumber(@Param("beginOfWeek") String beginOfWeek, @Param("reportDate") String reportDate);

    /**本月预约数
     * @param beginofmonth
     * @param endofmonth
     * @return
     */
    Integer findThisMonthOrderNumber(@Param("beginofmonth") String beginofmonth, @Param("endofmonth") String endofmonth);

    /**本月到诊数
     * @param beginOfMonth
     * @return
     */
    Integer findThisMonthVisitsNumbe(@Param("beginOfMonth") String beginOfMonth);
}

