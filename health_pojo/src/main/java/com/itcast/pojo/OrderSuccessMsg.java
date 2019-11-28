package com.itcast.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/26 22:12
 * @description: 预约成功实体类
 */
public class OrderSuccessMsg implements Serializable {

    private String member;   //体检人
    private String setmeal;   //体检套餐名称
    private Date orderDate;    //体检日期
    private String orderType;   //预约类型

    public OrderSuccessMsg() {
    }

    public OrderSuccessMsg(String name, String setmeal, Date orderDate, String orderType) {
        this.member = name;
        this.setmeal = setmeal;
        this.orderDate = orderDate;
        this.orderType = orderType;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getSetmeal() {
        return setmeal;
    }

    public void setSetmeal(String setmeal) {
        this.setmeal = setmeal;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
