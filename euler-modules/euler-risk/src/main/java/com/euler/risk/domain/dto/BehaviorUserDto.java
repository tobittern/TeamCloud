package com.euler.risk.domain.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户行为的用户信息
 *
 * @author euler
 * @date 2022-08-24
 */
@Data
@Accessors(chain = true)
public class BehaviorUserDto implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 账号
     */
    private String account;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 用户id，不登录为0
     */
    private Long userId;


}
