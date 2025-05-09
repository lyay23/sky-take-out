package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/05/09/10:26
 * @Description: 百度ak
 */
@Component
@ConfigurationProperties(prefix = "sky.baidu")
@Data
public class BaiduProperties {
    private String ak;
}
