package com.itcast.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itcast.constant.MessageConstant;
import com.itcast.entity.Result;
import com.itcast.mapper.MemberMapper;
import com.itcast.mapper.OrderMapper;
import com.itcast.mapper.OrderSettingMapper;
import com.itcast.pojo.*;
import com.itcast.utils.DateUtils;
import com.itcat.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/26 0:40
 * @description: 体检预约实现类
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private OrderSettingMapper orderSettingMapper;
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 体检预约
     *
     * @param map
     * @return
     */
    @Override
    public Result addOrder(Map map) {
//        体检预约方法处理逻辑比较复杂，需要进行如下业务处理：
//        1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
//        2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
//        3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
//        4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
//        5、预约成功，更新当日的已预约人数


//       1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        //获取前端返回的日期数据
        String orderDate = (String) map.get("orderDate");
        Date date = null;
        try {
            //工具类转换
            date = DateUtils.parseString2Date(orderDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //根据用户提供的预约日期查询数据库是否存在数据
        OrderSetting orderSetting = orderSettingMapper.findByOrderDate(date);
        if (orderSetting == null) {
            //如果不存在则不可以预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //2,检查预约日期是否预约已满
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if (reservations >= number) {
            //预约已满，不能预约
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //3,检查当前用户是否为会员，根据手机号判断
        String telephone = (String) map.get("telephone");
        //根据手机号查询数据库是否有记录
      Member member = memberMapper.findByTelephone(telephone);

        //4,防止重复预约
        if (member != null) {
            //获取用户手机号注册的会员Id,也就是memberId
            Integer memberId = member.getId();
            //将前端返回的套餐id String类型转换成Int类型,数据类型根据数据库类型来定
            int setmealId = Integer.parseInt((String) map.get("setmealId"));
            //将预约日期,会员号,套餐号进行封装后查询匹配是否存在重复预约
            Order order = new Order(memberId, date, null, null, setmealId);
            List<Order> list = orderMapper.findByCondition(order);
            if (list != null && list.size() > 0) {
                //已经完成了预约，不能重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }

        //4,可以预约，设置预约人数加一
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        //更新预约人数
        int i = orderSettingMapper.editReservationsByOrderDate(orderSetting);
        //如果同一个套餐名额只剩下一个时
        if (i==0){
            throw new RuntimeException("更新失败");
        }

        if (member == null) {
            //当前用户不是会员，需要添加到会员表
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberMapper.add(member);
        }

        //保存预约信息到预约表
        Order order = new Order(member.getId(),
                date,
                (String) map.get("orderType"),
                Order.ORDERSTATUS_NO,
                Integer.parseInt((String) map.get("setmealId")));
        orderMapper.add(order);

        memberMapper.updateName(member.getId(),(String)map.get("name"));

        return new Result(true, MessageConstant.ORDER_SUCCESS, order);
    }

    /**
     * 根据order_id查询预约成功的数据:
     * 体检人 体检套餐 体检日期 预约类型
     *
     * @param id
     * @return
     */
    @Override
    public OrderSuccessMsg findById(Integer id) {
        //套餐数据
        Setmeal setmealId = orderMapper.findSetmealIdById(id);
        Order order = orderMapper.findOrderById(id);
        //体检日期
        Date orderDate = order.getOrderDate();
        //预约类型
        String orderType = order.getOrderType();
        //体检人
        String name = orderMapper.findNameById(id);
        //返回数据
        return new OrderSuccessMsg(name, setmealId.getName(), orderDate, orderType);
    }
}

