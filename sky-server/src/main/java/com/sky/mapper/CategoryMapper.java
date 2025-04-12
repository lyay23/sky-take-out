package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.Mapping;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/12/9:35
 * @Description: 分类管理的Mapper
 */

@Mapper
public interface CategoryMapper {
    /**
     * 新增分类
     * @param category 分类信息
     */
    void insert(Category category);

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO 分页查询条件
     * @return 返回分页结果
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 启用禁用分类
     * @param status 状态
     * @param id 分类id
     */
    void update(Category category);
}
