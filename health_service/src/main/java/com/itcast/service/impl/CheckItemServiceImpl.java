package com.itcast.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itcast.entity.PageResult;
import com.itcast.entity.Result;
import com.itcast.mapper.CheckItemMapper;
import com.itcast.pojo.CheckItem;
import com.itcat.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/18 18:48
 * @description: 检查项服务
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemMapper checkItemDao;

    /**
     * 新增
     *
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

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


//        //使用分页插件(告诉分页拦截器我现在要分页)
//        Page page = PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
//        //写一个普普通通的sql
//        List<CheckItem> checkItems = checkItemDao.findPage(queryPageBean.getQueryString());
//
//        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
//        Page<CheckItem> page = checkItemDao.findPage(queryPageBean.getQueryString());
//        return new PageResult(page.getTotal(),checkItems);


        //使用分页插件(告诉分页拦截器我现在要分页)
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckItem> page = checkItemDao.findPage(queryString);

        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * Id查询
     *
     * @param row
     * @return
     */
    @Override
    public CheckItem findById(int row) {
        return checkItemDao.findById(row);
    }

    /**
     * 编辑
     *
     * @param checkItem
     */
    @Override
    public void edit(CheckItem checkItem) {

        checkItemDao.edit(checkItem);
    }

    /**
     * 删除
     *
     * @param id_
     */
    @Override
    public void delete(Integer id_) {
        checkItemDao.delete(id_);
    }

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }


}
