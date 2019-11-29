package com.itcast.controller;

import cn.hutool.core.lang.Assert;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.JedisPool;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/28 16:29
 * @description:
 */
@Controller
@RequestMapping("validateCode")
public class ValidateCodeController {
    @Autowired
    DefaultKaptcha defaultKaptcha;
    @Autowired
    private JedisPool jedisPool;

    /**获取图片验证码
     * @param deviceId
     * @param response
     * @throws Exception
     */
    @RequestMapping("/code/{deviceId}")
    public void createCode(@PathVariable String deviceId, HttpServletResponse response) throws Exception {
        Assert.notNull(deviceId, "机器码不能为空");
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        //生成文字验证码
        String text = defaultKaptcha.createText();
        //生成图片验证码
        BufferedImage image = defaultKaptcha.createImage(text);
        //生成的验证码写入redis
        jedisPool.getResource().setex(deviceId, 60 * 5, text);
        //获取输出流
        ServletOutputStream out = response.getOutputStream();
        //将图片写回浏览器
        ImageIO.write(image, "JPEG", out);
    }
}
