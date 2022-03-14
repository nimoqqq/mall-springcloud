package com.nimo.security.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @program: mall-springcloud
 * @ClassName: MyBatisConfig
 * @description:
 * @author: chuf
 * @create: 2022-03-14 14:25
 **/
@Configuration
@MapperScan({"com.nimo.security.mbg.mapper","com.nimo.security.dao"})
public class MyBatisConfig {
}
