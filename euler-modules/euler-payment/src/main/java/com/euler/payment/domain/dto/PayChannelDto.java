package com.euler.payment.domain.dto;

import lombok.Data;

@Data
public class PayChannelDto {
    /**
     * 支付渠道，wx，ali，wallet，apple
     */
    private String payChannel;

    /**
     * app,h5,iap
     */
    private  String payType;
    /**
     * 支付渠道编码
     */
    private String payChannelCode;


}
