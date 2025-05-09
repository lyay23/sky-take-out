package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/05/09/10:25
 * @Description: 商店地址
 */
@Component
@ConfigurationProperties(prefix = "sky.shop")
@Data
public class ShopProperties {
    private String address;
}
