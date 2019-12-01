package com.itcast.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.collect.Lists;
import com.itcast.mapper.OrderSettingMapper;
import com.itcast.pojo.OrderSetting;
import com.itcast.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/23 20:43
 * @description:
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingMapper orderSettingMapper;

    /**
     * 查询所有预约信息
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> findAll(Map map_) {
        //前台需要的数据
        // this.leftobj = [
        //     {"date":22,"mouth":8,"number":300,"reservations":300,},

        List<OrderSetting> orderSettings = orderSettingMapper.findAll(map_);

        if (CollUtil.isNotEmpty(orderSettings)) {
            List<Map<String, Object>> maps = new ArrayList<>();
            for (OrderSetting orderSetting : orderSettings) {
                //创建一个map集合封装需要的数据
                Map<String, Object> map = new HashMap<>();
                Date orderDate = orderSetting.getOrderDate();
                int date = orderDate.getDate();
                int mouth = orderDate.getMonth() + 1;
                int number = orderSetting.getNumber();
                int reservations = orderSetting.getReservations();
                map.put("date", date);
                map.put("mouth", mouth);
                map.put("number", number);
                map.put("reservations", reservations);
                maps.add(map);
            }
            return maps;
        }
        return null;
    }


    /**
     * 编辑预约人数
     *
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //预约设置日期
        Date orderDate = orderSetting.getOrderDate();
        //可预约人数
        int number = orderSetting.getNumber();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //转换成yyyy--MM--dd格式
        String format = sdf.format(orderDate);

        //判断该日期是否已经设置了预约人数
        int count = orderSettingMapper.findByData(format);
        if (count > 0) {
            //存在数据，更新即可
            orderSettingMapper.editNumberByDate(format, number);
        } else {
            //不存在,则插入数据
            orderSettingMapper.addOrder(format, number);
        }
    }

    /**
     * 将Excel表当中数据添加到数据库  日期和预约人数
     *
     * @param orderSettingList
     */
    @Override
    public void add(List<OrderSetting> orderSettingList) {

        long start = System.currentTimeMillis();

        //如果该集合不为空
        if (CollUtil.isNotEmpty(orderSettingList)) {
            //使用多线程导入
            //1,把数据拆分,400一组
            List<List<OrderSetting>> partition = Lists.partition(orderSettingList, 400);
            //2,搞一个线程池:Runtime.getRuntime().availableProcessors()根据cpu密集型
            ExecutorService executorService =
                    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
            //遍历
            for (List<OrderSetting> orderSettings : partition) {
                //提交线程任务
                //Runnable  没返回值
                // Callable 有返回值
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        //遍历集合
                        for (OrderSetting orderSetting : orderSettings) {
                            //调用方法
                            editNumberByDate(orderSetting);
                        }
                    }
                });
            }

        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }


    //如果需要有返回值
  //如果需要有返回值

//    public void addd(List<OrderSetting> orderSettingList) {
//        long start = System.currentTimeMillis();
//
////        for (OrderSetting orderSetting : orderSettings) {
////            setNumberByDate(orderSetting);
////        }
//
//        //固定创建5个线程
//        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
//
//        //多线程
////        orderSettings集合分成多份，
//
//        //把集合拆分每份200数据
//        List<List<OrderSetting>> partition = Lists.partition(orderSettingList, 400);
//
//        List<Future<Integer>> futures = new ArrayList<>();
//        //我们给每个线程飞一份数据去执行
////        Thread
////        Runnable  没返回值
////        Callable 有返回值
//        for (List<OrderSetting> settings : partition) {
//            Future<Integer> future = executorService.submit(new Callable<Integer>() {
//                @Override
//                public Integer call() throws Exception {
//                    for (OrderSetting setting : settings) {
//                        editNumberByDate(setting);
//                    }
//                    return 1;
//                }
//            });
//            //把票拿在手里
//            futures.add(future);
//        }
//
//        int count = 0;
//
//        for (Future<Integer> future : futures) {
//            try {
//                Integer o = future.get();//如果线程没有干完会阻塞
//                count = count + o;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//        if (count == partition.size()) {
//            //告诉客户端已大帮人帮你把数据批量导入到数据库
//            System.out.println(partition.size());
//        }
//
//
//        long end = System.currentTimeMillis();
//
//        System.out.println("做完用了时间：" + (end - start));
//
//    }


    public static void main(String[] args) {
        System.out.println("本机电脑cpu核数:" + Runtime.getRuntime().availableProcessors());
    }

}
