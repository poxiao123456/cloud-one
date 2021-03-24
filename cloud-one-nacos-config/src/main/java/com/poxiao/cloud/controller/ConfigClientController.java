package com.poxiao.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qq
 * @date 2021/1/14
 * Nacos等价于=eureka+config1+Bus
 * 和其它注册中心区别(eureka,zookeeper,consul)
 * 社区活跃度，CAP，健康检查，雪崩保护，自动注销，多数据中心，跨注册中心
 */
@RestController
@RefreshScope //支持Nacos的动态刷新功能。
public class ConfigClientController {

//    @Value("${config1.info}")
//    private String configInfo;

    @Value("${config1.info1}")
    private String configInfo1;

    @GetMapping("/config/info")
    public String getConfigInfo() {
        //System.out.println("configinfo:"+configInfo);
        System.out.println("configinfo1:"+configInfo1);
        return configInfo1;
    }

    /**
     * 配置：dataId的规则
     * ${spring.application.name}-${spring.profile.active}.${spring.cloud.nacos.config1.file-extension}
     * 例：cloud-one-nacos-config1-dev.yaml
     */


    /**
     * Namespace+Group+Data ID
     * Namespace+Group+Service+Cluster+instance_id
     * Namespace：用于区分环境
     * Group：可以区别服务类型
     * Cluster：比如杭州机房和广州机房
     */

    /**
     * nacos集群
     * 1.直连ip 2.vip 3.域名+vip(可读性)
     * nacos集群需要数据库做持久化，为了保证多个nacos节点数据一致性，采用了集中式存储，目前只支持MySQL
     * 一个nginx+3个nacos+1个mysql
     */
}
