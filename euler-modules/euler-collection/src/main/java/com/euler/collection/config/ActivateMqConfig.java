package com.euler.collection.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "webconfig.mq")
public class ActivateMqConfig {

    /**
     * 激活的交换机
     */
    private String activateExchange;

    /**
     * 激活的routingkey
     */
    private String activateRoutingKey;

    /**
     * 激活的队列名称
     */
    private String activateQueue;

}
