package com.sky.mapper;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/13/14:03
 * @Description: 套餐mapper
 */
@Mapper
public interface SetmealDishMapper {

    //多个菜品id查询套餐id ，List为多条数据
    List<Long> getSetmealIdsByDishId(List<Long> dishIds);

    //根据套餐id插入套餐包含的菜品

    void insertList(List<SetmealDish> setmealDishes);

    //根据套餐id删除套餐包含的菜品
    void deleteByIds(List<Long> ids);
}
