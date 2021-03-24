package com.poxiao.cloud.feign;

import com.poxiao.cloud.entity.Payment;
import com.poxiao.cloud.feign.fallback.PaymentFallbackService;
import com.poxiao.cloud.util.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author qq
 * @date 2021/1/19
 */
@FeignClient(value = "nacos-payment-provider",fallback = PaymentFallbackService.class)
public interface PaymentService {

    @GetMapping(value = "/paymentSQL/{id}")
    public CommonResult<Payment> paymentSQL(@PathVariable("id") Long id);
}
