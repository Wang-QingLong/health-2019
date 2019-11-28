package com.itcast.jobs;

import com.itcast.constant.RedisConstant;
import com.itcast.utils.QiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/23 9:21
 * @description: 定时任务：清理垃圾图片
 */

public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    //清理图片
    public void clearImg() {
        //计算redis中两个集合的差值，获取垃圾图片名称
        Set<String> set = jedisPool.getResource().sdiff(
                RedisConstant.SETMEAL_PIC_RESOURCES,
                RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String pic = iterator.next();
            //删除图片服务器中的图片文件
            QiniuUtil.delete(pic);
            //删除redis中的数据
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, pic);
        }
    }
}
