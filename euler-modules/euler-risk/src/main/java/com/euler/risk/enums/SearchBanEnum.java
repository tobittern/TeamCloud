package com.euler.risk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备号枚举
 */
@Getter
@AllArgsConstructor
public enum SearchBanEnum {

    PHONE(1, "手机号"),
    USERID(2, "用户ID"),
    IP(3, "IP"),
    IDCARD(4, "身份证"),
    DEVICE(5, "设备"),
    MUCHUSER(6, "按照一批用户ID"),
    UNIFIED_DEVICE(7, "同一设备编码");


    private final Integer searchCode;
    private final String searchName;

    public static SearchBanEnum find(Integer num) {
        for (SearchBanEnum enumd : values()) {
            if (enumd.getSearchCode().equals(num)) {
                return enumd;
            }
        }

        return null;
    }

}
