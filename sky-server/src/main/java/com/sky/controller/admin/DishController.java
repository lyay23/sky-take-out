package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/12/21:46
 * @Description: 菜品管理
 */
@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品相关接口")
public class DishController {

    @Autowired
    private  DishService dishService;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result<Object> save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品和口味:{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     *
     */
    @ApiOperation("分页查询菜品")
    @GetMapping("/page")
    // PageResult 分页查询的统一类 DishPageQueryDTO 前端需要接收的请求实体
    public Result<PageResult> pageQuery( DishPageQueryDTO pageQueryDTO  )  {
        log.info("分页查询菜品:{}", pageQueryDTO);
       PageResult pageResult= dishService.pageQuery(pageQueryDTO);
        return Result.success(pageResult);
    }


}
