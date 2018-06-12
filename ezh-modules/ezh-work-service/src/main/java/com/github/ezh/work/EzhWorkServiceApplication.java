package com.github.ezh.work;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.github.ezh.work","com.github.ezh.common.bean"})
public class EzhWorkServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EzhWorkServiceApplication.class, args);
    }

}
