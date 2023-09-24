package com.euler.risk.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("登录配置结果对象")
public class LoginConfigResDto {

    /**
     * 一键登录(0:开 1:关 2:不可用)
     */
    @ApiModelProperty("一键登录(0:开 1:关 2:不可用)")
    private String mobileLogin;

    /**
     * 游客登录(0:开 1:关 2:不可用)
     */
    @ApiModelProperty("游客登录(0:开 1:关 2:不可用)")
    private String idLogin;

    /**
     * 验证码登录(0:开 1:关 2:不可用)
     */
    @ApiModelProperty("验证码登录(0:开 1:关 2:不可用)")
    private String captchaLogin;

    /**
     * 账号密码登录(0:开 1:关 2:不可用)
     */
    @ApiModelProperty("账号密码登录(0:开 1:关 2:不可用)")
    private String passwordLogin;
}
