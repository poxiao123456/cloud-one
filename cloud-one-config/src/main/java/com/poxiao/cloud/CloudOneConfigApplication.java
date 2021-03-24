package com.poxiao.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
//curl -X POST “http://localhost:8888/actuator/bus-refresh” 一次发送处处生效
//curl -X POST “http://localhost:8888/actuator/bus-refresh/config-client:3355”
public class CloudOneConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudOneConfigApplication.class, args);
    }

}
