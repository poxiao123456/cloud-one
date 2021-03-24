package com.poxiao.cloud.spring_cache.controller;

import com.poxiao.cloud.spring_cache.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author qq
 * @date 2021/1/29
 */
//@RestController
public class TestController {

    @Autowired
    private CacheManager cacheManager;

    /**
     * 请求结束才缓存到spring中
     * 如果出现异常，不会进行缓存
     * 如果已经缓存，不会进入方法中，而是访问代理
     */
    @Cacheable(cacheNames = "studentCache", key = "#id")
    @GetMapping("/get/one")
    public Student getOneInfo(@RequestParam Integer id){
        Student student=null;
        try {
            int i=1/0;
            Date date=new Date();
            System.out.println("进行了查询---》"+ date.getTime());
            student= new Student(id,"zhagsan");
        } catch (Exception e) {
            throw e;
        }
        return student;
    }

    @GetMapping("/get/cache")
    public String getCache(@RequestParam Integer id){
        Cache demoCache = cacheManager.getCache("studentCache");
        System.out.println("cache name:"+demoCache.getName());
        System.out.println("cache result:"+demoCache.get(id, Student.class));
        return demoCache.getName();
    }

}
