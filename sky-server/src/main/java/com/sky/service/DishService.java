package com.sky.service;

import com.sky.dto.DishDTO;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/12/21:50
 * @Description: 菜品管理
 */
public interface DishService {

    /**
     * 新增菜品和口味
     * @param dishDTO 菜品和口味
     */
    void saveWithFlavor(DishDTO dishDTO);

}
