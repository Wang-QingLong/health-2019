package com.itcast.controller;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.itcast.constant.MessageConstant;
import com.itcast.entity.Result;
import com.itcast.pojo.OrderSetting;
import com.itcast.utils.POIUtils;
import com.itcast.service.OrderSettingService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/23 20:18
 * @description: 预约管理
 */
@RestController
@RequestMapping("ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 编辑预约管理人数
     *
     * @return
     */
    @RequestMapping("editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {

        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true, MessageConstant.EDIT_ORDER_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.EDIT_ORDER_FAIL);
        }


    }

    /**
     * 查询本月的预约人数
     *
     * @param date
     * @return
     */
    @RequestMapping("findPage")
    public Result findPage(String date) { //参数格式为：2019-03

        String dateBegin = date + "-1";//2019-3-1
        String dateEnd = date + "-31";//2019-3-31
        Map map = new HashMap();
        map.put("dateBegin", dateBegin);
        map.put("dateEnd", dateEnd);

        List<Map<String, Object>> orderSettingList = orderSettingService.findAll(map);

        if (CollUtil.isNotEmpty(orderSettingList)) {
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, orderSettingList);
        }
        return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
    }


    /**
     * 文件上传:批量插入数据
     *
     * @param excelFile
     * @return
     */
    @RequestMapping("upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) {

        try {
            //使用POIUtils工具类读取Excel文件数据
            List<String[]> list = POIUtils.readExcel(excelFile);
            //如果Excel数据不为空
            if (list != null && list.size() > 0) {
                //创建一个list集合,对预约信息类进行封装
                List<OrderSetting> orderSettingList = new ArrayList<>();
                //遍历Excel中的日期和预约人数数据
                for (String[] strings : list) {
                    //如果该数组只有一条数据,比如日期或者预约人数没写,则跳过
                    if (strings.length != 2) {
                        continue;
                    }
                    //日期
                    String string = strings[0];
                    //可预约人数
                    String string1 = strings[1];
                    //判断是否为空
                    if (isNotEmpty(string) && isNotEmpty(string1)) {
                        //封装到对象
                        OrderSetting orderSetting = new OrderSetting(new Date(string), Integer.parseInt(string1));
                        //添加到集合
                        orderSettingList.add(orderSetting);
                    }
                }
                orderSettingService.add(orderSettingList);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }


    /**
     * 自定义判断字符串是否为空
     *
     * @param o
     * @return
     */
    private boolean isNotEmpty(Object o) {
        if (null == o) {
            return false;
        }
        if (o instanceof String) {
            String o1 = (String) o;
            if (o1.equals("") || o1.equals(" ")) {
                return false;
            }
        }
        return true;
    }

}

