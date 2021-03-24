package com.poxiao.cloud.feign;

import com.poxiao.cloud.config.FeignConfig;
import com.poxiao.cloud.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author qinqi
 * @date 2020/6/1
 */
@FeignClient(value = "cloud-one-user"
        ,fallback = RemoteUserService.RemoteUserServiceFallback.class
        ,configuration = FeignConfig.class)
public interface RemoteUserService {

    @RequestMapping(value = "user/selectUserById/{id}",method = RequestMethod.GET,produces = "application/json")
    User selectUserById(@PathVariable("id") Integer id);

    @GetMapping(value = "/user/feign/timeout")
    String paymentFeignTimeout();

    @Component
    class RemoteUserServiceFallback implements RemoteUserService {

        @Override
        public User selectUserById(Integer id) {
            return null;
        }

        @Override
        public String paymentFeignTimeout() {
            return "openFeign fallback timeout";
        }
    }
}
