package com.itcast.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/1 19:52
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PracticeLombok {
    private String username;
    private  int  age;
    private  String address;
}

