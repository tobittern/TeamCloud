package com.euler.gateway.filter;

import cn.dev33.satoken.id.SaIdUtil;
import com.euler.gateway.config.WebConfig;
import com.euler.gateway.utils.RequestHeaderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

/**
 * 转发认证过滤器(内部服务外网隔离)
 *
 * @author euler
 */
@Component
public class ForwardAuthFilter implements GlobalFilter {

    @Autowired
    private WebConfig webConfig;

    @Autowired
    private RequestHeaderUtils requestHeaderUtils;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        Consumer<HttpHeaders> headersConsumer = httpHeaders -> {
            httpHeaders.add(SaIdUtil.ID_TOKEN, SaIdUtil.getToken());
        };

        ServerHttpRequest newRequest = exchange
            .getRequest()
            .mutate()
            // 为请求追加 Id-Token 参数
            .headers(headersConsumer)
            .build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();


        return chain.filter(newExchange);
    }


}

