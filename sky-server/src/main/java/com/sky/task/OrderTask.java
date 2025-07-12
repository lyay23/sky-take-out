package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/07/12/8:14
 * @Description: 定时任务类，用于处理订单状态
 * 每隔1分钟检查用户端的支付状态，未支付的订单隔15分钟状态变为取消
 * 每天凌晨1点检查订单状态，将派送中改为已经完成
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;
    /**
     * 处理超时订单(每分钟触发一次)
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder(){
        log.info("定时处理超时订单：{}", LocalDateTime.now());

        // 当前时间减去15分钟
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        // 查询出来的数据可能有多条
        List<Orders>  ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);

        if(ordersList!=null && !ordersList.isEmpty()){
            for(Orders order:ordersList){
                // 将订单状态改为取消
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("订单超时,自动取消");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }

    /**
     * 处理派送中订单(每天凌晨1点触发一次)
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("定时处理派送中订单：{}", LocalDateTime.now());

        // 计算 当天的所有订单
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);

        // 查询出所有派送中的订单
        List<Orders>  ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS,time);

        if(ordersList!=null && !ordersList.isEmpty()){
            for(Orders order:ordersList){
                // 将订单状态改为已完成
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }

    }
}
