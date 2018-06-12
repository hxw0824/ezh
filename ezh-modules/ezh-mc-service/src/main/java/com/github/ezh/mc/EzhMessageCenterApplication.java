package com.github.ezh.mc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author solor
 * @date 2018年01月15日13:24:23
 * 消息中心
 */
@EnableDiscoveryClient
@SpringBootApplication
public class EzhMessageCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(EzhMessageCenterApplication.class, args);
    }

}
