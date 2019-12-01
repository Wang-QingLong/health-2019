package com.itcast.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itcast.entity.PageResult;
import com.itcast.mapper.CheckGroupMapper;
import com.itcast.mapper.CheckItemMapper;
import com.itcast.pojo.CheckGroup;
import com.itcast.pojo.CheckItem;
import com.itcast.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/20 20:25
 * @description: 检查组服务类
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupMapper checkGroupMapper;
    @Autowired
    private CheckItemMapper checkItemMapper;

    /**
     * 分页查询
     *
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //使用分页插件(告诉分页拦截器我现在要分页)
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page = checkGroupMapper.findPage(queryString);

        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 新增检查组数据
     *
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //将前台返回的基本信息加入到t_checkgroup当中
        checkGroupMapper.add(checkGroup);
        setCheckGroupAndCheckItem(checkGroup.getId(), checkitemIds);

    }

    /**
     * Id查询
     *
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupMapper.findById(id);
    }

    /**
     * 通过检查组Id查询检查项数据
     *
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupMapper.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     * 编辑回显 三合一查询
     * 需要查询checkGroup ,checkitmes checkItemIds
     *
     * @param id
     * @return
     */
    @Override
    public Map findDialogFormVisible4Edit(Integer id) {
        //创建Map集合封装三个数据
        Map<String, Object> map = new HashMap<String, Object>();
        //Id查询检查组
        CheckGroup checkGroup = checkGroupMapper.findById(id);
        //查询所有检查项数据
        List<CheckItem> checkItems = checkItemMapper.findAll();
        //查询回显检查项Id数据
        List<Integer> checkItemIds = checkGroupMapper.findCheckItemIdsByCheckGroupId(id);
        //进行封装
        map.put("checkGroup", checkGroup);
        map.put("checkItems", checkItems);
        map.put("checkItemIds", checkItemIds);
        return map;
    }

    /**
     * 更新检查组
     *
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void Update(CheckGroup checkGroup, Integer[] checkitemIds) {
        //首先需要删除原来的关联关系,根据检查组id删除中间表数据（清理原有关联关系）
        checkGroupMapper.deleteAssociation(checkGroup.getId());
        //向中间表(t_checkgroup_checkitem)插入数据（建立检查组和检查项关联关系）
        setCheckGroupAndCheckItem(checkGroup.getId(), checkitemIds);
        //更新检查组基本信息
        checkGroupMapper.Update(checkGroup);
    }

    /**
     * 查询中间表数据
     *
     * @param id
     * @return
     */
    @Override
    public Integer findCountById(Integer id) {

        return checkGroupMapper.findCountById(id);
    }

    /**删除
     * @param id
     */
    @Override
    public void delete(Integer id) {
        checkGroupMapper.delete(id);
    }

    /**查询所有检查组信息
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {

      return   checkGroupMapper.findAll();
    }

    /**
     * 建立检查项与检查组之间的关系
     *
     * @param checkGroupId
     * @param checkitemIds
     */
//    public void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
//           //判断返回的数组是否为空
//        if (checkitemIds != null && checkitemIds.length > 0) {
//           //不为空,则遍历
//            for (Integer checkitemId : checkitemIds) {
//                //通过t_checkgroup_checkitem建立关联
//
//                //先将两张表之间的关联数据封装到map集合
//                Map<String, Integer> map = new HashMap<>();
//                map.put("checkgroup_id",checkGroupId);
//                map.put("checkitem_id",checkitemId);
//                checkGroupMapper.setCheckGroupAndCheckItem(map);
//            }
//        }
//    }
//


    /**
     * 优化
     * 建立检查项与检查组之间的关系
     *
     * @param checkGroupId
     * @param checkitemIds
     */
    public void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
        //判断返回的数组是否为空
        if (checkitemIds != null && checkitemIds.length > 0) {
            //不为空,则遍历
            //先将两张表之间的关联数据封装到map集合
            List<Map> maps = new ArrayList<>();
            for (Integer checkitemId : checkitemIds) {
                Map map = new HashMap();
                map.put("checkgroup_id", checkGroupId);
                map.put("checkitem_id", checkitemId);
                maps.add(map);
            }
            checkGroupMapper.setCheckGroupAndCheckItem(maps);

        }
    }

}
