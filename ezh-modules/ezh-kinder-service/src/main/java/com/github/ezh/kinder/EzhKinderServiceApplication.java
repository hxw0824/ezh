package com.github.ezh.kinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration
//@EnableFeignClients
@ImportResource(locations = { "classpath:druid-bean.xml" })
@ComponentScan(basePackages = {"com.github.ezh.kinder","com.github.ezh.common.bean"})
public class EzhKinderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EzhKinderServiceApplication.class, args);
    }
}
