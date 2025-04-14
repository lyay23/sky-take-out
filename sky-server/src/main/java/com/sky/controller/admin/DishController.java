package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
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

    /**
     * 新增菜品和口味
     * @param dishDTO
     * @return
     */
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


    /**
     * 删除菜品
     * 业务规则：
     * 1. 可以一次删除多个菜品，也可以一次删除一个菜品（动态sql）
     * 2. 删除菜品时，需要级联删除菜品和口味的关联关系（多表连接）
     * 3.起售的菜品不能删除（判断）
     * 4.被套餐关联的菜品不能删除（需要关联事务，异常处理）
     */
    @ApiOperation("删除菜品")
    @DeleteMapping
    // RequestParam 从请求中提取传来的参数,解析成List<Long>里面的数据
    public Result delete(@RequestParam List<Long> ids)  {
        log.info("删除菜品:{}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改菜品和口味
     * 接口设计：
     * 1. 根据id查询菜品
     * 2.根据类型查询分类（已实现）
     * 3.文件上传（已实现）
     * 4.修改菜品和口味
     */
    //根据id查询菜品和口味
    @ApiOperation("根据id查询菜品")
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id)  {
        log.info("根据id查询菜品:{}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }
    //修改菜品和口味
    @ApiOperation("修改菜品")
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO)  {
        log.info("修改菜品和口味:{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }


    /**
     * 根据分类id查询菜品
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

}
