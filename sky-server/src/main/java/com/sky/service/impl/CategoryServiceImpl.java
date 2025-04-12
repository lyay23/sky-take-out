package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/12/9:26
 * @Description: 分类服务实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 新增分类
     * @param categoryDTO 分类信息
     */
    @Override
    public void save(CategoryDTO categoryDTO) {
        // 1. 将dto转为实体对象
        Category category = new Category();
        // 2. 拷贝
        BeanUtils.copyProperties(categoryDTO, category);
        // 3. 设置默认状态为禁用，需要手动设置启用
        category.setStatus(StatusConstant.DISABLE);
        // 4.设置操作时间,操作人
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());
        // 5. 保存到数据库
        categoryMapper.insert(category);





    }
}
