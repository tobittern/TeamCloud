package com.euler.payment.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("统一下单结果对象")
public class UnifiedOrderResultDto {
    @ApiModelProperty(value = "订单号")
    private String businessOrderId;

    @ApiModelProperty(value = "支付信息")
    private String orderInfo;

    @ApiModelProperty(value = "下单结果,true:下单成功，false：下单失败")
    private Boolean success=false;

}
