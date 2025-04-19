package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/19/14:56
 * @Description: 微信端用户服务接口
 */
public interface UserService {

    /**
     * 微信用户登录
     * @param userLoginDTO
     * @return
     */
     User wxLogin(UserLoginDTO userLoginDTO);
}
