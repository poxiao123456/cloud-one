package com.poxiao.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CloudOneNacosConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudOneNacosConsumerApplication.class, args);
    }

}
