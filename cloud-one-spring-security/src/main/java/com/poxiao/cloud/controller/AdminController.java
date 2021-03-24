package com.poxiao.cloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qq
 * @date 2021/1/27
 * 测试yml的user属性登录
 */

@RestController
@RequestMapping("admin")
public class AdminController {

    @GetMapping("demo")
    public String demo() {
        return "示例返回";
    }
}
