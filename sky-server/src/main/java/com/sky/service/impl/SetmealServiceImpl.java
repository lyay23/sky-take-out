package com.sky.service.impl;

import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetmealService;
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
        Long setmealId = setmeal.getCategoryId();

        //向套餐和菜品中间表中添加多条数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        // 遍历集合 遍历SetmealDish对象，将id赋值给SetmealDish对象
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            setmealDishes.forEach(dish -> dish.setSetmealId(setmealId));

        }

        //插入n条数据-动态sql
        setmealDishMapper.insertList(setmealDishes);

    }
}
