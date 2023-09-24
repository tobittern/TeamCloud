package com.euler.risk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 心跳检测枚举
 */
@Getter
@AllArgsConstructor
public enum HeartBeatEnum {

    HEART_BAN(1, "heart_ban");


    private final Integer deviceNum;
    private final String deviceCode;

    public static HeartBeatEnum find(Integer num) {
        for (HeartBeatEnum enumd : values()) {
            if (enumd.getDeviceNum().equals(num)) {
                return enumd;
            }
        }

        return null;
    }

}
