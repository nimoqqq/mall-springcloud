package com.nimo.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @program: mall-springcloud
 * @ClassName: EurekaService7001
 * @description:
 * @author: chuf
 * @create: 2022-03-12 21:58
 **/
@SpringBootApplication
@EnableEurekaServer
public class EurekaService7001 {

    public static void main(String[] args) {
        SpringApplication.run(EurekaService7001.class, args);
    }
}
