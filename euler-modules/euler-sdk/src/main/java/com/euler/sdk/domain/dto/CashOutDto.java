package com.euler.sdk.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author euler
 * @date 2022-06-01
 */
@Data
public class CashOutDto {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long memberId;

    /**
     * 转账金额
     */
    @ApiModelProperty(value = "转账金额")
    private BigDecimal amount;

    /**
     * 转账备注
     */
    @ApiModelProperty(value = "转账备注")
    private String remark;

    /**
     * openId
     */
    @ApiModelProperty(value = "openId")
    private String openId;

}
