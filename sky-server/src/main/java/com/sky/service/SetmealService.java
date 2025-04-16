package com.sky.service;

import com.sky.dto.SetmealDTO;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/13/17:42
 * @Description: 套餐管理相关Service
 */
public interface SetmealService {

    /**
     * 新增套餐
     */
    void save(SetmealDTO setmealDTO);
}
