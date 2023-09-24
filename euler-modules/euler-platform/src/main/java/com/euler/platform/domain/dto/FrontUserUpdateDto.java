package com.euler.platform.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("前端用户修改基础信息")
public class FrontUserUpdateDto  {


    /**
     * 用户Id
     */
    @ApiModelProperty(value = "用户Id")
    private Long userId;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String code;

    /**
     * 公告标题
     */
    @ApiModelProperty(value = "真实姓名")
    private String realName;


    /**
     * 公告标题
     */
    @ApiModelProperty(value = "手机号")
    private String phonenumber;

}
