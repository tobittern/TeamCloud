package com.euler.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 设备信息
 */
@Getter
@AllArgsConstructor
public enum DeviceEnum {
    //0：pc，1：android，2：ios，3：h5，4：小程序
    PC("pc", 0),
    ANDROID("android", 1),
    IOS("ios", 2),
    H5("h5", 3),
    MINIAPP("miniapp", 4),

    ;

    private final String deviceCode;

    private final Integer deviceNum;

    public static DeviceEnum find(Integer num) {
        for (DeviceEnum enumd : values()) {
            if (enumd.getDeviceNum().equals(num)) {
                return enumd;
            }
        }

        return H5;
    }

}
