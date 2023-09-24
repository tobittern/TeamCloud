package com.euler.community.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息类型
 */
@Slf4j
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {

    /**
     * 消息类型：1 点赞消息
     */
    PRAISE(1, "PRAISE"),

    /**
     * 消息类型：2 评论消息
     */
    DYNAMIC_COMMENT(2, "DYNAMIC_COMMENT"),

    /**
     * 消息类型：3 新粉丝消息
     */
    ATTENTION(3, "ATTENTION"),

    /**
     * 消息类型：4 系统消息
     */
    SYSTEM(4, "SYSTEM"),

;

    private Integer status;
    private String code;

    public static MessageTypeEnum find(String value) {
        for (MessageTypeEnum enumd : values()) {
            if (enumd.getCode().equals(value)) {
                return enumd;
            }
        }
        log.info("MessageTypeEnum not found By ：{}",value);
        return null;
    }

}
