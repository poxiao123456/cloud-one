package com.poxiao.cloud.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <p>
 * user表 服务类
 * </p>
 *
 * @author zhangsan
 * @since 2020-04-23
 */
@Service
public class UserService{

    @Value("${username.name}")
    private String name;
    private String name2 = name + "lisi";

    public String selectName() {
        System.out.println("name:"+name2);
        return name2;
    }
}
