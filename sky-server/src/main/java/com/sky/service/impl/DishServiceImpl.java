package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/12/21:51
 * @Description: 菜品管理
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private SetmealMapper setmealMapper;
    /**
     * 新增菜品和口味
     * @param dishDTO 菜品和口味
     */
    @Override
    @Transactional // 开启事务两个操作要么都成功要么都失败
    public void saveWithFlavor(DishDTO dishDTO) {
        //向菜品表添加一条数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        // 向表中添加数据
        dishMapper.insert(dish);

        // 获取id
        Long dishId = dish.getId();

        // 向口味表添加多条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            // 遍历集合 遍历DishFlavor对象，将id赋值给DishFlavor对象
           flavors.forEach(flavor -> flavor.setDishId(dishId));

            // 插入n条数据-动态sql
            dishFlavorMapper.insertBach(flavors);
        }
    }

    /**
     * 菜品分页查询
     * @param pageQueryDTO 分页查询条件
     * @return 分页结果
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO pageQueryDTO) {
        PageHelper.startPage(pageQueryDTO.getPage(),pageQueryDTO.getPageSize());
        // DishVO实际传输过来的数据
        Page<DishVO> page = dishMapper.pageQuery(pageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 菜品批量删除
     * @param ids 多个参数id
     *
     */
    @Override
    @Transactional // 开启事务两个操作要么都成功要么都失败
    public void deleteBatch(List<Long> ids) {
        // 1. 判断菜品是不是售卖中
        for (Long id : ids) {
            // 查询菜品是否被售卖中
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        // 2. 判断当前菜品是不是被套餐关联
         List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishId(ids);
         if (setmealIds != null && !setmealIds.isEmpty()) {
             throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
         }

        // 删除菜品表数据
//        for (Long id : ids) {
//            dishMapper.deleteBatch(id);
//            // 根据菜品删除口味表数据
//            dishFlavorMapper.deleteBatch(id);
//        }
        //根据菜品id集合删除菜品表数据以及口味
         dishMapper.deleteByIds(ids);
         dishFlavorMapper.deleteByIds(ids);
    }

    /**
     * 根据id查询菜品和口味
     * @param id 菜品id
     * @return 菜品和口味
     */
    @Override
    public DishVO getByIdWithFlavor(Long id) {
        //查询菜品表-在删除时已经写过了
        Dish dish = dishMapper.getById(id);
        // 根据菜品查询口味表
        List<DishFlavor> dishFlavors =dishFlavorMapper.getByDishId(id);
        //拼接到vo中
         DishVO dishVO = new DishVO();
         BeanUtils.copyProperties(dish,dishVO);
         dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /**
     * 修改菜品和口味
     * @param dishDTO 菜品和口味
     */
    @Transactional // 开启事务两个操作要么都成功要么都失败
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        // 因为我们的dto对象中有关于口味的属性，为了合理利用，所以需要将dto对象中的属性赋值给dish对象
        BeanUtils.copyProperties(dishDTO,dish);
        // 修改菜品表数据
        dishMapper.updateById(dish);
        // 根据菜品id删除口味表数据
        dishFlavorMapper.deleteBatch(dishDTO.getId());
        // 向口味表添加多条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            // 遍历集合 遍历DishFlavor对象，将id赋值给DishFlavor对象
            flavors.forEach(flavor -> flavor.setDishId(dishDTO.getId()));

            // 插入n条数据-动态sql
            dishFlavorMapper.insertBach(flavors);
        }

    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }

    /**
     * 菜品停售起售功能
     * @param status 状态
     * @param id 菜品id
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        // 挑选出我们需要的参数
       Dish dish = Dish.builder()
               .id(id)
               .status(status)
               .build();
       dishMapper.updateById(dish);

       if (status == StatusConstant.ENABLE) {
           // 如果是停售操作，还需要将包含当前菜品的套餐也停售
           List<Long> dishIds = new ArrayList<>();
           dishIds.add(id);
           // select setmeal_id from setmeal_dish where dish_id in (?,?,?)
           List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(dishIds);
           if (setmealIds != null && setmealIds.size() > 0) {
               for (Long setmealId : setmealIds) {
                   Setmeal setmeal = Setmeal.builder()
                           .id(setmealId)
                           .status(StatusConstant.DISABLE)
                           .build();
                   setmealMapper.update(setmeal);
               }
           } // 起售
           // 查询当前菜品是否被套餐关联

       }
    }

    /**
     * 根据菜品id查询菜品和口味
     * @param dish 菜品id
     * @return
     */

    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
