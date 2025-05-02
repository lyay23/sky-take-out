package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/19/15:29
 * @Description:
 */
@Mapper
public interface UserMapper {

    /**
     * 根据openid查询用户信息
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    /**
     * 插入用户信息
     */
    void insert(User user);

    /**
     * 根据用户id查询用户信息
     */
    @Select("select * from user where id = #{userId}")
    User getById(Long userId);
}
