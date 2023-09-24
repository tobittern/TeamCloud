package com.euler.sdk.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("校验用户登录信息对象")

@Data
public class CheckLoginDto {
    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "用户id不能为空")
    private String uid;
    @ApiModelProperty(value = "用户token")
    @NotBlank(message = "用户token不能为空")
    private String token;
}
