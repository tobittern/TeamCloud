package com.euler.common.payment.bean;

import lombok.Data;

@Data
public class Trade {

    /**
     * 支付类型，app,h5,apple
     */
    private  String payType;

    /**
     * 主体
     */
    private  String body;
    /**
     * 标题
     */
    private  String subject;
    /**
     * 商户订单号，业务订单号
     */
    private  String outTradeNo;
    /**
     * 支付宝交易号
     */
    private  String tradeNo;
    /**
     * 总金额
     */
    private  String totalAmount;

    /**
     * 退款金额
     */
    private String refundAmount;

    /**
     * 退款请求号
     */
    private  String outRequestNo;

    /**
     * 退款原因
     */
    private  String refundReason;

    private  String businessOrderId;

}
