package com.itcast.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/1 22:19
 * @description: 报表数据层
 */
public interface ReportMapper {
    /**
     * 根据日期查询数据
     *
     * @param month
     * @return
     */
    Integer findMemberCountByMonths(@Param("month") String month);


    /**
     * 查询套餐占比
     *
     * @return
     */
    List< Map<String,Object>> findSetmealReport();
}
