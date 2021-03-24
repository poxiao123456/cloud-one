package com.poxiao.myrule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qq
 * @date 2021/1/20
 * 这个包和包下的类不能放在@ComponentScan所扫描的包以及子包下，否则这个配置类就是所有ribbon共享了，达不到特殊化定制的目的
 */
@Configuration
public class MySelfRule {

    @Bean
    public IRule myRule() {

        // 定义为随机
        //ribbon负载均衡规则有轮询，随机，重试（先按照轮询获取，如果获取失败则在指定时间重试获取），
        // 按照响应时间的权重，按照并发量小的，按照服务器所在区域
        return new RandomRule();
    }
}
