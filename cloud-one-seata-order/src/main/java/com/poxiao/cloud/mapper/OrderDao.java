package com.poxiao.cloud.mapper;

import com.poxiao.cloud.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author qq
 * @date 2021/1/18
 */
@Mapper
public interface OrderDao {

    //创建订单
    void create(Order order);

    //修改订单状态
    void update(@Param("userId") Long userId, @Param("status") Integer status);
}
