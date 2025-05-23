package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/12/22:20
 * @Description: 菜品口味
 */
@Mapper
public interface DishFlavorMapper {
    /**
     * 批量插入口味数据
     * @param flavors
     */
    void insertBach(List<DishFlavor> flavors);

    /**
     * 根据菜品id删除口味数据
     * @param dishId 菜品id
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteBatch(Long dishId);

    /**
     * 根据菜品id集合查询菜品口味
     * @param dishIds 菜品id
     * @return 菜品口味
     */
    void deleteByIds(List<Long> dishIds);

    /**
     * 根据菜品id查询菜品口味
     * @param dishId 菜品id
     * @return 菜品口味
     */
    List<DishFlavor> getByDishId(Long dishId);
}
