package com.euler.sdk.mq;

import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.LoginUser;
import com.euler.common.core.utils.JsonHelper;
import com.euler.sdk.domain.bo.GameUserManagementBo;
import com.euler.sdk.domain.dto.GameUserAddDto;
import com.euler.sdk.service.IGameUserManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GameUserConsumer {

    @Autowired
    private IGameUserManagementService gameUserManagementService;

    private String getMsgId(Message message) {
        String msgId = null;
        if (message == null || message.getMessageProperties() == null
            || message.getMessageProperties().getHeaders() == null
            || message.getMessageProperties().getHeaders().get("spring_returned_message_correlation") == null) {
            msgId = "未知msgId";
        } else {
            msgId = message.getMessageProperties().getHeaders().get("spring_returned_message_correlation").toString();
        }

        return msgId;
    }

    @RabbitListener(queues = "${webconfig.gameUserQueue}")
    public void addGameUserManagement(String msg, Message messages) {
        String msgId = getMsgId(messages);
        log.info("游戏上报消费：{}", msgId);
        GameUserAddDto gu = JsonHelper.toObject(msg, GameUserAddDto.class);
        if (gu != null && gu.getGameUserManagementBo() != null) {
            R r = gameUserManagementService.insertByBo(gu);
            if (!Constants.SUCCESS.equals(r.getCode())) {
                log.info("游戏角色上报失败,data:{},{}", msg, r.getMsg());
                return;
            }
            GameUserManagementBo gumBo = gu.getGameUserManagementBo();
            log.info("游戏上报成功：msgId:{},memberId:{},gameId:{},roleId:{},serverId:{},channelId:{}", msgId, gumBo.getMemberId(), gumBo.getGameId(), gumBo.getRoleId(), gumBo.getServerId(),gumBo.getChannelId());
        } else {
            log.error("游戏角色上报消费失败，信息不完善，msg：{}", msg);
        }

    }

}
