package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/05/02/14:26
 * @Description: 订单表
 */
@Mapper
public interface OrderMapper {

    /**
     * 插入订单
     * @param orders
     */
     void insert(Orders orders);
}
