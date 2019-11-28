package com.itcast.mapper;

import com.itcast.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/23 20:37
 * @description: 预约管理
 */
public interface OrderSettingMapper {
    /**
     * 查询所有预约信息
     *
     * @return
     */
    List<OrderSetting> findAll(Map map);

    /**
     * 编辑预约人数
     *
     * @param
     */
    void editNumberByDate(@Param("orderDate") String orderDate, @Param("number") int number);

    /**
     * 查询该日期是否存在预约人数
     *
     * @param orderDate
     * @return
     */
    int findByData(@Param("orderDate") String orderDate);

    /**
     * 插入数据
     *
     * @param orderDate
     * @param number
     */
    void addOrder(@Param("orderDate") String orderDate, @Param("number") int number);



    /**
     * 检查用户所选择的预约日期是否已经提前进行了预约设置，
     * 如果没有设置则无法进行预约
     *
     * @param date
     * @return
     */
    OrderSetting findByOrderDate(Date date);

    /**更新预约数量
     * @param orderSetting
     */
    int editReservationsByOrderDate(OrderSetting orderSetting);
}
