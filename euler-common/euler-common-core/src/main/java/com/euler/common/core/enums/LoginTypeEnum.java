package com.euler.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录方式
 *
 * @author euler
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnum {


    /**
     * 密码
     */
    PASSWORD("password", "1"),

    /**
     * 验证码
     */
    CAPTCHA("captcha", "2"),

    /**
     * 手机一键登录
     */
    MOBILESIGN("mobilesign", "3"),

    /**
     * 使用用户id登录
     */
    IDLOGIN("idLogin", "4");


    private final String loginType;

    private final String loginTypeNum;


    public static LoginTypeEnum find(String loginTypeNum) {
        for (LoginTypeEnum enumd : values()) {
            if (enumd.getLoginTypeNum().equals(loginTypeNum)) {
                return enumd;
            }
        }
        throw new RuntimeException("'LoginType' not found By " + loginTypeNum);
    }
}
