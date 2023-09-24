package com.euler.auth.form;

import com.euler.common.core.constant.UserConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("用户修改密码")
public class UpdatePwdDto {

    /**
     * 用户旧密码
     */
    @NotBlank(message = "{user.password.not.blank}")
    @Length(min = UserConstants.PASSWORD_MIN_LENGTH, max = UserConstants.PASSWORD_MAX_LENGTH, message = "{user.password.length.valid}")
    @ApiModelProperty(value = "用户密码")
    private String oldPassword;

    /**
     * 用户新密码
     */
    @NotBlank(message = "{user.password.not.blank}")
    @Length(min = UserConstants.PASSWORD_MIN_LENGTH, max = UserConstants.PASSWORD_MAX_LENGTH, message = "{user.password.length.valid}")
    @ApiModelProperty(value = "用户新密码")
    private String newPassword;

    /**
     * 用户确定新密码
     */
    @NotBlank(message = "{user.password.not.blank}")
    @Length(min = UserConstants.PASSWORD_MIN_LENGTH, max = UserConstants.PASSWORD_MAX_LENGTH, message = "{user.password.length.valid}")
    @ApiModelProperty(value = "用户确定新密码")
    private String confirmPassword;


    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型，1：sdk，2：开放平台，3：管理后台")
    @NotBlank(message = "用户类型不能为空")

    private String userType;


    /**
     * true:登录补充密码,false:常规修改
     */
    @ApiModelProperty(value = "登录补充密码")
    @NotNull(message = "用户类型不能为空")

    private Boolean fillPassword;
}
