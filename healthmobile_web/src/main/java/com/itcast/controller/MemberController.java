package com.itcast.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itcast.constant.MessageConstant;
import com.itcast.constant.RedisMessageConstant;
import com.itcast.entity.Result;
import com.itcast.pojo.Order;
import com.itcast.pojo.OrderSuccessMsg;
import com.itcast.utils.DateUtils;
import com.itcast.utils.JedisUtil;
import com.itcast.utils.SMSUtils;
import com.itcast.utils.ValidateCodeUtils;
import com.itcat.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;


/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/25 16:33
 * @description:
 */
@RestController
@RequestMapping("order")
public class MemberController {

    @Reference
    private MemberService memberService;


    @Autowired
    private JedisUtil jedisUtil;


    /**
     * 提交预约信息
     *
     * @param
     * @return
     */
    @RequestMapping("submit")
    public Result submit(@RequestBody Map map) {

        //排除是否是重复请求
        Long del = jedisUtil.del("token");
//        Jedis jdeis = jedisPool.getResource();
//        Long del = jdeis.del(token);  ToDo 问老师明天

        if (del == 0) {
            return Result.error("请求无效");
        }

        //先比较电话号码和验证码是否符合
        //redis当中的验证码

        String param = jedisUtil.get(map.get("telephone") + RedisMessageConstant.SENDTYPE_ORDER);

        //前台输入的验证码为interge转换成string类型
        String validateCode = (String) map.get("validateCode");
        //比较
        if (param != null && validateCode.equals(param)) {
            //相同,则存入数据库

            //定义一个对象
            Result result = null;
            try {
                //设置预约类型
                map.put("orderType", Order.ORDERTYPE_WEIXIN);

                result = memberService.addOrder(map);

            } catch (Exception e) {

                return result;
            }
            //判断返回值
            if (result.isFlag()) {
                //预约成功,发送短信通知ToDo
                try {
                    String date_ = (String) map.get("orderDate");
                    Date date = DateUtils.parseString2Date(date_);
                    SMSUtils.sendShortMessage1("SMS_178765072", (String) map.get("telephone"), date);
                    return new Result(true, MessageConstant.ORDER_SUCCESS, result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return Result.error("预约失败，没有档期");
            }


        }
        //再将数据存入数据库

        //预约设置数据失败
        return new Result(false, MessageConstant.ORDERSETTING_FAIL);
    }

    /**
     * 发送验证码并且返回验证码
     *
     * @param telephone
     * @return
     */
    @RequestMapping("sendOrder")
    public Result sendOrder(String telephone) {
        //随机获取验证码
        String param = ValidateCodeUtils.generateValidateCode4String(4);
        try {
            SMSUtils.sendShortMessage("SMS_178456679", telephone, param);

            //将获取的验证码存入jedis当中,方便效验
            //SETEX key seconds value将值value关联到key，并将key的生存时间设为seconds(以秒为单位)。

           jedisUtil.setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 5 * 60*60*60*60, param);
            //将验证码返回给前台
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS, param);
        } catch (ClientException e) {
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    /**
     * 根据预约表t_0rder的id来查询预约成功的数据信息
     *
     * @param id
     * @return
     */
    @RequestMapping("findById")
    public Result findById(Integer id) {
        OrderSuccessMsg orderSuccessMsg = memberService.findById(id);
        if (orderSuccessMsg != null) {
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, orderSuccessMsg);
        }

        return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
    }
}
