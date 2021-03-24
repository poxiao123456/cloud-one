package com.poxiao.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author qq
 * @date 2021/1/20
 */
@RestController
public class PaymentController {

    /**
     * 测试可以将本服务注册到zookeeper注册中心
     */

    @Value("${server.port}")
    private  String   serverPort;

    @RequestMapping(value = "payment/zk")
    public String paymentZk() {
        return "SpringCloud with zookeeper:" + serverPort + "\t" + UUID.randomUUID().toString();
    }


    /**
     * 测试zookeeper注册中心远程调用
     */

    private static final String INVOKE_URL = "http://cloud-one-zookeeper";
    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/consumer/payment/zk")
    public String paymentInfo() {
        return restTemplate.getForObject(INVOKE_URL + "/payment/zk", String.class);
    }


}
