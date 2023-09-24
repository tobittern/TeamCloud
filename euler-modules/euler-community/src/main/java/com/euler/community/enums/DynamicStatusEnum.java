package com.euler.community.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 游戏关联类型
 */
@Getter
@AllArgsConstructor
public enum DynamicStatusEnum {
    FIRST_DYNAMIC(0, "初稿"),
    AUDITING(10, "审核中"),
    WAIT_RELEASE(20, "待发布"),
    RELEASE(30, "已发布"),
    DOWN(40, "已下线"),
    AGAIN_EDIT(50, "重新编辑"),
    PEOPLE_FAIL(60, "人工未通过"),
    AI_FAIL(70, "AI未通过"),
    ;
    private Integer code;
    private String desc;

    public static DynamicStatusEnum find(Integer code) {
        for (DynamicStatusEnum enumd : values()) {
            if (enumd.getCode().equals(code)) {
                return enumd;
            }
        }
        throw new RuntimeException("'DynamicStatusEnum' not found By " + code);
    }
}
