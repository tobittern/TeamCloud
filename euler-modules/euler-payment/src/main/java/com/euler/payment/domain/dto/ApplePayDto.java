package com.euler.payment.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ApplePayDto {

    /**
     * 凭据
     */
    @NotBlank(message = "凭据不能为空")
    private String receipt;

    /**
     * 订单号
     */
    @NotBlank(message = "订单号不能为空")
    private String transactionId;

}
