package com.itcat.service;

import com.itcast.entity.Result;
import com.itcast.pojo.OrderSuccessMsg;

import java.util.Map;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/26 0:40
 * @description: 体检预约接口
 */
public interface MemberService {
    /**
     * 体检预约
     *
     * @param map
     * @return
     */
    Result addOrder(Map map);

    /**根据order_id查询预约成功的数据
     * @param id
     * @return
     */
    OrderSuccessMsg findById(Integer id);
}

