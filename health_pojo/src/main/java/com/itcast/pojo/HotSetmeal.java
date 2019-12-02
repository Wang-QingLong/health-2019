package com.itcast.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/2 18:57
 * @description: 热门套餐
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HotSetmeal implements Serializable {
    private  String name;    //套餐名
    private  Integer setmeal_count;  //预约数量
    private BigDecimal proportion ;   //占比
}
