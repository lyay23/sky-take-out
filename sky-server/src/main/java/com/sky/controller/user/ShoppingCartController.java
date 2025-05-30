package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/28/14:43
 * @Description: 购物车相关接口
 */
@RestController("userShopCartController")
@RequestMapping("/user/shoppingCart")
@Api(tags = "C端-购物车接口")
@Slf4j

public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;
    /**
     *  添加购物车
     */
    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车,商品信息为:{}", shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车
     */
    @ApiOperation("查看购物车")
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list() {

        List<ShoppingCart> list=shoppingCartService.showShoppingCart();
        log.info("查看购物车,商品信息为:{}", list);
        return Result.success(list);
    }

    /**
     * 清空购物车
     */

    @ApiOperation("清空购物车")
    @DeleteMapping("/clean")
    public Result delete() {
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }

    /**
     * 删除单个商品
     */
    @ApiOperation("删除单个商品")
    @PostMapping("sub")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("删除单个商品,商品信息为:{}", shoppingCartDTO);
        shoppingCartService.sub(shoppingCartDTO);
        return Result.success();
    }
}
