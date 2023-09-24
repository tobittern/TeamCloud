package com.euler.community.config;

import com.euler.common.rabbitmq.RabbitMqHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GiftRabbitMqProducer {

    @Autowired
    private RabbitMqHelper rabbitMqHelper;
    @Autowired
    private RabbitMqConfig rabbitMqConfig;


    /**
     * 礼包领取
     */
    public void giftPick(Object msg, String msgId) {
        rabbitMqHelper.sendObj(rabbitMqConfig.getBagPickExchange(), rabbitMqConfig.getBagPickRoutingKey(), msgId, msg);
    }
}
