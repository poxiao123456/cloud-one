package com.poxiao.cloud.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;

/**
 * @author qinqi
 * @date 2020/6/2
 */
@Component
@Slf4j
public class HystrixFallbackHandler implements HandlerFunction<ServerResponse> {
    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        Optional<Object> attribute = serverRequest.attribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        System.out.println("attributes:"+attribute);
        attribute.ifPresent(attribute1 -> log.error("网关执行请求:{}失败,hystrix服务降级处理",attribute1));
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromObject("服务异常"));
    }
}
