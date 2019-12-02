package com.itcast.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.itcast.mapper.MemberMapper;
import com.itcast.mapper.OrderMapper;
import com.itcast.mapper.ReportMapper;
import com.itcast.mapper.SetmealMapper;
import com.itcast.pojo.HotSetmeal;
import com.itcast.pojo.ReportData;
import com.itcast.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private SetmealMapper setmealMapper;

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

    /**
     * 查询套餐占比
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> findSetmealReport() {
        return reportMapper.findSetmealReport();
    }

    /**
     * 查询营运数据
     *
     * @return
     */
    @Override
    public ReportData getBusinessReportData() {
        //当前时间
        DateTime now = DateTime.now();

        //1，当前时间格式为：yyyy-MM-dd
        String reportDate = now.toString("yyyy-MM-dd");
        //2,新增会员数 : 注册日期是今天
        Integer todayNewMember = memberMapper.findTodayNewMember(reportDate);
        //3,总会员数量
        Integer totalMember = memberMapper.findTotalMember();
        //4，本周新增会员数:本周一到现在
        String beginOfWeek = DateUtil.beginOfWeek(now).toString("yyyy-MM-dd");
        Integer thisWeekNewMember = memberMapper.findThisWeekNewMember(beginOfWeek, reportDate);
        //5,本月新增会员数
        String beginOfMonth = DateUtil.beginOfMonth(now).toString("yyyy-MM-dd");
        Integer thisMonthNewMember = memberMapper.findThisMonthNewMember(beginOfMonth, reportDate);
        //6，今日预约数
        Integer todayOrderNumber = orderMapper.findTodayOrderNumber(reportDate);
        //7，今日到诊数
        Integer todayVisitsNumber = orderMapper.findTodayVisitsNumber(reportDate);
        //8，本周预约数
        String endOfWeek = DateUtil.endOfWeek(now).toString("yyyy-MM-dd");
        Integer thisWeekOrderNumber = orderMapper.findThisWeekOrderNumber(beginOfWeek, endOfWeek);
        //9，本周到诊数
        Integer thisWeekVisitsNumber = orderMapper.findThisWeekVisitsNumber(beginOfWeek, reportDate);
        //10,本月预约数
        String endOfmonth = DateUtil.endOfMonth(now).toString("yyyy-MM-dd");
        Integer thisMonthOrderNumber = orderMapper.findThisMonthOrderNumber(beginOfMonth, endOfmonth);
        //11，本月到诊数
        Integer thisMonthVisitsNumbe = orderMapper.findThisMonthVisitsNumbe(beginOfMonth);
        //12,热门套餐
         List<HotSetmeal> hotSetmeal=setmealMapper.findHotSetmeal();

         //封装
        return ReportData.builder()
                .reportDate(reportDate)
                .todayNewMember(todayNewMember)
                .totalMember(totalMember)
                .thisWeekNewMember(thisWeekNewMember)
                .thisMonthNewMember(thisMonthNewMember)
                .todayOrderNumber(todayOrderNumber)
                .todayVisitsNumber(todayVisitsNumber)
                .thisWeekOrderNumber(thisWeekOrderNumber)
                .thisWeekVisitsNumber(thisWeekVisitsNumber)
                .thisMonthOrderNumber(thisMonthOrderNumber)
                .thisMonthVisitsNumber(thisMonthVisitsNumbe)
                .hotSetmeal(hotSetmeal)
                .build();
    }
}
