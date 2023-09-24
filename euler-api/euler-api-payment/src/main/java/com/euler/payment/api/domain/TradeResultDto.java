package com.euler.payment.api.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class TradeResultDto implements Serializable {
    private String code;
    private String msg = "请求失败";
    private String subCode;
    private String subMsg;

    private boolean success = false;


    /**
     * 支付渠道订单号
     */
    private String tradeNo;

    /**
     * 支付渠道退款订单号
     */
    private String refundTradeNo;

    /**
     * 支付账号
     */
    private String buyerLogonId;
    /**
     * 支渠道订单状态
     */
    private String tradeStatus;
    /**
     * 业务订单号
     */
    private String outTradeNo;
    /**
     * 业务退款单号
     */
    private String outBizNo;

    /**
     * 支付方式，app,h5
     */
    private String payType;

    /**
     * 支付渠道类型,wx,ali
     */
    private String payChannelType;


    /**
     * 业务类型，1：支付，2：退款，3：转账
     */
    private Integer businessType;
}
