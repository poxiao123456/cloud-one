package com.poxiao.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.poxiao.cloud.entity.Payment;
import com.poxiao.cloud.feign.PaymentService;
import com.poxiao.cloud.handler.CustomerBlockHandler;
import com.poxiao.cloud.util.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author qq
 * @date 2021/1/15
 */
@RestController
@Slf4j
public class FlowLimitController {


    /**
     * 测试流控规则
     */
    //测试流控规则
    //测试testA和testB在sentinel dashbord中的QPS展示

    @GetMapping("/testA")
    public String testA()
    {
        log.info(Thread.currentThread().getName()+"\t"+"...testA");
        int i = 10/0;
        return "------testA";
    }

    @GetMapping("/testB")
    public String testB()
    {
        log.info(Thread.currentThread().getName()+"\t"+"...testB");
        return "------testB";
    }


    //QPS 1
    //1秒钟点击1下，OK
    //为了验证流控规则是临时的，服务下线流控规则就消失


    @GetMapping("/byResource")
    @SentinelResource(value = "byResource",blockHandler = "handleException")
    public CommonResult byResource()
    {
        return new CommonResult(200,"按资源名称限流测试OK",new Payment(2020L,"serial001"));
    }
    public CommonResult handleException(BlockException exception)
    {
        return new CommonResult(444,exception.getClass().getCanonicalName()+"\t 服务不可用");
    }

    //通过访问的URL来限流，会返回Sentinel自带默认的限流处理信息

    @GetMapping("/retaLimit/byUrl")
    public String byUrl()
    {
        return "按url限流测试OK";
    }

    @GetMapping("/rateLimit/customerBlockHandler")
    @SentinelResource(value = "customerBlockHandler",
            blockHandlerClass = CustomerBlockHandler.class,
            blockHandler = "handlerException")
    public CommonResult customerBlockHandler()
    {
        return new CommonResult(200,"按客戶自定义",new Payment(2020L,"serial003"));
    }


    /**
     * 测试降级
     */
    //测试降级RT策略
    //200ms 1s
    //条件是QPS>=5，平均RT

    @GetMapping("/testC")
    public String testC()
    {
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        log.info("testC 测试RT");

        return "------testC";
    }

    //测试降级异常比率策略
    //0.2 1s
    //条件是QPS>=5，异常比率>0.2
    //不再报错，而是降级

    @GetMapping("/testD")
    public String testD()
    {

        log.info("testD 测试异常比率");
        int age = 10/0;
        return "------testD";
    }

    //测试降级异常数策略
    //条件是1分钟的异常数超过阈值
    //时间窗口是分钟级别的，要大于1分钟

    @GetMapping("/testE")
    public String testE()
    {
        log.info("testE 测试异常数");
        int age = 10/0;
        return "------testE 测试异常数";
    }


    /**
     * 测试热点参数
     */
    //热点参数限流
    //热点参数必须是基本类型或者String


    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey",blockHandler = "deal_testHotKey")
    public String testHotKey(@RequestParam(value = "p1",required = false) String p1,
                             @RequestParam(value = "p2",required = false) String p2) {
        //int age = 10/0; //对于RunTimeException，SentinelResource是不管的，SentinelResource主管配置出错。
        return "------testHotKey";
    }

    //兜底方法
    public String deal_testHotKey (String p1, String p2, BlockException exception){
        return "------deal_testHotKey,o(╥﹏╥)o";
    }

    /**
     * 测试系统规则
     */
    //系统规则
    //针对一个应用
    //保证系统在最大吞吐量的稳定性

    //load=CPU核数*2.5
    //cpu usage
    //平均RT：所有入口流量的平均RT达到阈值触发
    //并发线程数
    //入口RPS


    /**
     * sentinel整合ribbon+openFeign+fallback
     */

    public static final String SERVICE_URL = "http://nacos-payment-provider";

    @Resource
    private RestTemplate restTemplate;



    @RequestMapping("/consumer/fallback/{id}")
    //@SentinelResource(value = "fallback") //没有配置
    //@SentinelResource(value = "fallback",fallback = "handlerFallback") //fallback只负责业务异常
    //@SentinelResource(value = "fallback",blockHandler = "blockHandler") //blockHandler只负责sentinel控制台配置违规
    @SentinelResource(value = "fallback",fallback = "handlerFallback",blockHandler = "blockHandler",
            exceptionsToIgnore = {IllegalArgumentException.class})
    public CommonResult<Payment> fallback(@PathVariable Long id) {
        CommonResult<Payment> result = restTemplate.getForObject(SERVICE_URL + "/paymentSQL/"+id, CommonResult.class,id);

        if (id == 4) {
            throw new IllegalArgumentException ("IllegalArgumentException,非法参数异常....");
        }else if (result.getData() == null) {
            throw new NullPointerException ("NullPointerException,该ID没有对应记录,空指针异常");
        }

        return result;
    }

    //fallback
    public CommonResult handlerFallback(@PathVariable  Long id,Throwable e) {
        Payment payment = new Payment(id,"null");
        return new CommonResult<>(444,"兜底异常handlerFallback,exception内容  "+e.getMessage(),payment);
    }

    //blockHandler
    public CommonResult blockHandler(@PathVariable  Long id,BlockException blockException) {
        Payment payment = new Payment(id,"null");
        return new CommonResult<>(445,"blockHandler-sentinel限流,无此流水: blockException  "+blockException.getMessage(),payment);
    }

    //openfeign
    @Resource
    private PaymentService paymentService;

    @GetMapping(value = "/consumer/paymentSQL/{id}")
    public CommonResult<Payment> paymentSQL(@PathVariable("id") Long id) {
        return paymentService.paymentSQL(id);
    }



    /**
     * 规则持久化
     */

    //在nacos添加配置
    /**
     * [
     *     {
     *          "resource": "/retaLimit/byUrl",
     *          "limitApp": "default",
     *          "grade":   1,
     *          "count":   1,
     *          "strategy": 0,
     *          "controlBehavior": 0,
     *          "clusterMode": false
     *     }
     * ]
     */


    /**
     * 授权规则
     */

    //给某个资源指定调用方（可以有多个）
    //可以加在白名单或者黑名单

}
