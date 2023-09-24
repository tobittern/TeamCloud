package com.euler.risk.mq;

import com.euler.common.core.utils.JsonHelper;
import com.euler.risk.config.WebConfig;
import com.euler.risk.domain.dto.BehaviorMqMsgDto;
import com.euler.risk.service.IBehaviorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BehaviorConsumer {

    @Autowired
    private WebConfig webConfig;
    @Autowired
    private IBehaviorService behaviorService;


    @RabbitListener(queues = "${webconfig.behaviorQueue}")
    public void storeBehaviorData(String msg, Message messages) {
        log.info("简单消费[用户行为数据入库]，queue：{}: msg：{}", webConfig.getBehaviorQueue(), msg);
        BehaviorMqMsgDto behaviorMqMsgDto = JsonHelper.toObject(msg, BehaviorMqMsgDto.class);

        if (behaviorMqMsgDto != null) {
            behaviorService.save(behaviorMqMsgDto);
        }

    }

}
