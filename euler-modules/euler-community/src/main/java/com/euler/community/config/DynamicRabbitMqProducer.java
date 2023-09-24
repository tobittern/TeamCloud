package com.euler.community.config;

import com.euler.common.rabbitmq.RabbitMqHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DynamicRabbitMqProducer {

    @Autowired
    private RabbitMqHelper rabbitMqHelper;
    @Autowired
    private RabbitMqConfig rabbitMqConfig;

    /**
     * 发送动态
     */
    public void dynamiInsertIntoEs(Object msg, String msgId) {
        rabbitMqHelper.sendObj(rabbitMqConfig.getAddDynamicExchange(), rabbitMqConfig.getAddDynamicRoutingkey(), msgId, msg);
    }

    /**
     * 修改动态
     */
    public void operationDynamic(Object msg, String msgId) {
        rabbitMqHelper.sendObj(rabbitMqConfig.getOperationDynamicExchange(), rabbitMqConfig.getOperationDynamicRoutingkey(), msgId, msg);
    }

}
