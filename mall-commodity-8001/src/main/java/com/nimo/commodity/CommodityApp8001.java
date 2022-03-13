package com.nimo.commodity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @program: mall-springcloud
 * @ClassName: CommodityApp8001
 * @description:
 * @author: chuf
 * @create: 2022-03-13 09:37
 **/
@SpringBootApplication
@EnableEurekaClient
public class CommodityApp8001 {
    public static void main(String[] args) {
        SpringApplication.run(CommodityApp8001.class, args);
    }
}
