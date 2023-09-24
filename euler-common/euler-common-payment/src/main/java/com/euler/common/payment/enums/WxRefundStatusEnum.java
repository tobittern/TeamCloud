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
public enum WxRefundStatusEnum {

    /**
     * 退款成功
     */
    SUCCESS(1, "SUCCESS"),
    /**
     * 退款关闭
     */
    CLOSED(2, "CLOSED"),

    /**
     * 退款异常
     */
    ABNORMAL(3, "ABNORMAL");



    private Integer status;
    private String code;


    public static WxRefundStatusEnum find(String value) {
        for (WxRefundStatusEnum enumd : values()) {
            if (enumd.getCode().equals(value)) {
                return enumd;
            }
        }
        log.info("WxTradeStatusEnum未找到：{}",value);
        return null;
    }

}
