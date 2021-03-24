package com.poxiao.cloud.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;

/**
 * @author qq
 * @date 2021/1/27
 * 测试Spring Security的注解对url的权限控制
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    //因为在配置类中配置了 .anyRequest().authenticated() ，任何请求，访问的用户都需要经过认证。
    // 所以这里 @PermitAll 注解实际是不生效的
    //也就是说，Java Config 配置的权限，和注解配置的权限，两者是叠加的。有先后顺序，配置文件在前，注解在后
    //先走完配置文件中的配置，然后再进行注解的配置

    @PermitAll
    @GetMapping("/echo")
    public String demo() {
        return "示例返回";
    }

    @GetMapping("/home")
    public String home() {
        return "我是首页";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "我是管理员";
    }


    //等价于 #access(String attribute) 方法

    @PreAuthorize("hasRole('ROLE_NORMAL')")
    @GetMapping("/normal")
    public String normal() {
        return "我是普通用户";
    }

}
