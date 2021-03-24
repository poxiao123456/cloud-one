package com.poxiao.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients({"com.poxiao.cloud.feign"})
public class CloudOneSentinelClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudOneSentinelClientApplication.class, args);
    }

}
