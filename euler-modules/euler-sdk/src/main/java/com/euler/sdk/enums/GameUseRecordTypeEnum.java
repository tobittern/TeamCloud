package com.euler.sdk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 游戏关联类型
 */
@Getter
@AllArgsConstructor
public enum GameUseRecordTypeEnum {
    GRADE_GIFTG(1, "等级礼包"),
    ACTIVITY_GIFT_BAG(2, "活动礼包"),
    ACTIVITY_CREATION(3, "活动创建"),
    CHANNEL_ASSOCIATION(4, "渠道关联"),
    ;
    private Integer code;
    private String desc;

    public static GameUseRecordTypeEnum find(String code) {
        for (GameUseRecordTypeEnum enumd : values()) {
            if (enumd.getCode().equals(code)) {
                return enumd;
            }
        }
        throw new RuntimeException("'GameUseRecordTypeEnum' not found By " + code);
    }
}
