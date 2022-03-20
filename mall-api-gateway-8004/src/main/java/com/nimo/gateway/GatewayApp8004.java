package com.nimo.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @program: mall-springcloud
 * @ClassName: GatewayApp8004
 * @description:
 * @author: chuf
 * @create: 2022-03-20 23:04
 **/
@SpringBootApplication
@EnableEurekaClient
public class GatewayApp8004 {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApp8004.class, args);
    }
}
