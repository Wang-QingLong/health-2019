package com.itcast.controller;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.itcast.constant.DeleteException;
import com.itcast.constant.MessageConstant;
import com.itcast.constant.RedisConstant;
import com.itcast.entity.PageResult;
import com.itcast.entity.QueryPageBean;
import com.itcast.entity.Result;
import com.itcast.pojo.Setmeal;
import com.itcast.utils.QiniuUtil;
import com.itcat.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/21 22:24
 * @description: 套餐管理
 */
@RestController
@RequestMapping("setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {

        return setmealService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
    }
    /**
     * 文件上传:使用七牛云上传
     *
     * @param imgFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        try {
            //获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            //获取.img中.的索引
            int lastIndexOf = originalFilename.lastIndexOf(".");
            //切割获取文件后缀
            String suffix = originalFilename.substring(lastIndexOf);
            //使用UUID随机产生文件名称，防止同名文件覆盖
            String fileName = UUID.randomUUID().toString() + suffix;
            QiniuUtil.upload(imgFile.getBytes(),fileName);
            //图片上传成功,将重组的文件名存入Result对象
            Result result = new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
//            将上传图片名称存入Redis，基于Redis的Set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            //图片上传失败
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 新增
     *
     * @return
     */
    @RequestMapping("add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            setmealService.add(setmeal, checkgroupIds);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {

            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);

        }
    }

    /**编辑-数据回显
     * @param id
     * @return
     */
    @RequestMapping("dialogFormVisible4Edit")
    public Result dialogFormVisible4Edit(Integer id){

    //需要查询3条数据:套餐表单数据,所有检查组数据,被勾选的检查组数据
         Map map= setmealService.dialogFormVisible4Edit(id);
        if (CollUtil.isNotEmpty(map)) {
            return  new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,map);
        }
return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
    }

    /**提交编辑数据
     * @return
     */
    @RequestMapping("edit")
    public Result edit(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){

        try {
            setmealService.Update(setmeal,checkgroupIds);
            return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            return  new Result(false,MessageConstant.EDIT_SETMEAL_FAIL);
        }
    }


    @RequestMapping("delete")
    public Result delete(Integer id){

        //根据id查询是否存在引用关系
        Integer count = setmealService.findCountById(id);
        if (count >= 1 && count != null) {
            //如果存在引用关系,就提示要存在引用关系,不可直接删除
            throw new DeleteException("套餐与检查组存在关联,不可直接删除");
        } else {
            //不存在就直接删除(逻辑删除)
            try {
                setmealService.delete(id);
                return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
            } catch (Exception e) {
                return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
            }
        }
    }

}
