package com.euler.risk.domain.dto;

import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.risk.api.domain.BehaviorType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 发送mq消息
 */
@Accessors(chain = true)
@Data
public class BehaviorMqMsgDto {

    /**
     * 消息id
     */
    private String msgId;
    /**
     * 请求行为
     */
    private BehaviorType behaviorType;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户id地址
     */
    private String ip;
    /**
     * 请求头消息
     */
    private RequestHeaderDto requestHeader;
    /**
     * 请求数据
     */
    private String requestData;
}
