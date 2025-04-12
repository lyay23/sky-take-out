package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping
    public Result save(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类：{}",categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }



}
