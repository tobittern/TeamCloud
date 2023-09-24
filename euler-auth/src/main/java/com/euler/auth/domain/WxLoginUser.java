package com.euler.auth.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author euler
 * @date 2022-06-01
 */
@Data
@NoArgsConstructor
public class WxLoginUser {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别
     */
    private String sex;
}
