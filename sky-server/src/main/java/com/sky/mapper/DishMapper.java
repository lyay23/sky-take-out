package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

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
     * @param categoryId 菜品分类id
     * @return 菜品数量
     */
    @Select("select count(id) from dish where category_id=#{categoryId} ")
     Integer countByCategoryId(Long categoryId);

    /**
     * 插入菜品
     * @param dish 菜品实体
     */
    // 公共字段填充
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 分页查询菜品
     * @param pageQueryDTO  分页查询条件
     * @return 分页结果
     */
    Page<DishVO> pageQuery(DishPageQueryDTO pageQueryDTO);

    /**
     * 根据菜品id查询菜品
     * @param id 菜品id
     */
    @Select("select * from dish where id=#{id}")
    Dish getById(Long id);

    /**
     * 根据菜品id删除菜品
     * @param id 菜品id
     */
    @Delete("delete from dish where id=#{id}")
    void deleteBatch(Long id);

    /**
     * 批量删除菜品
     * @param ids 多个参数id
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据菜品id更新菜品
     * @param dish 菜品实体
     */
    @AutoFill(value = OperationType.UPDATE)
    void updateById(Dish dish);

    /**
     * 根据菜品类型id查询菜品
     * @param dish 菜品类型id
     * @return 菜品和口味
     */
    List<Dish> list(Dish dish);

    /**
     * 根据菜品id查询菜品和口味
     * @param id 菜品id
     * @return 菜品和口味
     */

    List<Dish> getBySetmealId(Long id);

    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);

}
