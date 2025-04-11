package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO 员工登录信息
     * @return 员工登录结果
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeedDTO 员工信息
     */
    void save(EmployeeDTO employeedDTO);

    /**
     * 分页查询
     * @param employeePageQueryDTO 查询条件
     * @return 员工数据分页对象
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用禁用员工账号
     * @param status 状态
     * @param id id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据id查询员工
     * @param id id
     * @return 员工信息
     */
    Employee getById(Long id);

    /**
     * 修改员工信息
     * @param employeeDTO 员工信息
     */
    void update(EmployeeDTO employeeDTO);
}
