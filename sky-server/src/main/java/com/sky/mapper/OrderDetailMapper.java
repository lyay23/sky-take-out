package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/05/02/14:26
 * @Description: 订单详情表
 */
@Mapper
public interface OrderDetailMapper {

    /**
     * 批量插入订单详情
     *
     * @param orderDetailList
     */
    void insertBach(List<OrderDetail> orderDetailList);

    /**
     * 根据订单id查询订单详情
     */
    @Select("select * from order_detail where order_id = #{orderId}")
    List<OrderDetail> getByOrderId(Long orderId);
}