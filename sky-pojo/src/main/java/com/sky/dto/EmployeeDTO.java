package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 员工数据传输对象
 * Serializable 是 Java 序列化的标准接口，任何实现了 Serializable 接口的类都可以被序列化，
 */
@Data
public class EmployeeDTO implements Serializable {

    private Long id;

    private String username;

    private String name;

    private String phone;

    private String sex;

    private String idNumber;

}
