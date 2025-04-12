package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/12/9:17
 * @Description: 分类功能的一系列接口
 */
@Slf4j
@RestController
@Api(tags ="分类功能接口")
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param categoryDTO 分类信息
     * @return 新增结果
     */
    @ApiOperation(value = "新增分类")
    @PostMapping
    public Result<String> save(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类：{}",categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO 分类信息
     * @return 分类分页结果
     */
    @ApiOperation(value = "分类分页查询")
    @GetMapping("/page")
    public Result<PageResult> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分类分页查询：{}",categoryPageQueryDTO);
        PageResult pageResult =categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用禁用分类
     * @param status 状态 1 启用 0 禁用
     * @param id 分类id
     * @return 启用禁用结果
     */
    @ApiOperation(value = "启用禁用分类")
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status,Long id){
        log.info("启用禁用分类：{},id为{}",status,id);
        categoryService.startOrStop(status,id);
        return Result.success();
    }

    /**
     * 删除分类
     * @param id 分类id
     * @return 删除结果
     */
    @ApiOperation(value = "删除分类")
    @DeleteMapping
    public Result delete(Long id){
        log.info("删除分类：{}",id);
        categoryService.delete(id);
        return Result.success();
    }



}
