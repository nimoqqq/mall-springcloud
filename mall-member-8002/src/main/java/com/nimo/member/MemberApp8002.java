package com.nimo.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @program: mall-springcloud
 * @ClassName: MemberApp8002
 * @description:
 * @author: chuf
 * @create: 2022-03-13 15:48
 **/
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages={"com.nimo.common.services","com.nimo.member"})
@EnableEurekaClient
public class MemberApp8002 {
    public static void main(String[] args) {
        SpringApplication.run(MemberApp8002.class, args);
    }
}
