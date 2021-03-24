package com.poxiao.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard
@EnableDiscoveryClient
/**
 * 启动dashboard: http://localhost:9050/hystrix
 * 要求client有hystrix和actuator依赖
 * 调用的时候使用http://localhost:8001/actuator/hystrix.stream
 * delay默认2000ms，title自定义
 */
public class CloudOneHystrixDashBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudOneHystrixDashBoardApplication.class, args);
    }

}
