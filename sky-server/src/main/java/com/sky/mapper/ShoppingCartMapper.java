package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/28/15:15
 * @Description: 购物车
 */
@Mapper
public interface ShoppingCartMapper {

    /**
     * 动态查询购物车
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 根据用户id修改数量
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void update(ShoppingCart cart);

    /**
     * 插入购物车数据
     * @param shoppingCart
     */
    void insert(ShoppingCart shoppingCart);

    /**
     * 根据用户id删除购物车数据
     * @param userId
     */
    void deleteByUserId(Long userId);
}
