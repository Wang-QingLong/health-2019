package com.itcast.pojo;

import lombok.*;

import java.io.Serializable;

import java.util.List;


/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/2 18:31
 * @description: 运营数据 这里使用了自动生成getset方法的插件lombok
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReportData implements Serializable {

    private String reportDate;      //当前日期
    private Integer todayNewMember;      //新增会员数
    private Integer totalMember;      //总会员数
    private Integer thisWeekNewMember;      //本周新增会员数
    private Integer thisMonthNewMember;      //本月新增会员数
    private Integer todayOrderNumber;       //今日预约数
    private Integer todayVisitsNumber;        //今日到诊数
    private Integer thisWeekOrderNumber;        //本周预约数
    private Integer thisWeekVisitsNumber;        //本周到诊数
    private Integer thisMonthOrderNumber;         //本月预约数
    private Integer thisMonthVisitsNumber;          //本月到诊数
    private List<HotSetmeal> hotSetmeal;          //热门套餐

}
