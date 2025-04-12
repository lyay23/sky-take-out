package com.sky.mapper;

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
}
