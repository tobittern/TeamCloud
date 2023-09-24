package com.euler.collection.mq;

import cn.hutool.core.convert.Convert;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Slf4j
public class ActivateConsumer {

    private String getMsgId(Message message) {
        if (message == null || message.getMessageProperties() == null
            || message.getMessageProperties().getHeaders() == null
            || message.getMessageProperties().getHeaders().get("spring_returned_message_correlation") == null) {
            return "未知msgId";
        } else {
            return message.getMessageProperties().getHeaders().get("spring_returned_message_correlation").toString();
        }
    }

    @RabbitListener(queues = "${webconfig.mq.activateQueue}")
    public void registerSuccessQueue(String msg, Message messages) {
        // 自定义排查的msgId
        String msgId = getMsgId(messages);
        log.info("简单消费，msgid：{}，msg：{}", msgId, msg);
        HashMap hashMap = JsonUtils.parseObject(msg, HashMap.class);
        if(hashMap != null) {
            Long memberId = hashMap.get("member_id") == null ? null : Convert.toLong(hashMap.get("member_id"));
            String status = hashMap.get("status") == null ? null : Convert.toStr(hashMap.get("status"));
            if(memberId != null && StringUtils.equals("success", status)) {
                log.info("激活---消息消费成功");
            } else {
                log.info("数据异常，不做处理,msg:{},memberId: {}, status: {}", msg, memberId, status);
            }
        } else {
            log.error("激活---消费失败，msg：{}", msg);
        }
    }

}
