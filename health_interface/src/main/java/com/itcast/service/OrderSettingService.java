package com.itcast.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itcast.pojo.OrderSetting;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/23 20:36
 * @description: 预约管理
 */

public interface OrderSettingService {

    /**
     * 查询所有的预约信息
     *
     * @return
     */
    List<Map<String, Object>> findAll(Map map);


    /**
     * 编辑预约人数
     *
     * @param orderSetting
     */
    void editNumberByDate(OrderSetting orderSetting);

    /**将Excel表当中数据添加到数据库  日期和预约人数
     * @param orderSettingList
     */
    void add(List<OrderSetting> orderSettingList);
}
