package com.itcast.controller;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.itcast.constant.DeleteException;
import com.itcast.constant.MessageConstant;
import com.itcast.entity.PageResult;
import com.itcast.entity.QueryPageBean;
import com.itcast.entity.Result;
import com.itcast.pojo.CheckGroup;
import com.itcast.pojo.CheckItem;
import com.itcat.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/20 20:21
 * @description: 检查组管理
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;


    @RequestMapping("findAll")
    public Result findAll() {
        List<CheckGroup> groupList = checkGroupService.findAll();
        //CollUtil工具类,判断集合是否为空
        if (CollUtil.isNotEmpty(groupList)) {
            //不为空
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, groupList);
        } else {
            //为空
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }

    }


    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {

        return checkGroupService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
    }

    /**
     * 新增
     *
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @RequestMapping("add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        /*返回的处理数据*/
        try {
            //新增成功
            checkGroupService.add(checkGroup, checkitemIds);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            //新增失败
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    /**
     * Id查询
     *
     * @param id
     * @return
     */
    @RequestMapping("findById")
    public Result findById(Integer id) {
        CheckGroup checkGroup = checkGroupService.findById(id);
        if (checkGroup != null) {
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
        }
        return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    /**
     * 通过检查组的Id查询检查项数据
     *
     * @param id
     * @return
     */
    @RequestMapping("findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id) {
        List<Integer> checkItems = checkGroupService.findCheckItemIdsByCheckGroupId(id);
        if (checkItems != null && checkItems.size() > 0) {
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItems);
        }
        return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
    }


    /**
     * 检查组,检查项所有数据,检查组回显数据
     * Map集合封装数据:三合一
     *
     * @param id
     * @return
     */
    @RequestMapping("dialogFormVisible4Edit")
    public Result dialogFormVisible4Edit(Integer id) {
        //需要查询checkGroup ,checkitmes checkItemIds
        Map map = checkGroupService.findDialogFormVisible4Edit(id);
        if (map != null && map.size() > 0) {
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, map);
        }
        return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    /**
     * 编辑提交数据
     *
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @RequestMapping("edit")
    public Result edit(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {

        try {
            checkGroupService.Update(checkGroup, checkitemIds);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }

    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping("delete")
    public Result delete(Integer id) {
        //根据id查询是否存在引用关系
        Integer count = checkGroupService.findCountById(id);
        if (count >= 1 && count != null) {
            //如果存在引用关系,就提示要存在引用关系,不可直接删除
            throw new DeleteException("检查组与检查项存在关联,不可直接删除");
        } else {
            //不存在就直接删除(逻辑删除)
            try {
                checkGroupService.delete(id);
                return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }

    }
}
