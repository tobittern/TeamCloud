package com.euler.payment.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LaunchRefundDto {
    @ApiModelProperty(value = "支付订单Id")
    private String payOrderId;
    @ApiModelProperty("退款原因")
    private String refundReason;


}
