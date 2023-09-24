package com.euler.sdk.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SensitiveDto {
    @ApiModelProperty("真实姓名")
    private String realName = "";

    @ApiModelProperty("身份证号")
    private String idCardNo = "";

    @ApiModelProperty("用户邮箱")
    private String email = "";

    @ApiModelProperty("手机号码")
    private String mobile = "";
}
