package com.euler.common.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 交易状态
 */
@Slf4j
@Getter
@AllArgsConstructor
public enum WxPayStatusEnum {

    /**
     * 支付成功
     */
    SUCCESS(1, "SUCCESS"),


    /**
     * 转入退款
     */
    REFUND(2, "REFUND"),

    /**
     * 未支付
     */
    NOTPAY(3, "NOTPAY"),

    /**
     * 已关闭
     */
    CLOSED(4, "CLOSED"),

    /**
     * 已撤销（付款码支付）
     */
    REVOKED(5, "REVOKED"),


    /**
     * 用户支付中（付款码支付）
     */
    USERPAYING(6, "USERPAYING"),

    /**
     * 支付失败(其他原因，如银行返回失败)
     */
    PAYERROR(6, "PAYERROR");

    private Integer status;
    private String code;


    public static WxPayStatusEnum find(String value) {
        for (WxPayStatusEnum enumd : values()) {
            if (enumd.getCode().equals(value)) {
                return enumd;
            }
        }
        log.info("WxTradeStatusEnum未找到：{}",value);
        return null;
    }

}
