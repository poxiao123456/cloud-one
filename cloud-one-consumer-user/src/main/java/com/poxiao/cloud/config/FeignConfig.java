package com.poxiao.cloud.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qinqi
 * @date 2020/6/1
 */
@Configuration
public class FeignConfig {
    @Bean
    Logger.Level feignLoggerLevel() {
        // 请求和响应的头信息,请求和响应的正文及元数据
        /**
         * feign日志级别
         * none:默认的，不显示任何日志
         * basic:仅记录请求方法，url，响应码和执行时间
         * headers:除了记录请求方法，url，响应码和执行时间，还记录请求和响应的头信息
         * full:除了headers记录的内容，还包括请求和响应体以及元数据
         */
        return Logger.Level.BASIC;
    }
}
