package com.itcast.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itcast.constant.RedisMessageConstant;
import com.itcast.entity.Result;
import com.itcast.pojo.Member;
import com.itcast.utils.JedisUtil;
import com.itcast.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/29 20:02
 * @description:
 */
@RestController
@RequestMapping("Userlogin")
public class UserLoginController {

    @Autowired
    private JedisUtil jedisUtil;
    @Reference
    private MemberService memberService;


    /**
     * 登录判断
     *
     * @param map
     * @return
     */
    @RequestMapping("check")
    public Result check(HttpServletRequest request, HttpServletResponse response, @RequestBody Map map) {
        /*获取页面输入的验证码*/
        String validateCode = (String) map.get("validateCode");
        /*获取redis存入的验证码*/
        String telephone = (String) map.get("telephone");
        String param = jedisUtil.get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);

        if (param != null && param.equals(validateCode)) {
            //验证码输入正确
            //判断当前用户是否为会员
            Member member = memberService.findByTelephone(telephone);
            if (member == null) {
                //当前用户不是会员，自动完成注册
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.add(member);
            }
            //登录成功,将用户信息写入session
            request.getSession().setAttribute("member", JSON.toJSONString(member));

             //采用redis+token的方式，为了提升用户的体验
            //使用uuid生成token
            String token = UUID.randomUUID().toString();
//将用户信息存入redis
            jedisUtil.setex(token, 60 * 60 * 24 * 15, JSON.toJSONString(member));
//将token返回给前端
            return Result.success(token, "恭喜登陆成功！");
        }

        return Result.error("验证码输入有误，请重新输入");
    }
}
