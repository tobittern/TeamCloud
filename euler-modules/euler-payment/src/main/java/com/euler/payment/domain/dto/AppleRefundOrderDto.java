package com.euler.payment.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AppleRefundOrderDto {

    /**
     * 退款凭据
     */
    @NotBlank(message = "退款凭据不能为空")
    private String signedPayload;

}
