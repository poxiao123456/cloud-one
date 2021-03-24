package com.poxiao.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CloudOneNacosProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudOneNacosProviderApplication.class, args);
    }

}
