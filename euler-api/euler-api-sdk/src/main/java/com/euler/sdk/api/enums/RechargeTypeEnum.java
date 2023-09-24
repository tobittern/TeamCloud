package com.euler.sdk.api.enums;

import com.euler.common.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 充值类型
 */
@Getter
@AllArgsConstructor
public enum RechargeTypeEnum {

    score (1, "积分"),
    balance(2, "余额"),
    platform_currency(3, "平台币"),
    growth_value(4, "成长值"),
    ;
    private Integer code;
    private String desc;

    public static RechargeTypeEnum find(Integer code) {
        for (RechargeTypeEnum enumd : values()) {
            if (enumd.getCode().equals(code)) {
                return enumd;
            }
        }
        throw new ServiceException("无此类型的钱包");
    }
}
