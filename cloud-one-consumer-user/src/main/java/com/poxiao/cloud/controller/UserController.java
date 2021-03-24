package com.poxiao.cloud.controller;

import com.poxiao.cloud.feign.RemoteUserService;
import com.poxiao.cloud.model.User;
import com.poxiao.cloud.ribbon.LoadBalancer;
import com.poxiao.cloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

/**
 * @author qinqi
 * @date 2020/6/1
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("restTemplate2")
    private RestTemplate restTemplate2;

    private static final String REST_URL_PREFIX = "http://CLOUD-ONE-USER";

    @Resource
    private RemoteUserService remoteUserService;

    @Resource
    private UserService userService;

    /**
     *使用ribbon进行访问
     */
    @RequestMapping(value = "selectUserById/{id}",method = RequestMethod.GET,produces = "application/json")
    public User selectUserById(@PathVariable Integer id) {
        User user = restTemplate.getForObject(REST_URL_PREFIX+"/user/selectUserById/" + id, User.class);
        System.out.println("user:"+user);
        return user;
    }

    /**
     *普通restTemplate访问
     */
    @RequestMapping(value = "selectUserById1/{id}",method = RequestMethod.GET,produces = "application/json")
    public User selectUserById1(@PathVariable Integer id) {
        System.out.println("restTemplate2:"+restTemplate2);
        System.out.println("restTemplate:"+restTemplate);
        User user = restTemplate2.getForObject("http://localhost:8001/user/selectUserById/" + id, User.class);
        System.out.println("user:"+user);
        return user;
    }

    /**
     *使用openFeign正常访问
     */
    @RequestMapping(value = "selectUserById2/{id}",method = RequestMethod.GET,produces = "application/json")
    public User selectUserById2(@PathVariable Integer id) {
        System.out.println("remoteUserService:"+remoteUserService);
        User user = remoteUserService.selectUserById(id);
        System.out.println("user:"+user);
        return user;
    }

    /**
     * 使用openFeign超时测试
     * openFeign客户端默认只等待1S，这个默认时间来自ribbon的readTimeout
     * 所以一般openFeign的标准配置是：看配置文件
     */
    @RequestMapping(value = "feign/timeout")
    public String paymentFeignTimeout() {
        return remoteUserService.paymentFeignTimeout();
    }


    /**
     * 本地访问
     */
    @GetMapping("selectName")
    public String selectName() {
        return userService.selectName();
    }

    /**
     * 使用自定义负载均衡器去实现负载均衡
     */

    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private LoadBalancer loadBalancer;

    @GetMapping(value = "/consumer/payment/lb")
    public String getPaymentLB() {
        List<ServiceInstance> instances = discoveryClient.getInstances(REST_URL_PREFIX);
        if (CollectionUtils.isEmpty(instances)){
            return  null;
        }

        ServiceInstance serviceInstance = loadBalancer.instance(instances);
        URI uri = serviceInstance.getUri();
        return  restTemplate.getForObject(uri+"/payment/lb",String.class);
    }


}
