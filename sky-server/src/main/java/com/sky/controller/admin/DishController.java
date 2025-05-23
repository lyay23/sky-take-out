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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/12/21:46
 * @Description: 菜品管理(当我们需要新增菜品，修改菜品，删除菜品时，我们的数据都会发生变化，
 * ，所以我们需要清理redis的缓存数据，然后重新添加数据页面才会显示修改)
 */
@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品相关接口")
public class DishController {

    @Autowired
    private  DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;
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

        //清理redis缓存数据
        String key = "dish_"+dishDTO.getCategoryId();
        cleanCache(key);
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

        // 当我们删除菜品时，可能需要关联多个菜品表，因此直接清理所有的缓存数据
        // 所有以dish_开头的key都要删除

        cleanCache("dish_*");

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

        //清理redis缓存数据,当我们修改菜品名称时只会涉及一份数据，当我们需要修改
        //分类时，就需要涉及多份数据，比如我修改了菜品的套餐分类，那么之前的套餐下
        //就会少一份数据，修改后套餐下就会多一份数据
        // 所有以dish_开头的key都要删除
        cleanCache("dish_*");

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


    /**
     * 菜品停售起售功能
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品停售起售功能")
    public Result updateStatus(@PathVariable Integer status, Long id){
        log.info("菜品停售起售功能:{},id为:{}", status,id);
        dishService.updateStatus(status, id);

        //清理redis缓存数据
        cleanCache("dish_*");

        return Result.success();
    }

    //统一清理redis缓存数据
    private void  cleanCache(String patten){
        //清理redis缓存数据
        Set keys = redisTemplate.keys(patten);
        redisTemplate.delete(keys);
    }
}
