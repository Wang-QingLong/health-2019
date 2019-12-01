package com.itcast.controller;

import cn.hutool.core.lang.Assert;
import com.aliyuncs.exceptions.ClientException;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.itcast.constant.MessageConstant;
import com.itcast.constant.RedisMessageConstant;
import com.itcast.entity.Result;
import com.itcast.utils.SMSUtils;
import com.itcast.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
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
@RestController
@RequestMapping("ValidateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;


    /**
     * 登陆页面发送4位数的手机验证码
     *
     * @param telephone
     * @return
     */
    @RequestMapping("send4Login")
    public Result send4Login(String telephone) {

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
