package com.euler.community.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 动态操作类型
 */
@Getter
@AllArgsConstructor
public enum DynamicOperationEnum {
    AI_FAIL(1, "AI审核拒绝操作"),
    PEOPLE_FAIL(2, "人工审核拒绝操作"),
    ONLINE(3, "上线"),
    DOWN(4, "下线"),
    DEL(5, "删除"),
    RECOVERY(6, "删除恢复"),
    ALL_SEE_ME(7, "全部可见"),
    ONLY_SEE_ME(8, "仅我可见"),
    CAN_FAV(9, "允许点赞"),
    NO_CAN_FAV(10, "不允许点赞"),
    TOP(11, "置顶"),
    NO_TOP(12, "取消置顶"),
    ;
    private Integer code;
    private String desc;

    public static DynamicOperationEnum find(Integer code) {
        for (DynamicOperationEnum enumd : values()) {
            if (enumd.getCode().equals(code)) {
                return enumd;
            }
        }
        throw new RuntimeException("'DynamicOperationEnum' not found By " + code);
    }
}
