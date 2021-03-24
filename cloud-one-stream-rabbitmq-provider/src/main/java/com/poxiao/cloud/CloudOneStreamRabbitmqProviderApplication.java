package com.poxiao.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CloudOneStreamRabbitmqProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudOneStreamRabbitmqProviderApplication.class, args);
    }

}
