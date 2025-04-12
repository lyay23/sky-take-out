package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/12/21:51
 * @Description: 菜品管理
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    /**
     * 新增菜品和口味
     * @param dishDTO 菜品和口味
     */
    @Override
    @Transactional // 开启事务两个操作要么都成功要么都失败
    public void saveWithFlavor(DishDTO dishDTO) {
        //向菜品表添加一条数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        // 向表中添加数据
        dishMapper.insert(dish);

        // 获取id
        Long dishId = dish.getId();

        // 向口味表添加多条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            // 遍历集合 遍历DishFlavor对象，将id赋值给DishFlavor对象
           flavors.forEach(flavor -> flavor.setDishId(dishId));

            // 插入n条数据-动态sql
            dishFlavorMapper.insertBach(flavors);
        }

    }
}
