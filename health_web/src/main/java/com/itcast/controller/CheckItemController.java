package com.itcast.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itcast.constant.MessageConstant;
import com.itcast.entity.PageResult;
import com.itcast.entity.QueryPageBean;
import com.itcast.entity.Result;
import com.itcast.pojo.CheckItem;
import com.itcat.service.CheckItemService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/18 18:43
 * @description: 体检检查项管理
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    /**
     * 新增
     *
     * @param checkItem
     * @return 1, form 提交数据的格式(name=jack&age=18&address=shanggao)后期用对象接收不需要加注解
     * 2,页面传的是json数据,后端使用map或者pojo时,需要加@RequestBody
     * 3,基本数据类型 & 数组 &MultipartFile 只要保持页面的参数名称和Controller方法形参一致就不用加@RequestParam
     * 4,List 不管名字一不一样,必须加@RequestParam
     */
    @RequestMapping("add")
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }

        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }


    //分页查询
    @RequestMapping("findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return checkItemService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
    }

    /**
     * Id查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "findById")
    public CheckItem findById(String id) {

        Integer id_ = Integer.parseInt(id);
        return checkItemService.findById(id_);
    }


    /**
     * 编辑
     *
     * @param checkItem
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem) {

        try {
            checkItemService.edit(checkItem);
        } catch (Exception e) {
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }

        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }


    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "delete")
    public Result delete(Integer id) {
        try {
            checkItemService.delete(id);
        } catch (Exception e) {
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }

        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }


    /**
     * 查询所有
     *
     * @return
     */
    @RequestMapping("findAll")
    public Result findAll() {
        //
        List<CheckItem> checkItems = checkItemService.findAll();
        if (checkItems != null && checkItems.size() > 0) {
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItems);
        } else {
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }


}
