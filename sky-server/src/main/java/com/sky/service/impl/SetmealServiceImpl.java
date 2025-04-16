package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/13/17:44
 * @Description: 套餐管理相关Service的实现类
 */
@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private DishMapper dishMapper;

    /**
     * 新增套餐
     * @param setmealDTO 实现类
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 保存套餐信息（基本属性）
        setmealMapper.insert(setmeal);

        //获取套餐的id
        Long setmealId = setmeal.getId();

        //向套餐和菜品中间表中添加多条数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        // 遍历集合 遍历SetmealDish对象，将id赋值给SetmealDish对象
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            setmealDishes.forEach(dish -> dish.setSetmealId(setmealId));

        }

        //插入n条数据-动态sql
        setmealDishMapper.insertList(setmealDishes);

    }



    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        // 开始分页查询
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page =setmealMapper.pageQuery(setmealPageQueryDTO);

        // 获取分页查询结果,并将结果封装到PageResult中
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBatch(List<Long> ids) {
        // 1. 判断菜品是不是售卖中
        for (Long id : ids) {
            // 查询菜品是否被售卖中
           Setmeal setmeal = setmealMapper.getById(id);
            if(setmeal.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        // 删除套餐和菜品的关联关系
        setmealMapper.deleteBatch(ids);
        setmealDishMapper.deleteBatch(ids);

    }

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    @Override
    public SetmealVO getByIdWithCatory(Long id) {
        Setmeal setmeal= setmealMapper.getById(id);

        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);

        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    @Override
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        Long setmealId = setmealDTO.getId();
        // 删除套餐和菜品的关联关系
        setmealDishMapper.deleteBySetmealId(setmealId);


        // 向套餐和菜品中间表中添加多条数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });

        //插入n条数据-动态sql
        setmealDishMapper.insertList(setmealDishes);
    }

    /**
     * 更新套餐状态
     * @param status
     * @param id
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        /*
        - 可以对状态为起售的套餐进行停售操作，可以对状态为停售的套餐进行起售操作
        - 起售的套餐可以展示在用户端，停售的套餐不能展示在用户端
        - 起售套餐时，如果套餐内包含停售的菜品，则不能起售
         */
        if(status==StatusConstant.ENABLE) {
            List<Dish> dishList = dishMapper.getBySetmealId(id);
            if (dishList != null && dishList.size() > 0) {
                dishList.forEach(dish -> {
                    if (StatusConstant.DISABLE == dish.getStatus()) {
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }

}
