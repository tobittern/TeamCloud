package com.euler.platform.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 游戏审核操作类型
 */
@Getter
@AllArgsConstructor
public enum OperationActionTypeEnum {
    Submit(1, "提交审核申请"),
    On(2, "上架了"),
    Off(3, "下架了"),
    Pass(4, "通过了"),
    Reject(5, "驳回了"),
    ;
    private Integer code;
    private String desc;

    public static OperationActionTypeEnum find(String code) {
        for (OperationActionTypeEnum enumd : values()) {
            if (enumd.getCode().equals(code)) {
                return enumd;
            }
        }
        throw new RuntimeException("'GameOperationActionTypeEnum' not found By " + code);
    }
}
