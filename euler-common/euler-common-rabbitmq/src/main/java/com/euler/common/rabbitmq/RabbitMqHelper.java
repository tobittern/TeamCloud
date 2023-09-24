package com.euler.common.rabbitmq;



import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.euler.common.core.utils.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
public class RabbitMqHelper implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    private static final AtomicLong MSG_ID_SEQ = new AtomicLong(0L);

    public  String createMsgId(String idType) {
        return String.format("%s%s%04d", idType,
            DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN),
            (int) MSG_ID_SEQ.getAndIncrement() % 10000);

    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        // rabbitTemplate.setReturnCallback(this);
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("{},发送失败,exchange，{}，routingkey：{}，replayCode：{}，replayText:{}", message.getMessageProperties().getCorrelationId(), exchange, routingKey, replyCode, replyText);

    }

    /**
     * 发送消息，不需要实现任何接口，供外部调用
     * @param exchange
     * @param routingkey
     * @param msg
     */
    public void send(String exchange, String routingkey, String msg) {
        rabbitTemplate.convertAndSend(exchange, routingkey, msg);
    }

    /**
     * 发送消息，添加msgId，方便排查问题
     * 一般情况用这个
     *
     * @param exchange
     * @param routingkey
     * @param msgId
     * @param msg
     */
    public void sendObj(String exchange, String routingkey, String msgId, Object msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(msgId).append("_").append(DateUtil.current());
        CorrelationData correlationId = new CorrelationData(sb.toString());

        rabbitTemplate.convertAndSend(exchange, routingkey,  JsonHelper.toJson(msg), correlationId);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack)
            log.info("消息发送成功：{}", correlationData != null ? correlationData.getId() : "未知id");
        else
            log.error("消息发送失败：{}，原因：{}", correlationData != null ? correlationData.getId() : "未知id", cause);

    }

    private String getMsgId(Message message) {
        if (message == null || message.getMessageProperties() == null
            || message.getMessageProperties().getHeaders() == null
            || message.getMessageProperties().getHeaders().get("spring_returned_message_correlation") == null) {
            return "未知msgId";
        } else {
            return message.getMessageProperties().getHeaders().get("spring_returned_message_correlation").toString();
        }

    }
}
