package com.itcast.pojo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/3 16:25
 * @description: report_member.html 时间日期参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReportMemberParams implements Serializable {
    private Date start;
    private Date end;
}
