package com.poxiao.cloud.spring_cache.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qq
 * @date 2021/1/29
 */
//@EnableCaching
//@Configuration
public class CacheConfig {

    @Bean
    public ConcurrentMapCacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        //cacheManager.setStoreByValue(true); //true表示缓存一份副本，否则缓存引用
        return cacheManager;
    }
}
