package com.euler.job.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "webconfig.mq")
public class PayMqConfig {

    /**
     * 支付的交换机
     */
    private String payExchange;

    /**
     * 支付的routingkey
     */
    private String payRoutingKey;

    /**
     * 支付的队列名称
     */
    private String payQueue;

}
