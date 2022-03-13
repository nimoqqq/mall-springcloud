package com.nimo.commodity.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @program: mall-springcloud
 * @ClassName: MyBatisConfig
 * @description:
 * @author: chuf
 * @create: 2022-03-13 09:39
 **/
@Configuration
@MapperScan("com.nimo.commodity.mbg.mapper")
public class MyBatisConfig {

}
