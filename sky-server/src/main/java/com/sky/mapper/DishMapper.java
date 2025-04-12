package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/12/13:03
 * @Description: 菜品管理
 */
@Mapper
public interface DishMapper {

    /**
     * 根据菜品id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id=#{categoryId} ")
     Integer countByCategoryId(Long categoryId);

}
