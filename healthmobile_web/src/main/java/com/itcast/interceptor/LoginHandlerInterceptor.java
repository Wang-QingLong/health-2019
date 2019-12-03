package com.itcast.interceptor;

import com.alibaba.fastjson.JSON;
import com.itcast.entity.Result;
import com.itcast.pojo.Member;
import com.itcast.utils.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/3 9:20
 * @description: 配置登陆页面拦截器
 */

public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    JedisUtil jedisUtil;

    /**
     * 在发送请求之前拦截判断一下是否登陆了
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //从请求里面获取token
        String token = request.getHeader("token");
        if (null == token || "null".equals(token) || "".equals(token)) {
            //返回错误消息401
            write401Json(response);
            return false;
        }

        //根据token去redis获取用户信息
        String memberJson = jedisUtil.get(token);
        Member member = JSON.parseObject(memberJson, Member.class);
        if (null == member) {
            //返回错误消息401
            write401Json(response);
            return false;
        }
        return true;
    }
    //写回页面
    private void write401Json(HttpServletResponse response) {
        try {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.print(JSON.toJSONString(Result.error("401")));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
