package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/12/9:25
 * @Description: 分类管理的服务层接口
 */
public interface CategoryService {

    /**
     * 新增分类
     * @param categoryDTO 新增分类
     */
    void save(CategoryDTO categoryDTO);

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO 分类分页查询
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);
}
