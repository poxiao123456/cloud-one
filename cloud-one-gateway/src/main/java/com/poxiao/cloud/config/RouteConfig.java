package com.poxiao.cloud.config;

import com.poxiao.cloud.handler.HystrixFallbackHandler;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import javax.annotation.Resource;

/**
 * @author qinqi
 * @date 2020/6/2
 * 自定义路由规则
 */
@Configuration
public class RouteConfig {

    @Resource
    private HystrixFallbackHandler hystrixFallbackHandler;

    /**
     * 过滤器工厂
     * 网关经常需要对路由请求进行过滤，进行一些操作，
     * 如鉴权之后构造头部之类的，过滤的种类很多，
     * 如增加请求头、增加请求参数、增加响应头和断路器等等功能。
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes().route(
            r -> r.path("/auth")
                .filters(f -> f.addResponseHeader("token","123456"))
                .uri("http://localhost:9527/user-service/user/selectUserById/1")
        ).build();

        /**
         * RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
         * routes.route("path_route_atguigu",r -> r.path("/guoji").uri("https://news.baidu.com/guoji"))
         *       .route("path_route_atguigu1",r -> r.path("/guonei").uri("https://news.baidu.com/guonei"))
         *       .build();
         * return routes.build();
         */
    }

    /**
     * 路由断言工厂
     * 路由断言工厂有多种类型，根据请求的时间、host、路径、方法等等
     * 如下定义的是一个基于路径的路由断言匹配。
     */
    @Bean
    public RouterFunction<?> customRouterFunction() {
        return RouterFunctions.route(RequestPredicates.path("/test"), request -> ServerResponse.ok().body(BodyInserters.fromObject("hello")))
                .andRoute(RequestPredicates.path("/fallback").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),hystrixFallbackHandler);
    }


    /**
     * predicate
     *         Path=/test,
     *         Before=2020-04-15T18:23:00.156+08:00[Asia/Shanghai]
     *         After=2020-04-15T18:23:00.156+08:00[Asia/Shanghai]
     *         Between=2020-04-15T18:23:00.156+08:00[Asia/Shanghai],2020-04-15T18:23:00.156+08:00[Asia/Shanghai]
     *         (时间获取方法：ZonedDateTime now = ZonedDateTime.now();)
     *         Cookie=username,zhangsan
     *         Header=X-Request-Id, \d+（请求头需要有X-Request-Id属性名,值为整数正则表达式）
     *         Host=**.baidu.com
     *         Method=GET
     *         Query=username
     *         RemoteAddr=192.168.1.1/24
     *         Weight=group1, 2 #第一个路由20%权重
     *         Weight=group1, 8 #第二个路由80%权重
     */

    /**
     * filter
     * 对断言后的请求和响应进行过滤
     *
     * 生命周期
     *      pre
     *      post
     *
     * 种类
     *      GatewayFilter
     *          filters:
     *              - AddRequestParameter=X-Request-Id,1024
     *              - AddRequestHeader=X-Request-red, blue
     *              - AddResponseHeader=X-Response-Red, Blue
     *              - DedupeResponseHeader=Access-Control-Allow-Credentials Access-Control-Allow-Origin #重复响应头删除
     *              - name: Hystrix
     *                args:
     *                 name: fallbackcmd
     *                 fallbackUri: 'forward:/fallback'
     *      GlobalFilter
     *          统一网关鉴权
     *          全局日志
     */
}
