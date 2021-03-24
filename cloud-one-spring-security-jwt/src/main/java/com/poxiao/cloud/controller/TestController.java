package com.poxiao.cloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qq
 * @date 2021/1/28
 */
@RestController
@RequestMapping("/common")
public class TestController {

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/inner")
    public String inner(){
        return "inner";
    }
}
