package com.euler.community.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "webconfig.rabbitmq")
public class RabbitMqConfig {
    // 交换机的地址
    private String addDynamicExchange;
    // 数据审核之后进入到Es的Routingkey
    private String addDynamicRoutingkey;
    // 数据审核之后进入到Es的Queue
    private String addDynamicIntoEsQueue;

    private  String operationDynamicExchange;

    // 数据操作之后进入到EsRoutingkey
    private String operationDynamicRoutingkey;
    // 数据操作之后进入到Es的Queue
    private String operationDynamicEsQueue;

    /**
     * 礼包领取的交换机
     */
    private String bagPickExchange;

    /**
     * 礼包领取的routingkey
     */
    private String bagPickRoutingKey;

    /**
     * 礼包领取的队列名称
     */
    private String bagPickQueue;




}
