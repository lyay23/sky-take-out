package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

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

    /**
     * 菜品分页查询
     * @param pageQueryDTO 分页查询条件
     * @return 分页结果
     */
    PageResult pageQuery(DishPageQueryDTO pageQueryDTO);

    /**
     * 菜品批量删除
     * @param ids 多个参数id
     *
     */
    void deleteBatch(List<Long> ids);
}
