package com.euler.auth.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录对象
 *
 * @author euler
 */
@Data
@NoArgsConstructor
@ApiModel("用户登录对象")
public class LoginBody {


    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String userName;

    /**
     * 用户密码
     */

    @ApiModelProperty(value = "用户密码")
    private String password;


    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型，1：sdk，2：开放平台，3：管理后台")
    private String userType;


    /**
     * 平台
     */
    @ApiModelProperty(value = "登录平台，1：sdk，2：开放平台，3：管理后台 4：APP")
    private Integer platform = 1;

    /**
     * 登录方式
     */
    @ApiModelProperty(value = "登录方式，1：密码，2：验证码，3：手机一键登录，4：多用户时id登录，需要提供code")
    private String loginType;
    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String code;

    /**
     * 是否刚注册
     */
    @ApiModelProperty(value = "是否刚注册")
    private Integer isReg = 0;

}
