package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;
    /**
     * 新增分类
     * @param categoryDTO 分类信息
     */
    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        //属性拷贝
        BeanUtils.copyProperties(categoryDTO, category);

        //分类状态默认为禁用状态0
        category.setStatus(StatusConstant.DISABLE);

        //设置创建时间、修改时间、创建人、修改人
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.insert(category);
    }


    /**
     * 分类分页查询
     * @param categoryPageQueryDTO 分页查询条件
     */
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        // 开始分页查询
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        Page<Category> page =categoryMapper.pageQuery(categoryPageQueryDTO);

        // 获取分页查询结果,并将结果封装到PageResult中
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 启用禁用分类
     * @param status 状态
     * @param id 分类id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        // 创建一个Category对象，设置状态和id
        Category category = Category.builder()
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .id(id)
                .build();
        categoryMapper.update(category);
    }

    /**
     * 删除分类，删除分类时，需要判断该分类是否关联了菜品或套餐，如果关联了，则不能删除
     * @param id 分类id
     */
    @Override
    public void delete(Long id) {
        // 查询当前分类是否关联了菜品，如果关联了，则不能删除
        Integer countByCategoryId = dishMapper.countByCategoryId(id);
        if (countByCategoryId > 0) {
            // 当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH) {
            };
        }

        // 查询当前分类是否关联了套餐，如果关联了，则不能删除
        Integer countBySetmealId = setmealMapper.countByCategoryId(id);
        if (countBySetmealId > 0) {
            // 当前分类下有套餐，不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        categoryMapper.deleteById(id);

    }

    /**
     * 根据类型查询分类
     * @param type 分类类型
     * @return 分类信息
     */
    @Override
    public List<Category> list(Integer type) {
        return categoryMapper.list(type);
    }

    @Override
    public void update(CategoryDTO categoryDTO) {

        // 1. 创建一个Category对象，设置更新后的信息
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        // 2. 设置更新时间、更新人
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());

        // 3. 调用mapper的update方法，更新分类信息
        categoryMapper.update(category);
    }
}
