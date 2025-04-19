package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.MarshalException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/19/15:11
 * @Description: 用户登录的实现类
 */
@Service
public class UserServiceImpl implements UserService {

    // 微信登录接口
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;
    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        // 调用微信接口服务，获得当前用户的openId
        String openid = getOpenId(userLoginDTO.getCode());
        // 判断openId是否为空，如果为空表示登录失败，抛出异常
         if (openid == null) {
             throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
         }
        // 根据openId查询用户信息，
        User user = userMapper.getByOpenid(openid);

        // 如果查询不到，表示是新用户，需要注册，
         if (user == null) {

            // 注册
             user =  User.builder()
                     .openid(openid)
                     .createTime(LocalDateTime.now())
                     .build();
            userMapper.insert(user);
        }
        // 注册成功后返回用户信息
        return user;

    }

    /**
     * 调用微信接口服务，获得当前用户的openId
     * @param code
     * @return
     */
    private String getOpenId(String code) {
        Map<String, String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String s = HttpClientUtil.doGet(WX_LOGIN, map);
        JSONObject jsonObject = JSON.parseObject(s);
        String openid =(String)jsonObject.get("openid");
        return openid;
    }
}
