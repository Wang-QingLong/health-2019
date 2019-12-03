package com.itcast.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.itcast.entity.Result;
import com.itcast.pojo.HotSetmeal;
import com.itcast.pojo.ReportData;
import com.itcast.pojo.ReportMemberParams;
import com.itcast.service.ReportService;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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
    public Result getMemberReport(@RequestBody(required = false) ReportMemberParams reportMemberParams) {

        int count = 12;

        Date end;
        //如果前端输入时间日期
        if (reportMemberParams.getStart()!=null&&reportMemberParams.getEnd()!=null) {
            Date start = reportMemberParams.getStart();
             end = reportMemberParams.getEnd();
            count = (int) (int) DateUtil.betweenMonth(start, end, true)+1;
        }else{
            end = DateTime.now();
        }
        //封装需要的数据int
        HashMap<String, Object> map = new HashMap<>();
        //如果没传就获取前一年的月份时间,如果传了就获取当前时间范围
        DateTime dateTime = DateUtil.offsetMonth(end, -count);


        /*为了构造  ["2018-01","2018-02"]这样的结构，设计list结构  */
        ArrayList<String> months = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            //往后偏移一个月
            //转换成yyyy--MM月份
            String month = DateUtil.offsetMonth(dateTime, i).toString("yyyy-MM");
            months.add(month);
        }

        List<Integer> memberCount = reportService.findMemberCountByMonths(months);

        map.put("months", months);
        map.put("memberCount", memberCount);
        return Result.success(map, "");
    }


    /**
     * 获取套餐占比
     *
     * @return
     */
    @RequestMapping("getSetmealReport")
    public Result getSetmealReport() {
        List<Map<String, Object>> map = reportService.findSetmealReport();
        if (CollUtil.isEmpty(map)) {
            return Result.error("查询失败");
        }
        return Result.success(map, "查询成功");
    }

    /**
     * 获取运营数据信息
     *
     * @return
     */
    @RequestMapping("getBusinessReportData")
    public Result getBusinessReportData() {
        ReportData reportData = reportService.getBusinessReportData();
        if (reportData == null) {
            return Result.error("查询有误");
        }
        return Result.success(reportData, "查询成功");
    }


    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletResponse response, HttpServletRequest request) {
        try {
            ReportData reportData = reportService.getBusinessReportData();
            //把reportDataVo写入excel
            String path = request.getSession().getServletContext().getRealPath("template");
            //创建excel对象（加载excel）
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(path + "/report_template.xlsx");//公司一般会在服务器创建一个固定的目录放模板
            //我们的配置文件现在放项目下面，公司里面会把配置文件统一放在一个目录
            //还可以把配置文件放在统一的配置中心（Spring Cloud Config，Apollo，disconf）
            //获取sheet
            XSSFSheet sheetAt = xssfWorkbook.getSheetAt(0);
            //获取行
            sheetAt.getRow(2).getCell(5).setCellValue(reportData.getReportDate());


            sheetAt.getRow(4).getCell(5).setCellValue(reportData.getTodayNewMember());
            sheetAt.getRow(4).getCell(7).setCellValue(reportData.getTotalMember());

            sheetAt.getRow(5).getCell(5).setCellValue(reportData.getThisWeekNewMember());
            sheetAt.getRow(5).getCell(7).setCellValue(reportData.getThisMonthNewMember());

            sheetAt.getRow(7).getCell(5).setCellValue(reportData.getTodayOrderNumber());
            sheetAt.getRow(7).getCell(7).setCellValue(reportData.getTodayVisitsNumber());
            sheetAt.getRow(8).getCell(5).setCellValue(reportData.getThisWeekOrderNumber());
            sheetAt.getRow(8).getCell(7).setCellValue(reportData.getThisWeekVisitsNumber());
            sheetAt.getRow(9).getCell(5).setCellValue(reportData.getThisMonthOrderNumber());
            sheetAt.getRow(9).getCell(7).setCellValue(reportData.getThisMonthVisitsNumber());


            int i = 12;
            List<HotSetmeal> hotSetmeal = reportData.getHotSetmeal();
            for (HotSetmeal setmeal : hotSetmeal) {
                sheetAt.getRow(i).getCell(4).setCellValue(setmeal.getName());
                sheetAt.getRow(i).getCell(5).setCellValue(setmeal.getSetmeal_count());
                sheetAt.getRow(i).getCell(6).setCellValue(setmeal.getProportion().doubleValue());
                i++;
            }
            //在响应头里面告诉浏览器我们要返回的是excel
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + java.net.URLEncoder.encode("运营数据报表", "UTF-8") + ".xls");
            //然后把excel以流的形式写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            xssfWorkbook.write(outputStream);

            outputStream.flush();
            outputStream.close();
            xssfWorkbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
