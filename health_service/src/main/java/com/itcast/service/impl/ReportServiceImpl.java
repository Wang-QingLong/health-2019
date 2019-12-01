package com.itcast.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itcast.mapper.ReportMapper;
import com.itcast.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/1 22:16
 * @description: 数据报表接口实现类
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;

    /**
     * 根据日期查询数据
     *
     * @param months
     * @return
     */
    @Override
    public List<Integer> findMemberCountByMonths(ArrayList<String> months) {
        //封装返回的数据
        ArrayList<Integer> counts = new ArrayList<>();
        for (String month : months) {
            month = month + "-31";
            Integer i = reportMapper.findMemberCountByMonths(month);
            counts.add(i);
        }


        return counts;
    }
}
