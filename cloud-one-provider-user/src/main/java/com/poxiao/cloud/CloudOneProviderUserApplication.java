package com.poxiao.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * hystrix的教程
 * 远程方法调用使用openFeign就可以，因为openFeign包含了ribbon和hystrix
 * 本地方法调用使用hystrix
 */
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
public class CloudOneProviderUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudOneProviderUserApplication.class, args);
    }

}
