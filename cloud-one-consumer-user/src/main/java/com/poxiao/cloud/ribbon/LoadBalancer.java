package com.poxiao.cloud.ribbon;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * @author qq
 * @date 2021/1/20
 * 自己写一个本地负载均衡器用来代替ribbon
 */
public interface LoadBalancer {

    ServiceInstance  instance(List<ServiceInstance> serviceInstances);
}
