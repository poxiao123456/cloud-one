package com.poxiao.cloud.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author qq
 * @date 2021/1/18
 */
@Configuration
@MapperScan({"com.poxiao.cloud.mapper"})
public class MybatisConfig {
}
