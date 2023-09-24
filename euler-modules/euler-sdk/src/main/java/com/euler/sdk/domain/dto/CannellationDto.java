package com.euler.sdk.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("注销会员业务对象")

@Data
public class CannellationDto {
    @ApiModelProperty(value = "会员id")
    private Long memberId;
    @ApiModelProperty(value = "注销原因", required = true)
    private String reason;
}
