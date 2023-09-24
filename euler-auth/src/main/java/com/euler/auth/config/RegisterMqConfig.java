package com.euler.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "webconfig.mq")
public class RegisterMqConfig {

    /**
     * 注册的交换机
     */
    private String registerExchange;

    /**
     * 注册的routingkey
     */
    private String registerRoutingKey;

    /**
     * 注册的队列名称
     */
    private String registerQueue;

}
