package com.itcast.mapper;

import com.itcast.pojo.Member;
import org.apache.ibatis.annotations.Param;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/26 0:43
 * @description:
 */
public interface MemberMapper {
    /**
     * 根据用户提供的手机号查询是否存在数据
     *
     * @param telephone
     * @return
     */



   Member findByTelephone(@Param("telephone") String telephone);


    /**添加数据到t_member
     * @param member
     */
    void add(Member member);



    /**根据名字更新就诊人
     * @param id
     * @param name
     */
    void updateName(@Param("id") Integer id, @Param("name") String name);

    /**今日新增会员数
     * @param reportDate
     * @return
     */
    Integer findTodayNewMember(@Param("reportDate") String reportDate);

    /**获取总会员数量
     * @return
     */
    Integer findTotalMember();

    /**本周新增会员量
     * @param beginOfWeek
     * @param reportDate
     * @return
     */
    Integer findThisWeekNewMember(@Param("beginOfWeek") String beginOfWeek, @Param("reportDate") String reportDate);

    /**本月新增会员数
     * @param beginOfMonth
     * @param reportDate
     * @return
     */
    Integer findThisMonthNewMember(@Param("beginOfMonth") String beginOfMonth, @Param("reportDate") String reportDate);
}
