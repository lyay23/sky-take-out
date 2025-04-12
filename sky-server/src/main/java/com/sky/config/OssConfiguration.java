package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/12/21:06
 * @Description: 用于创建阿里云OSS客户端的配置类
 */
@Configuration
@Slf4j
public class OssConfiguration {
    @Bean //将这个Bean放入到容器中
    @ConditionalOnMissingBean //如果容器中没有这个Bean，则创建这个Bean
    public AliOssUtil aliOssUtil(AliOssProperties a){
        log.info("开始创建阿里云OSS文件上传工具类:{}",a);
        return new AliOssUtil(a.getEndpoint(),a.getAccessKeyId(),a.getAccessKeySecret(),a.getBucketName());
    }

}
