package com.sky.controller.admin;

import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @Author: 李阳
 * @Date: 2025/04/13/17:38
 * @Description: 套餐管理的一系列接口
 */
@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐管理")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增菜品
     */
    @ApiOperation(value = "新增套餐")
    @PostMapping
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐:{}", setmealDTO);
        setmealService.save(setmealDTO);
        return Result.success();
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public Result<PageResult> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO)  {
        log.info("分页查询菜品:{}", setmealPageQueryDTO);
        PageResult pageResult= setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除套餐
     * - 可以一次删除一个套餐，也可以批量删除套餐
     * - 起售中的套餐不能删除
     */
    @ApiOperation(value = "删除套餐")
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids)  {
        log.info("删除套餐:{}", ids);
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改套餐
     */

    //根据id查询套餐
    @ApiOperation(value = "根据id查询套餐")
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id)  {
        log.info("根据id查询套餐:{}", id);
        SetmealVO setmealVO = setmealService.getByIdWithCatory(id);
        return Result.success(setmealVO);
    }
}
