package com.euler.auth.form;

import com.euler.common.core.constant.UserConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 用户注册对象
 *
 * @author euler
 */
@Data
@ApiModel("用户注册对象")
public class RegisterBody   {


    /**
     * 邮箱或手机号注册用户或找回密码
     */
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名，手机号、邮箱")
    private String userName;

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    private String account;


    /**
     * 用户密码
     */
    @NotBlank(message = "{user.password.not.blank}")
    @Length(min = UserConstants.PASSWORD_MIN_LENGTH, max = UserConstants.PASSWORD_MAX_LENGTH, message = "{user.password.length.valid}")
    @ApiModelProperty(value = "用户密码")
    private String password;

    /**
     * 确认密码
     */
    @ApiModelProperty(value = "确认密码")
    private  String confirmPwd;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String code;

    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型，1：sdk，2：开放平台，3：管理后台")
    private String userType;

    /**
     * 平台
     */
    @ApiModelProperty(value = "注册平台，1：sdk，2：开放平台，3：管理后台 4：APP，5：小程序")
    private Integer platform = 1;


}
