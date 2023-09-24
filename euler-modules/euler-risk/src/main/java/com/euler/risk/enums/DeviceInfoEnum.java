package com.euler.risk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备号枚举
 */
@Getter
@AllArgsConstructor
public enum DeviceInfoEnum {

    mac(1, "mac"),
    oaid(2, "oaid"),
    imei(3, "imei"),
    android(4, "android"),
    uuid(5, "uuid"),
    idfa(6, "idfa"),
    pushId(7, "pushId");


    private final Integer deviceNum;
    private final String deviceCode;

    public static DeviceInfoEnum find(Integer num) {
        for (DeviceInfoEnum enumd : values()) {
            if (enumd.getDeviceNum().equals(num)) {
                return enumd;
            }
        }

        return null;
    }

}
