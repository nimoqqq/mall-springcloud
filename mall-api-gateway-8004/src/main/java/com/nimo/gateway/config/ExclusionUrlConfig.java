package com.nimo.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: mall-springcloud
 * @ClassName: ExclusionUrlConfig
 * @description: 白名单配置
 * @author: chuf
 * @create: 2022-03-21 13:31
 **/
@Data
@Component
@ConfigurationProperties(prefix = "exclusion")
public class ExclusionUrlConfig {

    private List<String> url;

}
