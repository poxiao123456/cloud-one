package com.poxiao.cloud.service;

/**
 * @author qq
 * @date 2021/1/18
 */
public interface StorageService {

    // 扣减库存
    void decrease(Long productId, Integer count);
}
