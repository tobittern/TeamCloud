package com.euler.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录平台类型
 *
 * @author euler
 */
@Getter
@AllArgsConstructor
public enum LoginPlatformEnum {

//登录平台，1：sdk，2：开放平台，3：管理后台 4：APP
    /**
     * sdk
     */
    SDK("sdk", 1),

    /**
     * 开放平台
     */
    OPEN("open", 2),

    /**
     * 管理后台
     */
    SYS("sys", 3),

    /**
     * app
     */
    APP("app", 4);



    private final String loginPlatformCode;

    private final Integer loginPlatformNum;


    public static LoginPlatformEnum find(Integer loginPlatformNum) {
        for (LoginPlatformEnum enumd : values()) {
            if (enumd.getLoginPlatformNum().equals(loginPlatformNum)) {
                return enumd;
            }
        }

        return SDK;
    }
}
