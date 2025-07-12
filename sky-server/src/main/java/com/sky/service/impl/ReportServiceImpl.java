package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/07/12/14:42
 * @Description: 统计报表的实现类
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;


     /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {

        // 当前集合用于存放从begin 到 end 范围内的每天的日期
        List<LocalDate> dateList =new ArrayList<>();
        // 首先将开始日期存放进去
        dateList.add(begin);
        // 循环，将结束日期之前的日期都存放到集合中
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        //存放每天的营业额
        List<Double> turnoverList = new ArrayList<>();
        // 遍历dateList集合中的日期
        for(LocalDate date: dateList){
            // 查询date日期对应的营业额（当天状态为已经完成的金额合计）,获取当天开始时间以及结束时间
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime=LocalDateTime.of(date,LocalTime.MAX);

            //查询当天营业额的总金额,可以将开始时间结束时间状态封装到Map中
            Map map = new HashMap();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover=orderMapper.sumByMap(map);
            turnover = turnover == null ? 0.0 : turnover;
            //将每天的营业额存放进List集合
            turnoverList.add(turnover);
        }

        // 将集合元素以逗号分割,解析为字符串,封装在VO中
        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(dateList,","))
                .turnoverList(StringUtils.join(turnoverList,","))
                .build();

    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        // 当前集合用于存放从begin 到 end 范围内的每天的日期
        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);
        // 循环，将结束日期之前的日期都存放到集合中
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        // 统计每一天对应的用户数量,新用户和总用户
        List<Integer> newUserList=new ArrayList<>();
        List<Integer> totalUserList=new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime=LocalDateTime.of(date,LocalTime.MAX);

            Map map =new HashMap();
            map.put("end",endTime);

            // 查询总用户数量
            Integer totalUser = userMapper.countByMap(map);
            
            map.put("begin",beginTime);
            Integer newUser = userMapper.countByMap(map);

            totalUserList.add(totalUser);
            newUserList.add(newUser);

        }

        // 封装结果集
        return UserReportVO
                .builder()
                .dateList(StringUtils.join(dateList,","))
                .totalUserList(StringUtils.join(totalUserList,","))
                .newUserList(StringUtils.join(newUserList,","))
                .build();
    }


    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        // 当前集合用于存放从begin 到 end 范围内的每天的日期
        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);
        // 循环，将结束日期之前的日期都存放到集合中
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        List<Integer> orderCountList =new ArrayList<>();
        List<Integer> validOrderCountList=new ArrayList<>();

        // 遍历dateList集合，查询每天的有效订单，订单总数
        for(LocalDate date:dateList){
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime =LocalDateTime.of(date,LocalTime.MAX);
            //查询每天订单总数
            Integer orderCount = getOrderCount(beginTime, endTime, null);
            Integer validOrderCount = getOrderCount(beginTime, endTime, Orders.COMPLETED);

            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);
        }

        // 利用Stream流计算时间区间内的有效订单总数量
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();


        //计算订单完成率
        Double orderCompletionRate=0.0;
        if(totalOrderCount!=0) {
            orderCompletionRate=validOrderCount.doubleValue() / totalOrderCount;
        }

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .orderCountList(StringUtils.join(orderCountList,","))
                .validOrderCountList(StringUtils.join(validOrderCountList,","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 提取公共代码
     * @param begin
     * @param end
     * @param status
     * @return
     */
    private Integer getOrderCount(LocalDateTime begin,LocalDateTime end ,Integer status){
        Map map =new HashMap();
        map.put("begin",begin);
        map.put("end",end);
        map.put("status", status);
        return orderMapper.countByMap(map);
    }



    /**
     * 销量排名Top10
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime=LocalDateTime.of(begin,LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(beginTime, endTime);

        // 1. 将salesTop10集合转换为流
        // 2. 通过map操作将每个GoodsSalesDTO对象映射为其getName()返回的字符串
        // 3. 最后通过collect操作将处理后的元素收集到一个新的List<String>中
        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());

        String nameList = StringUtils.join(names, ",");

        List<Integer> numbers = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String numberList=StringUtils.join(numbers,",");

        return SalesTop10ReportVO.builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();


    }
}
