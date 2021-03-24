package com.poxiao.cloud.feign.fallback;

import com.poxiao.cloud.entity.Payment;
import com.poxiao.cloud.feign.PaymentService;
import com.poxiao.cloud.util.CommonResult;
import org.springframework.stereotype.Component;

/**
 * @author qq
 * @date 2021/1/19
 */
@Component
public class PaymentFallbackService implements PaymentService {

    @Override
    public CommonResult<Payment> paymentSQL(Long id)
    {
        return new CommonResult<>(44444,"服务降级返回,---PaymentFallbackService",new Payment(id,"errorSerial"));
    }
}
