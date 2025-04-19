package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/19/10:20
 * @Description: 商店营业状态controller
 */
@RestController("adminShopController")
@Slf4j
@RequestMapping("/admin/shop")
@Api(tags = "商店营业状态")
public class ShopController {

    private static final String SHOP_STATUS = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;

    @PutMapping("/{status}")
    @ApiOperation("商店营业状态")
    public Result shopOpen(@PathVariable Integer status) {
        log.info("商店营业:{}", status==1?"营业中":"打烊中");

        // 设置商店营业状态
        redisTemplate.opsForValue().set(SHOP_STATUS, status);
        return Result.success();
    }

    // 获取商店营业状态
    @GetMapping("/status")
    @ApiOperation("获取商店营业状态")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(SHOP_STATUS);
        log.info("获取商店营业状态:{}", status==1?"营业中":"打烊中");
        return Result.success(status);
    }
}
