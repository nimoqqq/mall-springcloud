package com.nimo.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @program: mall-springcloud
 * @ClassName: SecurityApp8003
 * @description:
 * @author: chuf
 * @create: 2022-03-14 14:46
 **/
@SpringBootApplication
@EnableEurekaClient
public class SecurityApp8003 {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApp8003.class, args);
    }
}
