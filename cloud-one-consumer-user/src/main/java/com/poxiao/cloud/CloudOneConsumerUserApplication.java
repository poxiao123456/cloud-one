package com.poxiao.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * openFeign和ribbon的教程
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
// @RibbonClient(name = "CLOUD-ONE-USER", configuration = MySelfRule.class) 定制化CLOUD-ONE-USER服务的负载均衡规则，只有在定制化额时候才需要这个注解
@EnableFeignClients("com.poxiao.cloud.feign")
public class CloudOneConsumerUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudOneConsumerUserApplication.class, args);
    }

}
