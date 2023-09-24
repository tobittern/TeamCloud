package com.euler.payment.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("前端订单状态查询结果")
@Data
public class FrontQueryResultDto {
    @ApiModelProperty(value = "订单号")
    private String businessOrderId;
    @ApiModelProperty("订单状态，1：待付款，2：已支付，3：交易成功，4：交易关闭")
    private Integer orderState;
}
