package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

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

    /**
     * 分页查询
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据id查询套餐信息
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询套餐信息
     */
    SetmealVO getByIdWithCatory(Long id);

    /**
     * 修改套餐信息
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 更新套餐状态
     * @param status
     * @param id
     */
    void updateStatus(Integer status, Long id);
}
