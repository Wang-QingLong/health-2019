package com.itcast.service;

import com.itcast.pojo.ReportData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/1 22:15
 * @description: 数据报表接口
 */
public interface ReportService {

    /**
     * 根据日期查询数据
     *
     * @param months
     * @return
     */
    List<Integer> findMemberCountByMonths(ArrayList<String> months);

    /**
     * 查询套餐占比
     *
     * @return
     */
    List<Map<String, Object>> findSetmealReport();

    /**
     * 查询运营数据
     *
     * @return
     */
    ReportData getBusinessReportData();

}
