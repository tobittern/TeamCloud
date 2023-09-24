package com.euler.common.core.enums;

import com.euler.common.core.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备类型
 * 针对多套 用户体系
 *
 * @author euler
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnum {

    /**
     * 后台管理
     */
    SYS_USER("sys_user","3"),

    /**
     * 开放平台端
     */
    OPEN_USER("open_user","2"),


    /**
     * sdk用户
     */
    SDK_USER("sdk_user","1");
    private final String userType;

    private final String userTypeNum;

    public static UserTypeEnum find(String str) {
        for (UserTypeEnum enumd : values()) {
            if (enumd.getUserTypeNum().equals(str)) {
                return enumd;
            }
        }
        throw new RuntimeException("'UserType' not found By " + str);
    }


    public static UserTypeEnum getUserType(String str) {
        for (UserTypeEnum value : values()) {
            if (StringUtils.contains(str, value.getUserType())) {
                return value;
            }
        }
        throw new RuntimeException("'UserType' not found By " + str);
    }
}
