package com.euler.risk.api.enums;

import com.euler.common.core.enums.DeviceEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BehaviorTypeEnum {

    roleCreate("roleCreate","角色创建"),
    register("register","用户注册"),;


    private final String code;

    private final String name;

    public static BehaviorTypeEnum find(String code) {
        for (BehaviorTypeEnum enumd : values()) {
            if (enumd.getCode().equals(code)) {
                return enumd;
            }
        }

        return null;
    }
}
