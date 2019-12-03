package com.itcast.controller;

import cn.hutool.core.lang.Assert;
import com.aliyuncs.exceptions.ClientException;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.itcast.constant.MessageConstant;
import com.itcast.constant.RedisMessageConstant;
import com.itcast.entity.Result;
import com.itcast.utils.JedisUtil;
import com.itcast.utils.SMSUtils;
import com.itcast.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/28 16:29
 * @description:
 */
@RestController
@RequestMapping("validateCode")
public class ValidateCodeController {
    @Autowired
    DefaultKaptcha defaultKaptcha;
    @Autowired
    private JedisPool jedisPool;


    @Autowired
    private JedisUtil jedisUtil;

    /**
     * 获取图片验证码
     *
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


    /**
     * 登陆页面发送4位数的手机验证码
     *
     * @param map
     * @return
     */
    @RequestMapping("send4Login")
    public Result send4Login(@RequestBody Map map) {
        String telephone = (String) map.get("telephone");

        //先检验图片验证码是否正确
        //获取页面输入的图片验证码
        String code = (String) map.get("imgCode");
        //获取redis内的图片文字验证码
        //获取key
        String o = (String) map.get("deviceId");
        //获取redis内的value
        String validateCode_ = jedisUtil.get(o);
        if (!code.equals(validateCode_)) {
            return  Result.error("请输入正确的图片验证码");
        }

        //随机获取验证码
        String param = ValidateCodeUtils.generateValidateCode4String(4);
        try {
            SMSUtils.sendShortMessage("SMS_178456679", telephone, param);

            //将获取的验证码存入jedis当中,方便效验
            //SETEX key seconds value将值value关联到key，并将key的生存时间设为seconds(以秒为单位)。

            Jedis jedis = jedisPool.getResource();
            jedis.setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 5 * 60*60*60*60, param);
            //释放资源
            jedis.close();
            //将验证码返回给前台
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS, param);
        } catch (ClientException e) {
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }
}
