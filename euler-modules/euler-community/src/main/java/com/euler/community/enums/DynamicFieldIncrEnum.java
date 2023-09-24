package com.euler.community.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 动态基础数据增加类型
 */
@Getter
@AllArgsConstructor
public enum DynamicFieldIncrEnum {
    FORWARD(1, "转发"),
    FAV(2, "点赞"),
    CANCEL_FAV(3, "取消点赞"),
    COMMENT(4, "评论"),
    CANCEL_COMMENT(5, "取消评论"),
    COLLECTION(6, "收藏"),
    CANCEL_COLLECTION(7, "取消收藏"),
    CLICK(8, "点击"),
    REPORT(9, "举报"),
    ;

    private Integer code;
    private String desc;

    public static DynamicFieldIncrEnum find(Integer code) {
        for (DynamicFieldIncrEnum enumd : values()) {
            if (enumd.getCode().equals(code)) {
                return enumd;
            }
        }
        throw new RuntimeException("'DynamicFieldIncrEnum' not found By " + code);
    }
}
