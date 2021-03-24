package com.poxiao.cloud.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.netflix.discovery.DiscoveryManager;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.poxiao.cloud.mapper.UserMapper;
import com.poxiao.cloud.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author qinqi
 * @date 2020/5/29
 */
@RestController
@RequestMapping("user")
//@DefaultProperties(defaultFallback="paymentTimeOutFallbackMethod_Global") //hystrix全局异常
public class UserController {

    @Resource
    private UserMapper userMapper;
    @Autowired
    private DiscoveryClient discoveryClient;


    /**
     * 基本CURD
     */

    @RequestMapping(value = "insertUser",method = RequestMethod.POST,produces = "application/json")
    public int addUser(@Valid @RequestBody User user) {
        int insert = userMapper.insert(user);
        System.out.println("insert:"+insert);
        return insert;
    }

    @RequestMapping(value = "deleteUserById/{id}",method = RequestMethod.GET,produces = "application/json")
    public int deleteUserById(@PathVariable Integer id) {
        int delete = userMapper.deleteById(id);
        System.out.println("delete:"+delete);
        return delete;
    }

    @RequestMapping(value = "updateUserById",method = RequestMethod.POST,produces = "application/json")
    public int updateUserById(@Valid @RequestBody User user) {
        int update = userMapper.updateById(user);
        System.out.println("update:"+update);
        return update;
    }

    @RequestMapping(value = "selectUserList",method = RequestMethod.GET,produces = "application/json")
    public List<User> selectUserList() {
        List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>()
                .eq(User::getDeleteStatus,"0"));
        System.out.println("users:"+users);
        return users;
    }


    /**
     * openFeign超时
     */

    @GetMapping(value = "feign/timeout")
    public String paymentFeignTimeout() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "openFeign";
    }


    /**
     * hystrix降级（fallback）和熔断（break）和服务限流（flowlimit）
     * 降级：超时，熔断，线程池/信号量满了
     * 熔断：一定时间达到异常比（默认师10秒内20次调用失败）
     * 限流：排队，一秒钟N个,有序进行（alibaba的sentinel）
     */

    //争对业务异常，fallback有效，发生降级

    @RequestMapping(value = "selectUserById/{id}",method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "processHystrixSelectUserById")
    public User selectUserById(@PathVariable Integer id) {
        User user = userMapper.selectById(id);
        if (user == null)
        {
            throw new RuntimeException("找不到用户");
        }
        System.out.println("user:"+user);
        return user;
    }

    public User processHystrixSelectUserById(@PathVariable Integer id)
    {
        System.out.println("发生业务异常，服务降级");
        return new User().setId(id).setUsername("该ID：" + id + " 没有对应的信息,null");
    }


    //争对超时配置，fallback有效，发生降级

    //,commandProperties = {
    //            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value = "7000")
    //    }

    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler")
    @GetMapping(value = "hystrix/timeout")
    public String paymentInfo_TimeOut(Integer id) {
        int timeNumber = 5;
        try {
            // 暂停3秒钟
            TimeUnit.SECONDS.sleep(timeNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池:" + Thread.currentThread().getName() + " paymentInfo_TimeOut,id:" + id + "\t" +
                "O(∩_∩)O哈哈~  耗时(秒)" + timeNumber;
    }

    public String  paymentInfo_TimeOutHandler(Integer id){
        return "程序运行繁忙或报错,请稍后再试*****"+"当前线程: "+Thread.currentThread().getName()+id+"\t "+"orz!";
    }


    /**
     * hystrix全局异常
     */

    public  String  paymentTimeOutFallbackMethod_Global(){
        return "我是全局异常"+"当前线程: "+Thread.currentThread().getName()+"\t "+"mgd!";

    }


    /**
     * 测试服务熔断
     * 默认服务熔断请求时间是10秒
     */

    @HystrixCommand(fallbackMethod="paymentCircuitBreakerFallback", commandProperties={
            @HystrixProperty(name = "circuitBreaker.enabled" ,value = "true"),//是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),// 请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),//时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60")//失败率达到多少后跳闸
    })
    @GetMapping("/hystrix/circuit/{id}")
    public String paymentCircuitBreaker(@PathVariable("id") Integer id){
        if (id<0){
            throw new RuntimeException();
        }
        String serialNumber = "xxxx11110000";
        return Thread.currentThread().getName()+"\t "+"调用成功,流水号: "+serialNumber;
    }
    public String paymentCircuitBreakerFallback(@PathVariable("id") Integer id){
        return "id不能为负数,请稍后再试~ id: "+ id;
    }

    /**
     * 获取注册中心的某个服务的所有实例
     */

    @RequestMapping(value = "discovery",method = RequestMethod.GET,produces = "application/json")
    public Object discovery() {
        List<String> services = discoveryClient.getServices();
        services.forEach(System.out::println);
        List<ServiceInstance> instances = discoveryClient.getInstances("cloud-one-user");
        instances.forEach(System.out::println);
        return discoveryClient;
    }

    /**
     * 服务被eureka剔除
     * 发送一个delete 请求
     * curl -X DELETE http://你的注册中心/apps/你的实例名称/你的实例地址加端口
     * 但是如果服务不停止的话，在30s后会主动发送心跳给eureka，然后继续存在
     *
     * 下面这个方法调用后，也要手动定掉服务。
     */
    @RequestMapping(value = "/offline", method = RequestMethod.GET)
    public void offLine(){
        //主动下线
        DiscoveryManager.getInstance().shutdownComponent();
    }
}
