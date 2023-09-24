package com.euler.sdk.api.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("新版登录返回对象")
public class NewLoginResultDto implements Serializable {


    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("登录账号/ID")
    private String account;


    @ApiModelProperty("手机号")
    private  String mobile;


    @ApiModelProperty("头像")
    private String avatar;


    @ApiModelProperty("上次登录时间")
    private String lastLoginDate;


    @ApiModelProperty("用户性别（1男 0女 2未知）")
    private String sex;

    @ApiModelProperty("角色信息")
    private NewLoginRoleDto loginRole;

    @ApiModelProperty("状态")
    private  Integer status=200;

    @ApiModelProperty("消息")
    private  String msg;



}
