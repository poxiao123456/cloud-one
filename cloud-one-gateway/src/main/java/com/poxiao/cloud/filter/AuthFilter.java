package com.poxiao.cloud.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 * @author qinqi
 * @date 2020/6/2
 */
@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    // 排除过滤的 uri 地址
    // swagger排除自行添加
    private static final String[] whiteList = {"/auth/login"};

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        log.info("path:{}", path);
        //判断是否在白名单
        //在白名单，直接放掉
        if (Arrays.asList(whiteList).contains(path)) {
            return chain.filter(exchange);
        }
        //不在白名单，判断token
        String token = exchange.getRequest().getHeaders().getFirst("token");
        if (StringUtils.isBlank(token)) {
            return setUnAuthorizedResponse(exchange,"token can't null or empty string");
        }
        if (!"123456".equals(token)) {
            return setUnAuthorizedResponse(exchange,"token error");
        }
        //token没有问题
        //mutate:转换
        ServerHttpRequest mutableReq = exchange.getRequest().mutate().header("token", token).build();
        ServerWebExchange build = exchange.mutate().request(mutableReq).build();
        return chain.filter(build);
    }

    private Mono<Void> setUnAuthorizedResponse(ServerWebExchange exchange,String msg) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type","application/json;charset=UTF-8");
        byte[] bytes = msg.getBytes();
        DataBuffer wrap = response.bufferFactory().wrap(bytes);
        return response.writeWith(Flux.just(wrap));
    }
    @Override
    public int getOrder() {
        return -200;
    }
}
