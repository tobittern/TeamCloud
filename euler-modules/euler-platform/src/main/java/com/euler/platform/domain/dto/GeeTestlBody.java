package com.euler.platform.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 极验对象
 *
 * @author euler
 */
@Data
@ApiModel("发送邮件请求")
public class GeeTestlBody {

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    @NotBlank(message = "{user.email.not.blank}")
    @Email(message = "{user.email.not.valid}")
    private  String email;


    /**
     * 状态 0不通过 1通过
     */
    @ApiModelProperty(value = "状态 0不通过 1通过")
    private Integer status = 0;

    /**
     * 验证流水号
     */
    @ApiModelProperty(value = "验证流水号")
    private String lotNumber;

    /**
     * 验证输出信息
     */
    @ApiModelProperty(value = "验证输出信息")
    private String captchaOutput;

    /**
     * 验证通过标识
     */
    @ApiModelProperty(value = "验证通过标识")
    private String passToken;

    /**
     * 验证通过时间戳
     */
    @ApiModelProperty(value = "验证通过时间戳")
    private String genTime;
}
