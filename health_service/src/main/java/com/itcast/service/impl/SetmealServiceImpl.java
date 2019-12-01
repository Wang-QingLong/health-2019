package com.itcast.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itcast.constant.RedisConstant;
import com.itcast.entity.PageResult;
import com.itcast.mapper.CheckGroupMapper;
import com.itcast.mapper.CheckItemMapper;
import com.itcast.mapper.SetmealMapper;
import com.itcast.pojo.CheckGroup;
import com.itcast.pojo.CheckItem;
import com.itcast.pojo.Setmeal;
import com.itcast.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/21 22:28
 * @description: 套餐服务
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private CheckGroupMapper checkGroupMapper;

    @Autowired
    private CheckItemMapper checkItemMapper;

    @Autowired
    private JedisPool jedisPool;


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
        Page<Setmeal> page = setmealMapper.findPage(queryString);

        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 新增
     *
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //将前台返回的套餐信息加入到t_setmeal当中
        setmealMapper.add(setmeal);
        //设置关联关系
        setmealIdAndCheckGroupIds(setmeal.getId(), checkgroupIds);

        //将图片名称保存到Redis
        savePic2Redis(setmeal.getImg());
    }

    /**
     * 查询回显数据查询:套餐表单数据,所有检查组数据,被勾选的检查组数据
     *
     * @param id
     * @return
     */
    @Override
    public Map dialogFormVisible4Edit(Integer id) {
        //创建一个map集合,封装这些数据
        Map<String, Object> map = new HashMap<>();
        //获取setmeal数据
        Setmeal setmeal = setmealMapper.findSetmealById(id);
        //获取所有检查组数据
        List<CheckGroup> checkGroups = checkGroupMapper.findAll();
        //获取中间表里面的checkgroupIds
        List<Integer> groupIds = setmealMapper.findCheckGroupIdsById(id);

        //封装需要查询的数据
        map.put("setmeal", setmeal);
        map.put("checkGroups", checkGroups);
        map.put("groupIds", groupIds);

        return map;

    }

    /**
     * 更新数据
     *
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    public void Update(Setmeal setmeal, Integer[] checkgroupIds) {

        //首先需要删除原来的关联关系,根据套餐表id删除中间表数据（清理原有关联关系）
        setmealMapper.deleteAssociation(setmeal.getId());
        //向中间表(t_setmeal_checkgroup)插入数据（建立套餐和检查组关联关系）
        setmealIdAndCheckGroupIds(setmeal.getId(), checkgroupIds);
        //更新套餐基本信息
        setmealMapper.Update(setmeal);
    }

    /**
     * 查询中间表引用关系
     *
     * @param id
     * @return
     */
    @Override
    public Integer findCountById(Integer id) {
        return setmealMapper.findCountById(id);

    }

    /**
     * 逻辑删除套餐数据
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {
        setmealMapper.delete(id);
    }

    /**
     * 将图片名称保存到Redis
     *
     * @param pic
     */
    private void savePic2Redis(String pic) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, pic);
    }


    /**
     * 创建检查组和套餐之间的关联关系，并且加入到中间表t_setmeal_checkgroup表当中
     *
     * @param setmealId
     * @param checkgroupIds
     */
    private void setmealIdAndCheckGroupIds(Integer setmealId, Integer[] checkgroupIds) {

        //判断返回的数组是否为空
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            //不为空,则遍历
            //先将两张表之间的关联数据封装到map集合
            List<Map> maps = new ArrayList<>();
            for (Integer checkgroupId : checkgroupIds) {
                Map map = new HashMap();
                map.put("setmeal_id", setmealId);
                map.put("checkgroup_id", checkgroupId);
                maps.add(map);
            }
            setmealMapper.setCheckGroupAndCheckItem(maps);

        }
    }

    /**
     * 查询所有的套餐数据
     *
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        return setmealMapper.findAll();
    }

    /**
     * 老版方法
     * 根据套餐Id查询套餐内所有数据
     *
     * @param id
     * @return
     */

    public Setmeal findById_(Integer id) {
        //先使用Id查询套餐数据,判断是否有数据
        Setmeal setmeal = setmealMapper.findSetmealById(id);
        if (setmeal != null) {
            //使用套餐的Id查询对应的检查组的Id
            List<Integer> checkGroupIds = setmealMapper.findCheckGroupIdsById(setmeal.getId());
            //判断集合是否为空
            if (CollUtil.isNotEmpty(checkGroupIds)) {
                //创建一个list集合用于封装检查组对象
                ArrayList<CheckGroup> checkGroupArrayList = new ArrayList<>();
                //不为空,则遍历
                for (Integer checkGroupId : checkGroupIds) {
                    //获取检查组对象
                    CheckGroup checkGroup = checkGroupMapper.findById(checkGroupId);
                    //判断是否为空
                    if (checkGroup != null) {
                        //不为空,添加到集合
                        checkGroupArrayList.add(checkGroup);
                        //根据Id查询检查项Ids
                        List<Integer> checkitemIds = checkGroupMapper.findCheckItemIdsByCheckGroupId(checkGroup.getId());
                        //判断集合是否为空
                        if (CollUtil.isNotEmpty(checkitemIds)) {
                            //创建一个集合用于封装查询的检查项数据
                            List<CheckItem> checkItemslist = new ArrayList<>();
                            //不为空
                            for (Integer checkitemId : checkitemIds) {
                                CheckItem checkitem = checkItemMapper.findById(checkitemId);
                                //判断是否为空
                                if (checkitem != null) {
                                    //不为空添加到集合
                                    checkItemslist.add(checkitem);
                                }
                            }
                            //检查组对象封装检查项数据checkItemslist
                            checkGroup.setCheckItems(checkItemslist);
                        }

                    }

                }
                //套餐对象封装检查组数据checkGroupArrayList
                setmeal.setCheckGroups(checkGroupArrayList);
            }
            return setmeal;
        }

        return null;
    }


    /**
     * 优化
     * 根据套餐Id查询套餐内所有的数据
     *
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {
        //为了减少与数据库交互的次数,提高性能,一次性从数据库将我们需要的数据全部拿回来
        //先判断从数据库返回的数据是否为空
        Setmeal setmeal = setmealMapper.findSetmealById(id);
        if (setmeal != null) {
            //不为空

            // 根据套餐id查询套餐下面的检查组
            List<CheckGroup> checkGroups = setmealMapper.findCheckGroupsBySetmealId(id);

            //创建一个集合用于封装所有的检查组Id
            List<Integer> ids = new ArrayList<>();

            // 循环所有的检查组查询对应的检查项集合
            for (CheckGroup checkGroup : checkGroups) {
                ids.add(checkGroup.getId());
            }

            //所有的检查项
            List<CheckItem> checkItems = setmealMapper.findCheckItemsByCheckGroupIds(ids);

            //分组:
            // 创建一个map集合.用于封装一个检查组Id对应多个检查项
            Map<Integer, List<CheckItem>> map = new HashMap<>();

//            // 循环所有的检查项
//            for (CheckItem checkItem : checkItems) {
//                //从查询的检查项里面取检查组checkGroupId
//                Integer checkGroupId = checkItem.getCheckgroupId();
//                //假设map集合当中有数据
//                //通过检查组Id,也就是map集合里面的key,获取检查项数据
//                List<CheckItem> items = map.get(checkGroupId);
//                //如果检查项集合为空(第一次肯定为空)
//                if (items == null) {
//                    //新建一个集合
//                    items = new ArrayList<>();
//                }
//                //往里面装检查项,一个一个装
//                items.add(checkItem);
//                //装入map集合
//                map.put(checkGroupId, items);
//            }

            map = checkItems.stream().collect(Collectors.groupingBy(CheckItem::getCheckgroupId));

            for (CheckGroup checkGroup : checkGroups) {
                checkGroup.setCheckItems(map.get(checkGroup.getId()));
            }

            setmeal.setCheckGroups(checkGroups);

        }
        return setmeal;
    }

}
