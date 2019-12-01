package com.itcast.service;

import java.util.ArrayList;
import java.util.List;

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
}
