package com.itcast.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.itcast.entity.Result;
import com.itcast.service.ReportService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/1 21:34
 * @description:
 */
@RestController
@RequestMapping("report")
public class ReportController {
    @Reference
    private ReportService reportService;

    /**
     * 需要 横坐标months，纵坐标memberCount
     *
     * @return
     */
    @RequestMapping("getMemberReport")
    public Result getMemberReport() {
        //封装需要的数据
        HashMap<String, Object> map = new HashMap<>();




        //获取前一年的月份时间
        DateTime dateTime = DateUtil.offsetMonth(new Date(), -12);

        /*为了构造  ["2018-01","2018-02"]这样的结构，设计list结构  */
        ArrayList<String> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            //往后偏移一个月
            //转换成yyyy--MM月份
            String month = DateUtil.offsetMonth(dateTime, i).toString("yyyy-MM");
            months.add(month);
        }

        List<Integer> memberCount= reportService.findMemberCountByMonths(months);

        map.put("months", months);
        map.put("memberCount", memberCount);
        return Result.success(map, "");
    }


}
