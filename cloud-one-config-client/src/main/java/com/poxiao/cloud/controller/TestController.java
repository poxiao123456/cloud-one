package com.poxiao.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinqi
 * @date 2020/6/2
 */
@RestController
@RefreshScope
public class TestController {

    @Value("${config.version}")
    private String version;

    /**
     * 先测试可以从本地获取，然后测试git获取
     * 结论是：先加载本地的配置文件，然后再加载配置中心的配置文件。如果配置中心和本地属性相同，那么配置中心的配置会覆盖本地的配置。
     * 而且本地配置文件以bootstrap.yml优先，然后是application.yml，然后是application-dev.yml。
     * 本地如果bootstrap.yml,application.yml,application-dev.yml都存在，那么会按照优先级加载。
     *
     * 如果config中心下线，eureka可能由于保护机制导致config-client还存在，所以依然可以拉取配置中心的内容，只不过拉取的是内存里的东西，如果这个时候刷新自己，可能就获取不到配置中心的内容了。
     *
     * curl -X POST “http://localhost:8888/actuator/refresh”
     * curl -X POST “http://localhost:8006/actuator/refresh”
     * 先刷新配置中心，然后再刷新客户端，这样客户端才能从配置中心取到最新的数据
     *
     *
     * curl -X POST “http://localhost:8006/actuator/bus-refresh”（在springboot1.x版本是...:8888/bus/refresh）
     * 第一阶段是在所有client添加bus，server不加，然后只需要刷新一个client，所有client就都可以动态刷新
     * 第二阶段是在server端和client端都添加bus，然后只需要刷新server端，所有的client端就会刷新
     * 定点刷新：curl -X POST “http://localhost:8888/actuator/bus-refresh/cloud-one-config-client:8006”
     * 刷新请求不再发送到具体的服务实例上，而是发给config server 。并通过destination参数类指定需要发送更新配置的服务或者实例
     */
    @GetMapping("/test")
    public String test()
    {
        System.out.println("version:"+version);
        return version;
    }
}
