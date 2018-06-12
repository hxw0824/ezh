package com.github.ezh.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author solor
 * @date 2017年10月27日13:59:05
 */
@EnableAsync
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.github.ezh.admin", "com.github.ezh.common.bean"})
public class EzhAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(EzhAdminApplication.class, args);
    }
}