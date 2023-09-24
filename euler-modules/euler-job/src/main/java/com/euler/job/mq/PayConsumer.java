package com.euler.job.mq;

import cn.hutool.core.convert.Convert;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;

@Component
@Slf4j
public class PayConsumer {

    private String getMsgId(Message message) {
        if (message == null || message.getMessageProperties() == null
            || message.getMessageProperties().getHeaders() == null
            || message.getMessageProperties().getHeaders().get("spring_returned_message_correlation") == null) {
            return "未知msgId";
        } else {
            return message.getMessageProperties().getHeaders().get("spring_returned_message_correlation").toString();
        }
    }

    @RabbitListener(queues = "${webconfig.mq.payQueue}")
    public void payFailQueue(String msg, Message messages) {
        // 自定义排查的msgId
        String msgId = getMsgId(messages);
        log.info("简单消费，msgid：{}，msg：{}", msgId, msg);
        HashMap hashMap = JsonUtils.parseObject(msg, HashMap.class);
        if(hashMap != null) {
            Long memberId = hashMap.get("member_id") == null ? null : Convert.toLong(hashMap.get("member_id"));
            Long goodId = hashMap.get("good_id") == null ? null : Convert.toLong(hashMap.get("good_id"));
            String goodName = hashMap.get("good_name") == null ? null : Convert.toStr(hashMap.get("good_name"));
            BigDecimal price = hashMap.get("price") == null ? null : Convert.toBigDecimal(hashMap.get("price"));
            String status = hashMap.get("status") == null ? null : Convert.toStr(hashMap.get("status"));
            if(memberId != null && StringUtils.equals("fail", status)) {
                log.info("支付失败---消息消费成功");
            } else {
                log.info("数据异常，不做处理,msg:{},memberId: {}, goodId: {}, goodName: {}, price: {}, status: {}", msg, memberId, goodId, goodName, price, status);
            }
        } else {
            log.error("支付失败---消费失败，msg：{}", msg);
        }
    }

}
