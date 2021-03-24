package com.poxiao.cloud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author qq
 * @date 2021/1/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    //代理id
    private Long id;

    private Long userId;

    //物品id
    private Long productId;

    private Integer count;

    private BigDecimal money;

    private Integer status; //订单状态：0：创建中；1：已完结
}
