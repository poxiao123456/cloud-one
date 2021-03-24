package com.poxiao.cloud.controller;

import com.poxiao.cloud.entity.Order;
import com.poxiao.cloud.service.OrderService;
import com.poxiao.cloud.util.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author qq
 * @date 2021/1/18
 */
@RestController
public class OrderController {

    @Resource
    private OrderService orderService;


    @PostMapping("/order/create")
    public CommonResult create(@RequestBody Order order)
    {
        System.out.println("order:"+order);
        orderService.create(order);
        return new CommonResult(200,"订单创建成功");
    }
}
