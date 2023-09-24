package com.euler.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 交易状态
 */
@Slf4j
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {


    /**
     * 交易状态：交易创建，等待买家付款
     */
    WAIT_BUYER_PAY(1, "WAIT_BUYER_PAY"),


    /**
     * 交易状态：交易支付成功
     */
    TRADE_SUCCESS(2, "TRADE_SUCCESS"),

    /**
     * 交易状态：交易结束，不可退款
     */
    TRADE_FINISHED(3, "TRADE_FINISHED"),

    /**
     * 交易状态：未付款交易超时关闭，或支付完成后全额退款
     */
    TRADE_CLOSED(4, "TRADE_CLOSED"),

    /**
     * 退款状态：退款成功
     */
    REFUND_SUCCESS(5, "REFUND_SUCCESS"),


    /**
     * 交易不存在
     */
    TRADE_NOT_EXIST(6,"TRADE_NOT_EXIST"),


;

    private Integer status;
    private String code;


    public static OrderStatusEnum find(String value) {
        for (OrderStatusEnum enumd : values()) {
            if (enumd.getCode().equals(value)) {
                return enumd;
            }
        }
        log.info("OrderStatusEnum未找到：{}",value);
        return null;
    }

}
